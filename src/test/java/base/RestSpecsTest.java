package base;

import cyclops.control.Either;
import cyclops.control.Option;
import cyclops.control.Try;
import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.SyncFunctions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Tag("specs")
class RestSpecsTest implements AsyncFunctions, BaseUtils, HttpFunctions, SyncFunctions, TestUtils {

    @Tag("builder")
    @Test
    void specsBuilderWithTwoParametersTest(){

        var specs2 = new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = baseURL.get();
                    $.headersParams = headers.get();
                }).createSpecs();

        assertAll("Just BaseURL and Headers from Builder",
                () -> assertNotNull(specs2),
                () -> assertTrue(specs2.getURI().toString().equalsIgnoreCase(baseURL.get())),
                () -> assertEquals(2, specs2.getHeaders().length));

    }

    @Tag("specs-from-file")
    @Test
    void specsConstructorTest(){

        var specs = specsFromFile.apply(configFile.get());

        assertAll("Load Specs from Config File",
                () -> assertNotNull(specs, "Specs cannot be null!"),
                () -> assertNotNull(specs.getBaseUrl(), "BaseURL cannot be null!"),
                () -> assertNotNull(specs.getRawEndpoint(), "Raw endpoint cannot be null!"),
                () -> assertNotNull(specs.getFormatedEndpoint(), "Formated endpoint cannot be null"),
                () -> assertNotNull(specs.getHeaders(), "Headers cannot be null!"),
                () -> assertNotNull(specs.getPathParams(), "PathParameters cannot be null!"),
                () -> assertNotNull(specs.getQueryParams(), "QueryParameters cannot be null!"),
                () -> assertNotNull(specs.getBaseClient(), "BaseClient cannot be null!"),
                () -> assertNotNull(specs.getURI(), "URI cannot be null!")
        );

    }


    @Tag("specs-simple")
    @Test
    void specsConstructorSimpleTest() throws ExecutionException, InterruptedException {

        var specs = new RestSpecs(baseURL.get(), headers.get(), "");

        assertAll("Just BaseURL, Headers and empty body for Constructor",
                () -> assertNotNull(specs, "Specs cannot be null!"),
                () -> assertNotNull(specs.getBaseUrl(), "BaseURL cannot be null!"),
                () -> assertNotNull(specs.getHeaders(), "Headers cannot be null!"),
                () -> assertNotNull(specs.getBaseClient(), "BaseClient cannot be null!"),
                () -> assertNotNull(specs.getURI(), "URI cannot be null!")
        );

//        System.out.println(asyncRequestGET.apply(specs).body());

//        var response = syncRequestGET.apply(specs).get();
        /*var body = response.peek(HttpResponse::body);
        Option<Object> status = response.map(HttpResponse::statusCode);*

        /*System.out.println(body);
        System.out.println(status);*/

        var response = algo.apply(specs);
        System.out.println(response.thenApply(HttpResponse::body).get());
    }

    /*Function<RestSpecs, Either<Exception, HttpResponse>> syncRequestGET = specs ->
            cyclops.control.Try.withCatch(
                    () -> specs.getBaseClient().send(requestGET.apply(specs),
                            specs.getResponseBodyHandler()), IOException.class
            ).toEither();*/

    Function<RestSpecs, CompletableFuture<HttpResponse>> algo = specs ->
            specs.getBaseClient().sendAsync(requestGET.apply(specs), specs.getResponseBodyHandler());

}