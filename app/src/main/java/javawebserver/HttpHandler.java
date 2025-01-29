package javawebserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Optional;

public class HttpHandler {

    protected final Map<String, RequestRunner> routes;

    public HttpHandler(final Map<String, RequestRunner> routes) {
        this.routes = routes;
    }

    public void handleConnection(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        Optional<HttpRequest> request = HttpDecoder.decode(inputStream);
        request.ifPresentOrElse((r) -> handleRequest(r, writer), () -> handleInvalidRequest(writer));

        writer.close();
        inputStream.close();
    }

    private void handleInvalidRequest(final BufferedWriter writer) {
        // TODO add custom builder func for all this at once
        // new HttpResponse.Builder().buildBadRequest();
        HttpResponse notFound = new HttpResponse.Builder().setStatusCode(400).setEntity("Bad request..").build();
        ResponseWriter.writeResponse(writer, notFound);
    }

    private void handleRequest(final HttpRequest request,final BufferedWriter writer) {
        // TODO add method that gets all this at once
        final String routeKey = request.getHttpMethod().name().concat(request.getUri().getRawPath());

        if (routes.containsKey(routeKey)) {
            // Note: request is run right here
            ResponseWriter.writeResponse(writer, routes.get(routeKey).run(request));
        } else {
            // Key not found
            // TODO add custom builder fun for this all at once
            // new HttpResponse.Builder().builderRequest();
            ResponseWriter.writeResponse(writer, new HttpResponse.Builder().setStatusCode(404).setEntity("Route not found..").build());
        }
    }

}
