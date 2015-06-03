package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class BadRequestException extends ClientRequestException {

    public BadRequestException(StatusLine statusLine) {
        super(statusLine);
    }
}
