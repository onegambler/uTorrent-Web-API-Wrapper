package com.utorrent.webapiwrapper.restclient.exceptions;

import org.apache.http.StatusLine;

public class ForbiddenException extends ClientRequestException {

    public ForbiddenException(StatusLine statusLine) {
        super(statusLine);
    }
}
