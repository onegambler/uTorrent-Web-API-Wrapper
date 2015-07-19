package com.utorrent.webapiwrapper.restclient;

import com.utorrent.webapiwrapper.restclient.exceptions.*;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.assertj.core.api.StrictAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testNotAcceptableException() throws IOException {
        testException(406, NotAcceptableException.class);
    }

    @Test
    public void testNotFoundException() throws IOException {
        testException(404, NotFoundException.class);
    }

    @Test
    public void testForbiddenException() throws IOException {
        testException(403, ForbiddenException.class);
    }

    @Test
    public void testUnauthorizedException() throws IOException {
        testException(401, UnauthorizedException.class);
    }

    @Test
    public void testClientRequestException() throws IOException {
        testException(300, ClientRequestException.class);
        testException(450, ClientRequestException.class);
        testException(500, ClientRequestException.class);
    }

    @Test
    public void testBadRequestException() throws IOException {
        testException(400, BadRequestException.class);
    }

    private <T extends Exception> void testException(int code, Class<T> exception) throws IOException {
        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(httpClient.execute(any(HttpUriRequest.class), any(HttpClientContext.class)))
                .thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("protocol", 0, 1), code, "reason"));
        when(entity.getContent())
                .thenReturn(new ByteArrayInputStream("test".getBytes()));

        assertThatThrownBy(() -> client.get(Request.builder().setDestination(URI.create("127.0.0.1")).create()))
                .isInstanceOf(exception);
    }
}