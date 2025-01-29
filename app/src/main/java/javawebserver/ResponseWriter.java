package javawebserver;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResponseWriter {

    public static final String NEW_LINE = "\r\n";

    /**
     * Write HttpResponse to a specified outputStream
     * @param outputStream - the outputStream (BufferedWriter)
     * @param response - the Http Response
     */
    public static void writeResponse(final BufferedWriter outputStream, final HttpResponse response) {
        try {
            final int statusCode = response.getStatusCode();
            final String statusCodeMeaning = HttpStatusCode.STATUS_CODES.get(statusCode);
            final List<String> responseHeaders = buildHeaderStrings(response.getResponseHeaders());

            // Note: Windows uses /r/n (carraige return, line feed)
            outputStream.write("HTTP/1.1 " + statusCode + " " + statusCodeMeaning + NEW_LINE);

            for (String header : responseHeaders) {
                outputStream.write(header);
            }

            final Optional<String> entityString = response.getEntity().flatMap(ResponseWriter::getResponseString);
            if (entityString.isPresent()) {
                final String encodedString = new String(entityString.get().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                outputStream.write("Content-Length: " + encodedString.getBytes().length + NEW_LINE);
                outputStream.write(NEW_LINE);
                outputStream.write(encodedString);
            } else {
                outputStream.write(NEW_LINE);
            }
            
        } catch (Exception e) {
            System.err.println(e);
            // TODO: handle exception
        }
    }

    protected static List<String>buildHeaderStrings(final Map<String, List<String>> responseHeaders) {
        final List<String> responseHeaderList = new ArrayList<>();

        responseHeaders.forEach((name, values) -> {
            final StringBuilder valuesCombined = new StringBuilder();
            values.forEach(valuesCombined::append);
            valuesCombined.append(";");

            responseHeaderList.add(name + ": " + valuesCombined + NEW_LINE);

        });

        return responseHeaderList;
    }

    protected static Optional<String> getResponseString(final Object entity) {
        // Only support strings
        if (entity instanceof String) {
            try {
                return Optional.of(entity.toString());
            } catch (Exception e) {
                System.err.println(e);
                // TODO: handle exception
            }
        }

        return Optional.empty();
    }

}
