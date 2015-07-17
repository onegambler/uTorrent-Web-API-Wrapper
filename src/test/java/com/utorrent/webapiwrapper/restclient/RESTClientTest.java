package com.utorrent.webapiwrapper.restclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RESTClientTest {

    @Mock
    private CloseableHttpClient httpClient;

    private RESTClient client;


    @Test(expected = NullPointerException.class)
    public void testPostNullRequest() throws Exception {
        client.post(null);
    }

    @Test
    public void testPost() throws Exception {
        //client.post();

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testNotAcceptableException() {

    }

    @Test
    public void testNotFoundException() {

    }

    @Test
    public void testForbiddenException() {

    }

    @Test
    public void testUnauthorizedException() {

    }

    @Test
    public void testClientRequestException() {

    }

    @Test
    public void testBadRequestException() {

    }
}