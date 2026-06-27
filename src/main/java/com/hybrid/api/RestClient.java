package com.hybrid.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Thin REST Assured wrapper — one instance per base URI.
 */
public class RestClient {

    private final RequestSpecification requestSpec;

    public RestClient(String baseUri) {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public Response get(String endpoint) {
        return given(requestSpec).get(endpoint);
    }

    public Response get(String endpoint, Object... pathParams) {
        return given(requestSpec).get(endpoint, pathParams);
    }

    public Response post(String endpoint, Object body) {
        return given(requestSpec).body(body).post(endpoint);
    }

    public Response put(String endpoint, Object body, Object... pathParams) {
        return given(requestSpec).body(body).put(endpoint, pathParams);
    }

    public Response patch(String endpoint, Object body, Object... pathParams) {
        return given(requestSpec).body(body).patch(endpoint, pathParams);
    }

    public Response delete(String endpoint, Object... pathParams) {
        return given(requestSpec).delete(endpoint, pathParams);
    }
}
