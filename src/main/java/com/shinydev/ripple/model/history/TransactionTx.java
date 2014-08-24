package com.shinydev.ripple.model.history;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.shinydev.ripple.model.common.TakerCurrency;

import java.math.BigDecimal;

public class TransactionTx {

    @JsonProperty("Account")
    private String account;

    @JsonProperty("Amount")
    private TakerCurrency amount;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("DestinationTag")
    private long destinationTag;

    @JsonProperty("Fee")
    private BigDecimal fee;
    @JsonProperty("Flags")
    private long flags;
    @JsonProperty("OfferSequence")
    private long offersSequence;
    @JsonProperty("Sequence")
    private long sequence;
    @JsonProperty("SigningPubKey")
    private String signingPubKey;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("TxnSignature")
    private String txnSignature;
    @JsonProperty("date")
    private int date;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("inLedger")
    private long inLedger;
    @JsonProperty("ledger_index")
    private long ledgerIndex;

    public TransactionTx() {
    }

    public String getAccount() {
        return account;
    }

    public TakerCurrency getAmount() {
        return amount;
    }

    public String getDestination() {
        return destination;
    }

    public long getDestinationTag() {
        return destinationTag;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public long getFlags() {
        return flags;
    }

    public long getOffersSequence() {
        return offersSequence;
    }

    public long getSequence() {
        return sequence;
    }

    public String getSigningPubKey() {
        return signingPubKey;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTxnSignature() {
        return txnSignature;
    }

    public int getDate() {
        return date;
    }

    public String getHash() {
        return hash;
    }

    public long getInLedger() {
        return inLedger;
    }

    public long getLedgerIndex() {
        return ledgerIndex;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("account", account)
                .add("amount", amount)
                .add("destination", destination)
                .add("destinationTag", destinationTag)
                .add("fee", fee)
                .add("flags", flags)
                .add("offersSequence", offersSequence)
                .add("sequence", sequence)
                .add("signingPubKey", signingPubKey)
                .add("transactionType", transactionType)
                .add("txnSignature", txnSignature)
                .add("date", date)
                .add("hash", hash)
                .add("inLedger", inLedger)
                .add("ledgerIndex", ledgerIndex)
                .toString();
    }
}
