package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class NotFoundException extends ClientRequestException {

    public NotFoundException(StatusLine statusLine) {
        super(statusLine);
    }
}
