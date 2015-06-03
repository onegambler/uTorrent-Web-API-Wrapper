package com.utorrent.webapiwrapper.restclient.exceptions;

public class RESTException extends RuntimeException{
    public RESTException(String message, Exception parentException) {
        super(message, parentException);
    }
}
