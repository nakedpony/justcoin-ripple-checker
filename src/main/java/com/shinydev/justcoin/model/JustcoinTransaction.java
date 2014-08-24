package com.shinydev.justcoin.model;

import com.google.common.base.Objects;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class JustcoinTransaction {
    private String id;
    private String type;
    private long timestamp;
    private DateTime dateTime;
    private String currency;
    private BigDecimal amount;

    public JustcoinTransaction(String id, String type, long timestamp, DateTime dateTime, String currency, BigDecimal amount) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.dateTime = dateTime;
        this.currency = currency;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("timestamp", timestamp)
                .add("dateTime", dateTime)
                .add("currency", currency)
                .add("amount", amount)
                .toString();
    }
}
