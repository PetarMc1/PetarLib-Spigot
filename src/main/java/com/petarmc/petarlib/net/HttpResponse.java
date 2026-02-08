package com.petarmc.petarlib.net;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP response with status code, body, and headers.
 */
public final class HttpResponse {

    /**
     * The HTTP status code of the response.
     */
    public final int statusCode;

    /**
     * The body of the response as a string.
     */
    public final String body;

    /**
     * The HTTP headers of the response.
     */
    public final Map<String, List<String>> headers;

    /**
     * Constructs a new HttpResponse.
     *
     * @param statusCode the HTTP status code
     * @param body       the response body, or empty string if null
     * @param headers    the response headers, or empty if null
     */
    public HttpResponse(int statusCode, String body, Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body == null ? "" : body;
        this.headers = headers == null ? Collections.emptyMap() : Collections.unmodifiableMap(headers);
    }

    /**
     * Returns true if the status code indicates a successful response (200-299).
     *
     * @return true if status code is 200–299, false otherwise
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Returns a string representation of the HttpResponse, including
     * status code and body length.
     *
     * @return string summary of the response
     */
    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", bodyLen=" + (body == null ? 0 : body.length()) +
                '}';
    }
}
