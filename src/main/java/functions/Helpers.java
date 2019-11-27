package functions;

import base.RestSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public interface Helpers {

    Supplier<String> errorMessage = () -> "POJO's ERROR - verify it!";

    BiFunction<Map<String,Object>, RestSpecs, RestSpecs> updatePathParams = (pathParams, specs) ->
            new RestSpecs(specs.getBaseUrl().toString(),
                    specs.getRawEndpoint(),
                    specs.getHeadersMap(),
                    specs.getQueryParams(),
                    pathParams, specs.getBody().toString(), specs.getRawRequestMethod());

    BiFunction<Map<String,Object>, RestSpecs, RestSpecs> updateQueryParams = (queryParams, specs) ->
            new RestSpecs(specs.getBaseUrl().toString(),
                    specs.getRawEndpoint(),
                    specs.getHeadersMap(),
                    queryParams,
                    specs.getPathParams(), specs.getBody().toString(), specs.getRawRequestMethod());

    BiFunction<String, RestSpecs, RestSpecs> updateEndpoint = (endpoint, specs) ->
            new RestSpecs(specs.getBaseUrl().toString(), endpoint, specs.getHeadersMap(), specs.getQueryParams(), specs.getPathParams(), specs.getBody().toString(), specs.getRawRequestMethod());

    BiFunction<String, RestSpecs, RestSpecs> updateBody = (body, specs) ->
            new RestSpecs(specs.getBaseUrl().toString(), specs.getRawEndpoint(), specs.getHeadersMap(), specs.getQueryParams(), specs.getPathParams(), body, specs.getRawRequestMethod());


    Function<Object, String> clazzToJson = object ->
            Try.of(() -> new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object))
                    .get();

    BiFunction<String, Class, Object> stringToObject = (lancamento, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(lancamento, clazz))
                    .get();

    BiFunction<HttpResponse, Class, Object> responseToClass = (response, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(response.body().toString(), clazz)).getOrElse(errorMessage);


    BiFunction<String, Class, Object> responseBodyToClass = (body, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(body, clazz)).getOrElse(errorMessage);

}
