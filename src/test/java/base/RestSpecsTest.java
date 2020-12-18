package base;

import functions.BaseUtils;
import functions.Helpers;
import functions.HttpFunctions;
import functions.RestFunctions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertThrows;
import pojo.ResponseObject;
import utils.MessageSupplier;
import utils.TestUtils;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("specs")
class RestSpecsTest implements RestFunctions, Helpers, BaseUtils, HttpFunctions, TestUtils, MessageSupplier {

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
                () -> assertNotNull(specs.getRequestMethod(), requestMethodNotNull.get()),
                () -> assertTrue(specs.getRequestMethod().method().equalsIgnoreCase( "PUT"), requestMethodNotNull.get()),
                () -> assertNotNull(specs.getUri(), uriNotNull.get())
        );

        System.out.println("Method: " +specs.getRequestMethod().toString());

    }


    @Tag("specs-simple")
    @Test
    void specsConstructorSimpleTest() {

        //googleBaseURL.get(), headers.get(), "", "get"
        var specs = new RestSpecs()
                .baseURL(googleBaseURL.get())
                .headersParams(headers.get())
                .requestMethod("get")
                .build();

        assertAll("Just BaseURL, Headers and empty body for Constructor",
                () -> assertNotNull(specs, specsNotNull.get()),
                () -> assertNotNull(specs.getBaseUrl(), baseURLNotNull.get()),
                () -> assertNotNull(specs.getHeaders(), headersNotNull.get()),
                () -> assertNotNull(specs.getBaseClient(), baseClientNotNull.get()),
                () -> assertNotNull(specs.getUri(), uriNotNull.get()));

    }

    @Tag("method-exception")
    @Test
    void specsConstructorExceptionTest() {

        var exceptionMsg = "you should specify a request method!"; //"baseURL cannot be empty!";
        //new RestSpecs(googleBaseURL.get(), headers.get(), "", "")
        var exception = assertThrows(AssertionError.class, () -> new RestSpecs()
                .baseURL(googleBaseURL.get()).headersParams(headers.get()).body("").build());
        var exceptionFullConstructor = assertThrows(AssertionError.class, () -> new RestSpecs()
                .baseURL(googleBaseURL.get()).headersParams(headers.get()).pathParams(Map.of()).queryParams(Map.of()).body("").build());

        assertAll("Raising exception for request method empty String",
                () -> assertEquals(exceptionFullConstructor.getMessage(), exceptionMsg),
                () -> assertEquals(exception.getMessage(), exceptionMsg));

    }

    @Tag("baseURL-exception")
    @Test
    void specsBaseURLExceptionTest() {

        var exceptionMsg = "baseURL cannot be empty!";
//        var specs = new RestSpecs(googleBaseURL.get(), headers.get(), "", "GET");
        var specs = new RestSpecs().baseURL(googleBaseURL.get()).headersParams(headers.get()).requestMethod("GET");
        var exception = assertThrows(AssertionError.class, () -> specs.baseURL(""));

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

        response.get().request();

        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);

        assertAll(
                () -> assertNotNull(response.get().body(), notNull.get()),
                () -> assertTrue(specs.getRequestMethod().method().equalsIgnoreCase(data.getString(1)), objectContentEquals.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }

    @Test
    void testSpecss(){
        var baseUrl = "https://httpbin.org";
        var requestMethod = "GET";
        var endpoint = "/get/{cnpj}/{conta}";
        var end = "/get/";
        Map<String, Object> headers = Map.of("Content-Type", "application/x-www-form-urlencoded", "client_id", "CLIENTE_ID", "access_token", "QzBOdMOhQjFsLUMwcjMtQjRDay1UMGszbgo=");
        Map<String, Object> queryParams = Map.of("ano", 2019, "mesInicial", 1, "mesFinal", 12);
        Map<String, Object> pathParams = Map.of("cnpj","12312312312312", "conta", "2.01.01.01.10");

        var specs = new RestSpecs()
                .baseURL(baseUrl)
                .requestMethod(requestMethod)
                .endpoint(end)
//                .queryParams(queryParams)
//                .pathParams(pathParams)
                .headersParams(headers)
                .build();

    }

}