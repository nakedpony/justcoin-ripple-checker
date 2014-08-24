package com.shinydev.ripple;

import com.shinydev.ripple.model.history.Marker;
import com.shinydev.ripple.model.history.TransactionHistoryRequest;
import com.shinydev.ripple.model.history.TransactionHistoryResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.util.Optional;

public class RippleRpcClient {

    private WebResource webTarget;

    public RippleRpcClient(String rippleUrl, Client client) {
        webTarget = client.resource(rippleUrl);
    }

    public TransactionHistoryResponse getTransactionHistory(String account, long ledgerIndexMin, long ledgerIndexMax, int limit, Optional<Integer> offset, Marker marker) {
        TransactionHistoryRequest request = new TransactionHistoryRequest(account, ledgerIndexMin, ledgerIndexMax, limit, offset, marker);
        return webTarget.entity(request, MediaType.APPLICATION_JSON_TYPE).post(TransactionHistoryResponse.class);
    }

}
