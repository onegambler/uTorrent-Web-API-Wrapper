package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class NotAcceptableException extends ClientRequestException {

    public NotAcceptableException(StatusLine statusLine) {
        super(statusLine);
    }
}
