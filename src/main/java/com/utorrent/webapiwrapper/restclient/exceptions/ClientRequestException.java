package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class ClientRequestException extends RuntimeException {

    public ClientRequestException(StatusLine statusLine) {
        super(String.format("Error %d:. %s.", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
    }
}
