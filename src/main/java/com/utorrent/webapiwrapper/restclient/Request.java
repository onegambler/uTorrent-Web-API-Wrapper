package com.utorrent.webapiwrapper.restclient;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Request {

    private URI uri;
    private Set<QueryParam> params;
    private Set<FilePart> files;

    private Request(URI uri, Set<QueryParam> params, Set<FilePart> files) {
        this.uri = uri;
        this.params = params;
        this.files = files;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    @Getter
    @EqualsAndHashCode
    public static class FilePart {
        private String name;
        private File file;
        private ContentType contentType;

        public FilePart(String name, File file, ContentType contentType) {
            this.name = name;
            this.file = file;
            this.contentType = contentType;
        }
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class QueryParam {
        private final String name;
        private final String value;
    }

    public static class RequestBuilder {
        private URI uri;
        private Set<QueryParam> params;
        private Set<FilePart> files;

        private RequestBuilder() {
            params = new HashSet<>();
            files = new HashSet<>();
        }

        public RequestBuilder addQueryParameter(String name, String value) {
            this.params.add(new QueryParam(name, value));
            return this;
        }

        public RequestBuilder addFile(String name, File file, ContentType contentType) {
            this.files.add(new FilePart(name, file, contentType));
            return this;
        }

        public RequestBuilder setDestination(URI uri) {
            this.uri = uri;
            return this;
        }

        public RequestBuilder setDestination(String path) {
            this.uri = URI.create(path);
            return this;
        }

        public Request create() {
            Objects.requireNonNull(uri, "Destination cannot be null");
            return new Request(uri, params, files);
        }
    }
}
