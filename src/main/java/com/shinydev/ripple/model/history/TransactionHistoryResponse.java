package com.shinydev.ripple.model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.Objects;

import java.util.List;

@JsonRootName("result")
public class TransactionHistoryResponse {

    @JsonProperty("status")
    protected String status;

    @JsonProperty("marker")
    protected Marker marker;

    @JsonProperty("transactions")
    private List<Transaction> transactions;

    public String getStatus() {
        return status;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Marker getMarker() {
        return marker;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("status", status)
                .add("marker", marker)
                .add("transactions", transactions)
                .toString();
    }
}
