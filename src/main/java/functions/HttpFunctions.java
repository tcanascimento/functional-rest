package functions;

/**
 * author: Thiago Carreira
 *   license: Apache2
 */

import base.RestSpecs;
import io.vavr.API;

import java.net.http.HttpRequest;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;

@Deprecated(forRemoval = true)
public interface HttpFunctions {


    Function<RestSpecs, HttpRequest> requestGET = specs ->
            HttpRequest
                    .newBuilder()
                    .uri(specs.getURI())
                    .timeout(specs.getTimeout())
                    .headers(specs.getHeaders())
                    .GET().build();

    Function<RestSpecs, HttpRequest> requestDELETE = specs ->
            HttpRequest
                    .newBuilder()
                    .uri(specs.getURI())
                    .timeout(specs.getTimeout())
                    .headers(specs.getHeaders())
                    .DELETE().build();

    Function<RestSpecs, HttpRequest> requestPOST = specs ->
            HttpRequest
                    .newBuilder()
                    .uri(specs.getURI())
                    .timeout(specs.getTimeout())
                    .headers(specs.getHeaders())
                    .POST(specs.getBody()).build();

    Function<RestSpecs, HttpRequest> requestPUT = specs ->
            HttpRequest
                    .newBuilder()
                    .uri(specs.getURI())
                    .timeout(specs.getTimeout())
                    .headers(specs.getHeaders())
                    .PUT(specs.getBody()).build();


    Function<String, Function<RestSpecs, HttpRequest>> getRequestFunction = requestMethod ->
            API.Match(requestMethod.toUpperCase()).of(
                    Case($("GET"), requestGET),
                    Case($("POST"), requestPOST),
                    Case($("PUT"), requestPUT),
                    Case($("DELETE"), requestDELETE));

}
