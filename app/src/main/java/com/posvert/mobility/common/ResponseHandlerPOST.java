package com.posvert.mobility.common;

/**
 * Created by giovanni on 31/10/16.
 */
public interface ResponseHandlerPOST {
    public void parseResponse(String response);

    public String getJSONMessage();
}
