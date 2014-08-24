package com.shinydev;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.shinydev.json.ObjectMapperFactory;
import com.shinydev.justcoin.model.JustcoinTransaction;
import com.shinydev.ripple.RippleRpcClient;
import com.shinydev.ripple.model.common.Currency;
import com.shinydev.ripple.model.history.Transaction;
import com.shinydev.wrappers.JustcoinWrapper;
import com.shinydev.wrappers.RippleWrapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Checker {

    private static final Logger LOG = LoggerFactory.getLogger(Checker.class);

    public static final String DEST_TAG_KEY = "ripple.destination.tag";
    public static final String RIPPLE_URL_KEY = "ripple.url";
    public static final String RIPPLE_ADDR_KEY = "ripple.address";
    public static final String CVS_PATH_KEY = "justcoin.cvs.full.path";
    public static final String TIME_ZONE_KEY = "time.zone";
    public static final String TIME_START_KEY = "time.start";
    public static final String TIME_END_KEY = "time.end";


    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            LOG.info("Please provide an absolute path to java properties file as a first argument.");
            LOG.info("Format is the following:\n" +
                                "ripple.destination.tag = 123456\n" +
                                "ripple.url = http://s1.ripple.com:51234\n" +
                                "ripple.address = rD0000000000000000\n" +
                                "justcoin.cvs.full.path = /Users/walterwhite/transactions.csv\n" +
                                "time.zone = Europe/Amsterdam\n" +
                                "time.start = 2014-08-22T12:25:00\n" +
                                "time.end = 2014-08-24T23:59:00");
            System.exit(0);
        }

        try (Reader reader = new FileReader(args[0])) {
            Properties prop = new Properties();
            prop.load(reader);
            long destinationTag = Long.parseLong(prop.getProperty(DEST_TAG_KEY));
            String rippleUrl = prop.getProperty(RIPPLE_URL_KEY);
            String rippleAddress = prop.getProperty(RIPPLE_ADDR_KEY);
            String justcoinCvsFileLocation = prop.getProperty(CVS_PATH_KEY);
            DateTimeZone zone = DateTimeZone.forID(prop.getProperty(TIME_ZONE_KEY));
            DateTime start = DateTime.parse(prop.getProperty(TIME_START_KEY)).withZone(zone);
            DateTime end = DateTime.parse(prop.getProperty(TIME_END_KEY)).withZone(zone);

            JacksonJsonProvider jsonProvider = new JacksonJsonProvider(ObjectMapperFactory.getMapper());
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getSingletons().add(jsonProvider);
            Client jerseyClient = Client.create(clientConfig);
            RippleRpcClient client = new RippleRpcClient(rippleUrl, jerseyClient);
            RippleWrapper rippleWrapper = new RippleWrapper(client, zone);
            JustcoinWrapper justcoinWrapper = new JustcoinWrapper(justcoinCvsFileLocation, zone);

            LOG.info("Going to process transactions between {} and {}. Timezone {}", start, end, zone);

            List<Transaction> rippleTransactions = rippleWrapper.getTransactions(rippleAddress, start, end);
            List<Transaction> rippleToJustcoin = rippleTransactions.parallelStream()
                    .filter(tx -> "Payment".equals(tx.getTransactionTx().getTransactionType()))
                    .filter(tx -> tx.getTransactionTx().getDestinationTag() == destinationTag)
                    .collect(Collectors.toList());
            LOG.debug("Found the following transactions SENT Ripple to Justcoin:");
            rippleToJustcoin.stream().forEach(tx -> LOG.debug("{} {} {}",
                    RippleWrapper.RIPPLE_EPOC.plusSeconds(tx.getTransactionTx().getDate()).withZone(zone),
                    tx.getTransactionTx().getAmount(),
                    tx.getTransactionTx().getHash()));


            List<JustcoinTransaction> justcoinTransactions = justcoinWrapper.getJustcoinCreditTransactions(start, end);
            List<JustcoinTransaction> justcoinCreditTransactions = justcoinTransactions.parallelStream()
                    .filter(tx -> "Credit".equals(tx.getType()))
                    .collect(Collectors.toList());

            LOG.debug("Found the following transactions REACHED to Justcoin:");
            justcoinCreditTransactions.stream().forEach(tx -> LOG.debug(tx.toString()));

            LOG.info("Found {} transactions sent from Ripple and {} reached Justcoin. Trying to get stuck transactions.", rippleToJustcoin.size(), justcoinCreditTransactions.size());

            Map<String, JustcoinTransaction> idToTransactionMap = justcoinCreditTransactions.parallelStream()
                    .collect(Collectors.toMap(JustcoinTransaction::getId, tx -> tx));

            List<Transaction> stuckRippleTransactions = rippleToJustcoin.stream() //shouldn't use parallel here because we delete matched results from map
                    .filter(tx -> !hasMatchedTransaction(tx, idToTransactionMap))
                    .collect(Collectors.toList());

            LOG.info("Found the following stuck transactions: ");
            stuckRippleTransactions.stream()
                    .sorted((tx1, tx2) -> Integer.compare(tx2.getTransactionTx().getDate(), tx1.getTransactionTx().getDate()))
                    .forEach(tx -> LOG.info("{} {} {}",
                            RippleWrapper.RIPPLE_EPOC.plusSeconds(tx.getTransactionTx().getDate()).withZone(zone),
                            tx.getTransactionTx().getAmount(),
                            tx.getTransactionTx().getHash()));

            Double xrp = stuckRippleTransactions.stream()
                    .filter(tx -> tx.getTransactionTx().getAmount().getCurrency() == Currency.XRP)
                    .mapToDouble(tx -> tx.getTransactionTx().getAmount().getValue().doubleValue())
                    .sum();

            Double btc = stuckRippleTransactions.stream()
                    .filter(tx -> tx.getTransactionTx().getAmount().getCurrency() == Currency.BTC)
                    .mapToDouble(tx -> tx.getTransactionTx().getAmount().getValue().doubleValue())
                    .sum();
            LOG.info("Sum of stuck transactions: {} XRP, {} BTC", xrp, btc);
        }
    }

    private static boolean hasMatchedTransaction(Transaction ripple, Map<String, JustcoinTransaction> idToTransactionMap) {
        List<JustcoinTransaction> matched = idToTransactionMap.values().stream()
                .filter(tx -> ripple.getTransactionTx().getAmount().getCurrency().name().equals(tx.getCurrency()) &&
                        ripple.getTransactionTx().getAmount().getValue().equals(tx.getAmount())
                       )
                .collect(Collectors.toList());
        switch (matched.size()) {
            case 1:
                LOG.debug("Successfully matched ripple transaction: {} to {}", ripple, matched.get(0));
                idToTransactionMap.remove(matched.get(0).getId());
                return true;
            case 0:
                LOG.debug("Cannot match ripple transaction: {}", ripple);
                return false;
            default: //more than one. should try to resolve by time matching
                LOG.debug("Multiple transactions were matched. Trying to resolve conflict using time");
                LOG.debug("Ripple tx: {}, justcoin candidates: {}", ripple, matched);
                DateTime rippleTime = RippleWrapper.RIPPLE_EPOC.plusSeconds(ripple.getTransactionTx().getDate());
                List<JustcoinTransaction> filteredByTime = matched.parallelStream()
                        .filter(tx -> Math.abs(new Duration(new DateTime(tx.getTimestamp() * 1000, DateTimeZone.UTC), rippleTime).getStandardSeconds()) < 10)
                        .collect(Collectors.toList());
                if (filteredByTime.size() >= 1) {
                    LOG.debug("Successfully matched ripple transaction: {} to {}", ripple, filteredByTime.get(0));
                    idToTransactionMap.remove(matched.get(0).getId());
                    return true;
                } else {
                    LOG.error("Cannot resolve conflict by time: Ripple tx: {}, justcoin candidates: {}, filtered by time: {}", ripple, matched, filteredByTime);
                    return false;
                }
        }
    }

}
