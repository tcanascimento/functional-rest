package base;

import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.SyncFunctions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

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
                    $.bodyString = "";
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

        var response = asyncRequestGET.apply(specs);

        System.out.println("Status Code: "+ response.thenApply(HttpResponse::statusCode).get());
        System.out.println("Body: " + response.get().body());
        System.out.println("Headers: " + response.get().headers());

        System.out.println("\nHttpResponse: " + response.get());

    }


    @Disabled
    @Test
    void nada(){
//        var url = "http://localhost:8080/uat/sso/oauth/token?grant_type=password&username=superadmin&password=erebus";

        var url = "http://www.google.com";

        var specs2 = new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = url;
                    $.headersParams = headers.get();
                }).createSpecs();

        var response = syncRequestPost.apply(specs2);

        System.out.println(response.body());

    }

}