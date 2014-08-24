package com.shinydev.ripple.model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.shinydev.ripple.model.common.AbstractRequest;

import java.util.List;
import java.util.Optional;

public class TransactionHistoryRequest extends AbstractRequest {

    private static final String COMMAND = "account_tx";
    private final List<TransactionHistoryParams> historyTransactionParams;

    public TransactionHistoryRequest(String account, long ledgerIndexMin, long ledgerIndexMax, int limit, Optional<Integer> offset, Marker marker) {
        historyTransactionParams = ImmutableList.of(new TransactionHistoryParams(account, ledgerIndexMin, ledgerIndexMax, limit, offset, marker));
    }

    @Override
    public String getMethod() {
        return COMMAND;
    }

    @Override
    public List<TransactionHistoryParams> getParams() {
        return historyTransactionParams;
    }

    private static final class TransactionHistoryParams {

        @JsonProperty("account")
        private final String account;
        @JsonProperty("ledger_index_min")
        private final long ledgerIndexMin; // required, set to -1 for the first fully-validated ledger.
        @JsonProperty("ledger_index_max")
        private final long ledgerIndexMax; // required, set to -1 for the last fully-validated ledger.
        @JsonProperty("forward")
        private final boolean forward = false; // True, to sort in ascending ledger order.
        @JsonProperty("limit")
        private final int limit;
        @JsonProperty("offset")
        private final Integer offset;
        @JsonProperty("marker")
        private final Marker marker;

        private TransactionHistoryParams(String account, long ledgerIndexMin, long ledgerIndexMax, int limit, Optional<Integer> offset, Marker marker) {
            this.account = account;
            this.ledgerIndexMin = ledgerIndexMin;
            this.ledgerIndexMax = ledgerIndexMax;
            this.limit = limit;
            this.offset = offset.orElse(null);
            this.marker = marker;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("account", account)
                    .add("ledgerIndexMin", ledgerIndexMin)
                    .add("ledgerIndexMax", ledgerIndexMax)
                    .add("forward", forward)
                    .add("limit", limit)
                    //.add("offset", offset)
                    .add("marker", marker)
                    .toString();
        }
    }
}
