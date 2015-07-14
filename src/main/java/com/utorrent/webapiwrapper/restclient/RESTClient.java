package com.utorrent.webapiwrapper.restclient;

import com.utorrent.webapiwrapper.restclient.exceptions.*;
import com.utorrent.webapiwrapper.utils.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class RESTClient implements Closeable {

    private final CloseableHttpClient client;
    private final HttpClientContext httpClientContext;
    private final RequestConfig requestConfig;

    public RESTClient(ConnectionParams params) {
        requireNonNull(params, "Connection Parameters cannot be null");

        this.httpClientContext = HttpClientContext.create();
        if (params.getCredentials().isPresent()) {
            ConnectionParams.Credentials credentials = params.getCredentials().get();
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(credentials.getUsername(), credentials.getPassword()));
            httpClientContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credentialsProvider);
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setAuthenticationEnabled(params.isAuthenticationEnabled());

        if (params.getTimeout() > 0) {
            requestConfigBuilder.setSocketTimeout(params.getTimeout())
                    .setConnectTimeout(params.getTimeout())
                    .setConnectionRequestTimeout(params.getTimeout());
        }

        requestConfig = requestConfigBuilder.build();
        client = HttpClients.createDefault();
    }

    public String post(Request request) {
        requireNonNull(request, "Request cannot be null");

        MultipartEntityBuilder httpEntityBuilder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        request.getFiles().forEach(file -> httpEntityBuilder.addPart(file.getName(), new FileBody(file.getFile(), file.getContentType())));
        request.getParams().forEach(param -> httpEntityBuilder.addTextBody(param.getName(), param.getValue()));

        HttpUriRequest postRequest = RequestBuilder.post(request.getUri())
                .setEntity(httpEntityBuilder.build())
                .setConfig(requestConfig)
                .build();

        return executeVerb(postRequest);
    }

    public String get(URI uri) {
        HttpUriRequest request = RequestBuilder.get()
                .setUri(uri)
                .setConfig(requestConfig)
                .build();
        return executeVerb(request);
    }

    private String executeVerb(HttpUriRequest httpRequest) {
        try (CloseableHttpResponse httpResponse = client.execute(httpRequest, httpClientContext)) {
            String message = IOUtils.toString(httpResponse.getEntity().getContent());
            validateResponse(httpResponse);
            return message;
        } catch (ClientRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RESTException("Impossible to execute request " + httpRequest.getMethod(), e);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private void validateResponse(HttpResponse response) {
        requireNonNull(response, "Response from server is null");
        requireNonNull(response.getStatusLine(), "Response status is null");
        if (response.getStatusLine().getStatusCode() > 299) {
            switch (response.getStatusLine().getStatusCode()) {
                case 400:
                    throw new BadRequestException(response.getStatusLine());
                case 401:
                    throw new UnauthorizedException(response.getStatusLine());
                case 403:
                    throw new ForbiddenException(response.getStatusLine());
                case 404:
                    throw new NotFoundException(response.getStatusLine());
                case 406:
                    throw new NotAcceptableException(response.getStatusLine());
                default:
                    throw new ClientRequestException(response.getStatusLine());
            }
        }
    }
}
