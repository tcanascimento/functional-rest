package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface HttpFunctions {

    Supplier<String> errorMessage = () -> "POJO's ERROR - verify it!";

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

    BiFunction<HttpResponse, Class, Object> responseToClass = (response, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(response.body().toString(), clazz)).getOrElse(errorMessage);


    BiFunction<String, Class, Object> responseBodyToClass = (body, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(body, clazz)).getOrElse(errorMessage);
}
