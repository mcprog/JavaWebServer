package javawebserver;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class HttpResponse {

    private final HeadersMap responseHeaders;
    private final int statusCode;
    private final Optional<Object> entity;

    /**
     * Headers should contain the following:
     * Date: < date >
     * Server: < my server >
     * Content-Type: text/plain, application/json etc...
     * Content-Length: size of payload
     */
    protected HttpResponse(final HeadersMap responseHeaders, final int statusCode, final Optional<Object> entity) {
        this.responseHeaders = responseHeaders;
        this.statusCode = statusCode;
        this.entity = entity;
    } 

    public HeadersMap getResponseHeaders() {
        return responseHeaders;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Optional<Object> getEntity() {
        return entity;
    }

    public static class Builder {

        private HeadersMap responseHeaders;
        private int statusCode;
        private Optional<Object> entity;

        public Builder() {
            responseHeaders = new HeadersMap();
            responseHeaders.put("Server", List.of("MyServer"));
            responseHeaders.put("Date", List.of(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC))));

            entity = Optional.empty();
        }

        public Builder setResponseHeaders(HeadersMap responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder addHeader(final String name, final String value) {
            responseHeaders.put(name, List.of(value));
            return this;
        }

        public Builder setEntity(Object entity) {
            if (entity != null) {
                this.entity = Optional.of(entity);
            }

            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(responseHeaders, statusCode, entity);
        }

        public HttpResponse buildBadRequest() {
            return new HttpResponse.Builder().setStatusCode(400).setEntity("Bad request..").build();
        }

        public HttpResponse buildRouteNoteFound() {
            return new HttpResponse.Builder().setStatusCode(404).setEntity("Route not found..").build();
        }
    }

}
