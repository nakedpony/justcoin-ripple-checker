package com.shinydev.ripple.model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class Transaction {

    @JsonProperty("tx")
    private TransactionTx transactionTx;

    @JsonProperty("validated")
    private boolean validated;

    public TransactionTx getTransactionTx() {
        return transactionTx;
    }

    public boolean isValidated() {
        return validated;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("transactionTx", transactionTx)
                .add("validated", validated)
                .toString();
    }
}
