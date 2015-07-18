package com.utorrent.webapiwrapper.restclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RESTClientTest {

    @Mock
    private CloseableHttpClient httpClient;

    private final ConnectionParams connectionParams = ConnectionParams.builder()
            .withScheme("http")
            .withCredentials("username", "password")
            .withAddress("host.com", 8080)
            .withTimeout(1500)
            .create();


    private RESTClient client;

    @Before
    public void setUp() {
        client = new RESTClient(httpClient, connectionParams);
    }


    @Test(expected = NullPointerException.class)
    public void whenRequestIsNullThenPostThrowException() throws Exception {
        client.post(null);
    }

    @Test(expected = NullPointerException.class)
    public void whenRequestIsNullThenGetThrowException() throws Exception {
        client.get(null);
    }

    @Test
    public void testPost() throws Exception {

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