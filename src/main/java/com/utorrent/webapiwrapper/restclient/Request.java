package com.utorrent.webapiwrapper.restclient;

import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Request {

    private final URI uri;
    private final Set<QueryParam> params;
    private final Set<FilePart> files;

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    @Data
    public static class FilePart {
        private final String name;
        private final File file;
        private final ContentType contentType;

        public FilePart(String name, File file, ContentType contentType) {
            this.name = name;
            this.file = file;
            this.contentType = contentType;
        }
    }

    @Data
    public static class QueryParam {
        private final String name;
        private final String value;
    }

    public static class RequestBuilder {
        private URIBuilder uriBuilder;
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
            this.uriBuilder = new URIBuilder(uri);
            return this;
        }

        public Request create() {
            Objects.requireNonNull(uriBuilder, "Destination cannot be null");
            try {
                return new Request(uriBuilder.build(), params, files);
            } catch (URISyntaxException e) {
                throw Throwables.propagate(e);
            }
        }
    }
}
