package com.shinydev.wrappers;

import com.shinydev.ripple.RippleRpcClient;
import com.shinydev.ripple.model.history.Marker;
import com.shinydev.ripple.model.history.Transaction;
import com.shinydev.ripple.model.history.TransactionHistoryResponse;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class RippleWrapper {

    private final static int RIPPLE_TX_LIMIT = 200; //amount of transactions ripple will return per http request
    private final static int MAX_RIPPLE_REQ = 20000; //maximum amount of http requests to ripple
    public final static DateTime RIPPLE_EPOC = new DateTime(2000, 1, 1, 0, 0, DateTimeZone.UTC);

    private final RippleRpcClient client;
    private final DateTimeZone zone;

    public RippleWrapper(RippleRpcClient client, DateTimeZone zone) {
        this.client = client;
        this.zone = zone;
    }

    public List<Transaction> getTransactions(String rippleAddress, DateTime start, DateTime end) {
        checkArgument(start.isBefore(end), "start should be before end");
        checkArgument(start.getZone().equals(zone), "start date should be with the same zone as passed in constructor");
        checkArgument(end.getZone().equals(zone), "end date should be with the same zone as passed in constructor");
        List<Transaction> rippleTransactions = new LinkedList<>();
        List<Transaction> beforeStartTx;
        Marker marker = null;
        int reqCount = 0;
        do {
            TransactionHistoryResponse history = client.getTransactionHistory(rippleAddress, -1, -1, RIPPLE_TX_LIMIT, Optional.empty(), marker);
            beforeStartTx = history.getTransactions().parallelStream()
                    .filter(tx -> getDateTime(tx).isBefore(start))
                    .collect(Collectors.toList());
            rippleTransactions.addAll(history.getTransactions());
            marker = history.getMarker();
            reqCount++;
        } while (marker != null && reqCount < MAX_RIPPLE_REQ && beforeStartTx.size() == 0);

        return rippleTransactions.parallelStream()
                .filter(tx -> getDateTime(tx).isAfter(start))
                .filter(tx -> getDateTime(tx).isBefore(end))
                .collect(Collectors.toList());
    }

    private DateTime getDateTime(Transaction tx) {
        return RIPPLE_EPOC.plusSeconds(tx.getTransactionTx().getDate()).withZone(zone);
    }
}
