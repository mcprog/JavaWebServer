/*
 * This source file was generated by the Gradle 'init' task
 */
package javawebserver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

class AppTest {
    /*@Test void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }*/

    @Test void testHttpRequest() {

        //Test 1
        HttpRequest.Builder builder1 = new HttpRequest.Builder();
        builder1.setHttpMethod(HttpMethod.GET);
        URI uri1 = null;
        try {
            uri1 = new URI("testUri1");
            builder1.setUri(new URI("testUri1"));
        } catch (URISyntaxException e) {
            fail("uri exception for test 1", e);
        }
        assertNotNull(uri1);

        HeadersMap headers1 = new HeadersMap();
        headers1.put("headerEntry1", List.of("headerEntry1Element1"));
        headers1.put("headerEntry2", List.of("headerEntry2Element1", "headerEntry2Element2"));
        assertNotNull(headers1);
        assertEquals(2, headers1.size());

        builder1.setRequestHeaders(headers1);

        HttpRequest request1 = builder1.build();
        assertEquals(HttpMethod.GET, request1.getHttpMethod());
        assertEquals(uri1, request1.getUri());
        assertEquals(headers1, request1.getRequestHeaders());
    }

    @Test void testHttpResponse() {
        // Test 2
        HttpResponse.Builder builder1 = new HttpResponse.Builder();
        HttpResponse response1 = builder1.buildBadRequest();

        assertNotNull(response1);
        assertEquals(400, response1.getStatusCode());

        //TODO add more tests

    }
}
