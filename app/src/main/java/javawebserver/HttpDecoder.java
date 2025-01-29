package javawebserver;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javawebserver.HttpRequest.Builder;;

public class HttpDecoder {

    public static Optional<HttpRequest> decode(final InputStream inputStream) {
        return readMessage(inputStream).flatMap(HttpDecoder::buildRequest);
    }

    protected static Optional<List<String>> readMessage(final InputStream inputStream) {
        try {
            if (!(inputStream.available() > 0)) {
                return Optional.empty();
            }

            final char[] characterBuffer = new char[inputStream.available()];
            final InputStreamReader reader = new InputStreamReader(inputStream);
            // Note return value is just how many characters have successfully been transferred into the buffer
            final int charactersRead = reader.read(characterBuffer);

            List<String> message = new ArrayList<>();
            System.out.println(new String(characterBuffer));

            try (Scanner scanner = new Scanner(new String(characterBuffer))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    message.add(line);
                }
            }

            return Optional.of(message);
            
        } catch (Exception e) {
            System.err.println(e);
            // TODO: handle exception
        }

        return Optional.empty();
    }

    protected static Optional<HttpRequest> buildRequest(List<String> message) {
        if (message.isEmpty()) {
            return Optional.empty();
        }

        String firstLine = message.get(0);
        // Note eg firstLine: HTTP/1.1 200 OK
        String[] httpInfo = firstLine.split(" ");

        if (httpInfo.length != 3) {
            return Optional.empty();
        }

        String protocolVersion = httpInfo[2];
        if (!protocolVersion.equals("HTTP/1.1")) {
            return Optional.empty();
        }

        try {
            Builder requestBuilder = new Builder();
            requestBuilder.setHttpMethod(HttpMethod.valueOf(httpInfo[0]));
            requestBuilder.setUri(new URI(httpInfo[1]));
            return Optional.of(addRequestHeaders(message, requestBuilder));
        } catch (URISyntaxException | IllegalArgumentException e) {
            System.err.println(e);
        }

        return Optional.empty();
    }

    protected static HttpRequest addRequestHeaders(final List<String> message, final Builder builder) {
        final HeadersMap requestHeaders = new HeadersMap();

        // Note first line is already read in buildRequest()
        if (message.size() > 1) {
            for (int i = 1; i < message.size(); ++i) {
                String header = message.get(i);
                int colonIndex = header.indexOf(':');

                if (colonIndex <= 0 || header.length() < colonIndex) {
                    break;
                }

                String headerName = header.substring(0, colonIndex);
                String headerValue = header.substring(colonIndex + 1);

                requestHeaders.compute(headerName, (key, values) -> {
                    if (values != null) {
                        values.add(headerValue);
                    } else {
                        values = new ArrayList<>();
                    }
                    return values;
                });
            }
        }

        builder.setRequestHeaders(requestHeaders);
        return builder.build();
    }

}
