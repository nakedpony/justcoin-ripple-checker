package com.shinydev.ripple.model.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class Marker {
    @JsonProperty("ledger")
    private long ledger;
    @JsonProperty("seq")
    private long seq;

    public long getLedger() {
        return ledger;
    }

    public long getSeq() {
        return seq;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ledger", ledger)
                .add("seq", seq)
                .toString();
    }
}
