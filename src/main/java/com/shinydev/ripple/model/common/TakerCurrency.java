package com.shinydev.ripple.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.math.MathContext;

public class TakerCurrency {

    private static final BigDecimal MILLION = new BigDecimal("1000000");

    private final Currency currency;
    private String issuer;
    private final BigDecimal value;

    @JsonCreator
    public TakerCurrency(@JsonProperty("currency") Currency currency,
                         @JsonProperty("issuer") String issuer,
                         @JsonProperty("value") BigDecimal value) {
        this.currency = currency;
        if (currency != Currency.XRP) {
            this.issuer = issuer;
        }
        this.value = value;
    }

    @JsonCreator
    public TakerCurrency(String value) {
        this.currency = Currency.XRP;
        this.issuer = null;
        this.value = new BigDecimal(value).divide(MILLION, MathContext.DECIMAL128); //XRP currency in ripple multiplied by million
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getIssuer() {
        return issuer;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("currency", currency)
                .add("issuer", issuer)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TakerCurrency that = (TakerCurrency) o;

        return Objects.equal(this.currency, that.currency) &&
                Objects.equal(this.issuer, that.issuer) &&
                Objects.equal(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currency, issuer, value);
    }
}
