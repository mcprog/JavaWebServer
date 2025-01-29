package javawebserver;

import java.net.URI;

public class HttpRequest {

    private final HttpMethod httpMethod;
    private final URI uri;
    private final HeadersMap requestHeaders;

    protected HttpRequest(HttpMethod opCode, URI uri, HeadersMap requestHeaders) {
        this.httpMethod = opCode;
        this.uri = uri;
        this.requestHeaders = requestHeaders;
    }

    public URI getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HeadersMap getRequestHeaders() {
        return requestHeaders;
    }

    public static class Builder {
        private HttpMethod httpMethod;
        private URI uri;
        private HeadersMap requestHeaders;

        public Builder() {

        }

        public void setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public void setRequestHeaders(HeadersMap requestHeaders) {
            this.requestHeaders = requestHeaders;
        }

        public HttpRequest build() {
            return new HttpRequest(httpMethod, uri, requestHeaders);
        }

    }

}
