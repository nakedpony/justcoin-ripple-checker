package com.shinydev.wrappers;

import au.com.bytecode.opencsv.CSVReader;
import com.shinydev.justcoin.model.JustcoinTransaction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class JustcoinWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(JustcoinWrapper.class);

    private final String justcoinCvsFileLocation;
    private final DateTimeZone zone;

    public JustcoinWrapper(String justcoinCvsFileLocation, DateTimeZone zone) {
        this.justcoinCvsFileLocation = justcoinCvsFileLocation;
        this.zone = zone;
    }

    public List<JustcoinTransaction> getJustcoinCreditTransactions(DateTime start, DateTime end) throws IOException {
        checkArgument(start.getZone().equals(zone), "start date should be with the same zone as passed in constructor");
        checkArgument(end.getZone().equals(zone), "end date should be with the same zone as passed in constructor");
        CSVReader reader = new CSVReader(new FileReader(justcoinCvsFileLocation));
        List<String[]> myEntries = new ArrayList<>();
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if ("id".equals(nextLine[0])) {
                LOG.debug("Parsed Justcoin CVS header");
            } else {
                myEntries.add(nextLine);
            }
        }
        List<JustcoinTransaction> justcoinTransactions = myEntries.stream()
                .map(entry -> new JustcoinTransaction(
                        entry[0],
                        entry[1],
                        Long.valueOf(entry[2]), //long
                        DateTime.parse(entry[3]).withZone(zone),
                        entry[4],
                        new BigDecimal(entry[5]) //big decimal
                ))
                .collect(Collectors.toList());

        return justcoinTransactions.stream()
                .filter(tx -> tx.getDateTime().isAfter(start))
                .filter(tx -> tx.getDateTime().isBefore(end))
                .collect(Collectors.toList());
    }
}
