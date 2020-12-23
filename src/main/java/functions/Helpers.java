package functions;

import base.RestSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Lazy;
import io.vavr.control.Try;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


public interface Helpers {

    Lazy<String> errorMessage = Lazy.of(() -> "ERROR on de-serialization- verify it!");

    BiFunction<Map<String,Object>, RestSpecs, RestSpecs> updatePathParams = (pathParams, specs) ->
            new RestSpecs()
                    .baseURL(specs.getBaseUrl().toString())
                    .endpoint(specs.getRawEndpoint())
                    .headersParams(specs.getHeadersMap())
                    .pathParams(pathParams)
                    .queryParams(specs.getQueryParams())
                    .body(specs.getBody())
                    .baseClient(specs.getBaseClient())
                    .requestMethod(specs.getRequestMethod().method())
                    .build();


    BiFunction<Map<String,Object>, RestSpecs, RestSpecs> updateQueryParams = (queryParams, specs) ->
            new RestSpecs()
                    .baseURL(specs.getBaseUrl().toString())
                    .endpoint(specs.getRawEndpoint())
                    .headersParams(specs.getHeadersMap())
                    .pathParams(specs.getPathParams())
                    .queryParams(queryParams)
                    .body(specs.getBody())
                    .baseClient(specs.getBaseClient())
                    .requestMethod(specs.getRequestMethod().method())
                    .build();

    BiFunction<String, RestSpecs, RestSpecs> updateEndpoint = (endpoint, specs) ->
            new RestSpecs()
                    .baseURL(specs.getBaseUrl().toString())
                    .endpoint(endpoint)
                    .headersParams(specs.getHeadersMap())
                    .pathParams(specs.getPathParams())
                    .queryParams(specs.getQueryParams())
                    .body(specs.getBody())
                    .baseClient(specs.getBaseClient())
                    .requestMethod(specs.getRequestMethod().method())
                    .build();

    BiFunction<String, RestSpecs, RestSpecs> updateBody = (body, specs) ->
            new RestSpecs()
                    .baseURL(specs.getBaseUrl().toString())
                    .endpoint(specs.getRawEndpoint())
                    .headersParams(specs.getHeadersMap())
                    .pathParams(specs.getPathParams())
                    .queryParams(specs.getQueryParams())
                    .body(body)
                    .baseClient(specs.getBaseClient())
                    .requestMethod(specs.getRequestMethod().method())
                    .build();


    Function<Object, String> clazzToJson = object ->
            Try.of(() -> new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object))
                    .getOrElse(errorMessage.get());

    BiFunction<String, Class, Object> stringToObject = (str, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(str, clazz))
                    .getOrElse(errorMessage.get());

    BiFunction<HttpResponse, Class, Object> responseToClass = (response, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(response.body().toString(), clazz)).getOrElse(errorMessage.get());

    @Deprecated
    BiFunction<String, Class, Object> responseBodyToClass = (body, clazz) ->
            Try.of(() -> new ObjectMapper().readValue(body, clazz)).getOrElse(errorMessage.get());

    Function<String, String> getConfigPath = filename -> {
        var path = Paths.get("src", "test", "resources", filename, filename.concat(".conf"));
        return Files.exists(path) ? path.toString() : "no dir found for " + filename;
    };

    Function<Map<String, Object>, String> mapToFormParameter = map ->
            map.entrySet()
                    .stream()
                    .map(i ->  new StringBuilder(URLEncoder.encode(i.getKey(), StandardCharsets.UTF_8)
                            .concat("=")
                            .concat(URLEncoder.encode(i.getValue().toString(), StandardCharsets.UTF_8))))
                    .reduce((k,v) -> k.append("&").append(v))
                    .stream().collect(Collectors.joining());

}
