package com.utorrent.webapiwrapper.restclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
public class ConnectionParams {

    private int port;
    private String host;
    private String scheme;
    private Optional<Credentials> credentials;
    private int timeout;
    private boolean authenticationEnabled;

    private ConnectionParams(String scheme, String host, int port, Optional<Credentials> credentials, int timeout, boolean authenticationEnabled) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.credentials = credentials;
        this.timeout = timeout;
        this.authenticationEnabled = authenticationEnabled;
    }

    public static ConnectionParamsBuilder builder() {
        return new ConnectionParamsBuilder();
    }

    @Getter
    @AllArgsConstructor
    static class Credentials {

        private final String username;
        private final String password;
    }

    public static class ConnectionParamsBuilder {
        private static final String DEFAULT_SCHEME = "http";
        private int port;
        private String scheme = DEFAULT_SCHEME;
        private String host;
        private String username;
        private String password;
        private int timeout;
        private boolean authenticationEnabled;

        public ConnectionParamsBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public ConnectionParamsBuilder withAddress(String host, int port) {
            this.host = host;
            this.port = port;
            return this;
        }

        public ConnectionParamsBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public ConnectionParamsBuilder withCredentials(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        public ConnectionParamsBuilder withTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public ConnectionParamsBuilder enableAuthentication(boolean authenticationEnabled) {
            this.authenticationEnabled = authenticationEnabled;
            return this;
        }

        public ConnectionParamsBuilder withScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public ConnectionParams create() {
            Credentials credentials = null;
            if (Objects.nonNull(username)) {
                requireNonNull(password);
                credentials = new Credentials(username, password);
            }
            requireNonNull(host, "A host value must be specified");

            return new ConnectionParams(scheme, host, port, Optional.ofNullable(credentials), timeout, authenticationEnabled);
        }
    }
}
