package base;

import functions.BaseUtils;
import functions.Helpers;
import functions.HttpFunctions;
import functions.RestFunctions;
import io.vavr.Lazy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.MessageSupplier;
import utils.TestUtils;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("specs")
class RestSpecsTest implements RestFunctions, Helpers, BaseUtils, HttpFunctions, TestUtils, MessageSupplier {

    @Tag("builder")
    @Test
    void specsBuilderWithTwoParametersTest(){

        var specs2 = new RestSpecsBuilder().with(
                $ -> {
                    $.baseUrl2 = googleBaseURL.get();
                    $.headersParams = headers.get();
                    $.bodyString = "";
                    $.requestMethod = "get";
                }).createSpecs();

        assertAll("Just BaseURL and Headers from Builder",
                () -> assertNotNull(specs2),
                () -> assertTrue(specs2.getURI().toString().equalsIgnoreCase(googleBaseURL.get())),
                () -> assertEquals(2, specs2.getHeaders().length));

    }

    @Tag("specs-from-file")
    @Test
    void specsConstructorTest(){

        var specs = specsFromFile.apply(configFile.get());


        assertAll("Load Specs from Config File",
                () -> assertNotNull(specs, specsNotNull.get()),
                () -> assertNotNull(specs.getBaseUrl(), baseURLNotNull.get()),
                () -> assertNotNull(specs.getRawEndpoint(), rawEndpointNotNull.get()),
                () -> assertNotNull(specs.getFormatedEndpoint(), formatedEndpointNotNull.get()),
                () -> assertNotNull(specs.getHeaders(), headersNotNull.get()),
                () -> assertNotNull(specs.getPathParams(), pathParametersNotNull.get()),
                () -> assertNotNull(specs.getQueryParams(), queryParametersNotNull.get()),
                () -> assertNotNull(specs.getBaseClient(), baseClientNotNull.get()),
                () -> assertNotNull(specs.getRawRequestMethod(), requestMethodNotNull.get()),
                () -> assertTrue(specs.getRawRequestMethod().equalsIgnoreCase( "PUT"), requestMethodNotNull.get()),
                () -> assertNotNull(specs.getURI(), uriNotNull.get())
        );

        System.out.println("Method: " +specs.getRequestMethod().toString());

    }


    @Tag("specs-simple")
    @Test
    void specsConstructorSimpleTest() {

        var specs = new RestSpecs(googleBaseURL.get(), headers.get(), "", "get");

        assertAll("Just BaseURL, Headers and empty body for Constructor",
                () -> assertNotNull(specs, specsNotNull.get()),
                () -> assertNotNull(specs.getBaseUrl(), baseURLNotNull.get()),
                () -> assertNotNull(specs.getHeaders(), headersNotNull.get()),
                () -> assertNotNull(specs.getBaseClient(), baseClientNotNull.get()),
                () -> assertNotNull(specs.getURI(), uriNotNull.get()));

    }

    @Tag("method-exception")
    @Test
    void specsConstructorExceptionTest() {

        var exceptionMsg = "you should specify a request method!"; //"baseURL cannot be empty!";

        var exception = assertThrows(AssertionError.class, () -> new RestSpecs(googleBaseURL.get(), headers.get(), "", ""));
        var exceptionFullConstructor = assertThrows(AssertionError.class, () -> new RestSpecs(
                googleBaseURL.get(), "", headers.get(), Map.of(), Map.of(), "", ""
        ));

        assertAll("Raising exception for request method empty String",
                () -> assertEquals(exceptionFullConstructor.getMessage(), exceptionMsg),
                () -> assertEquals(exception.getMessage(), exceptionMsg));

    }

    @Tag("baseURL-exception")
    @Test
    void specsBaseURLExceptionTest() {

        var exceptionMsg = "baseURL cannot be empty!";
        var specs = new RestSpecs(googleBaseURL.get(), headers.get(), "", "GET");
        var exception = assertThrows(AssertionError.class, () -> specs.setBaseUrl(""));

        assertAll("Raising exception for baseURL empty String",
                () -> assertEquals(exception.getMessage(), exceptionMsg));

    }

    @Tags({@Tag("specs")})
    @DisplayName(value = "Rest Specs Method")
    @ParameterizedTest(name = "Async sample Http test {index} with [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void syncTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {

        var specs = updateRestSpecs.apply(data,specsFromFile.apply(configSync.get()));

        var response = asyncRequest.apply(specs);

        assumeTrue(response.get().statusCode() >= data.getInteger(2), statusCode200.get());

        response.join().request();

        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);

        assertAll(
                () -> assertNotNull(response.get().body(), notNull.get()),
                () -> assertTrue(specs.getRawRequestMethod().equalsIgnoreCase(data.getString(1)), objectContentEquals.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }

}