package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class UnauthorizedException extends ClientRequestException {
    public UnauthorizedException(StatusLine statusLine) {
        super(statusLine);
    }
}
