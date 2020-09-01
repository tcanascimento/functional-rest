package functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Lazy;
import io.vavr.control.Try;

import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public interface Helpers {

    Lazy<String> errorMessage = Lazy.of(() -> "ERROR on de-serialization- verify it!");

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
                    .getOrElse(errorMessage.get());

    BiFunction<String, Class, Object> stringToObject = (str, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(str, clazz))
                    .getOrElse(errorMessage.get());

    BiFunction<HttpResponse, Class, Object> responseToClass = (response, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(response.body().toString(), clazz)).getOrElse(errorMessage.get());


    BiFunction<String, Class, Object> responseBodyToClass = (body, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(body, clazz)).getOrElse(errorMessage.get());

    Function<String, String> getConfigPath = filename -> {
        var path = Paths.get("src", "test", "resources", filename, filename.concat(".conf"));
        return Files.exists(path) ? path.toString() : "no dir found for " + filename;
    };


}
