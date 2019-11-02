package base;

import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.SyncFunctions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.MessageSupplier;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

@Tag("specs")
class RestSpecsTest implements AsyncFunctions, BaseUtils, HttpFunctions, SyncFunctions, TestUtils, MessageSupplier {

    @Tag("builder")
    @Test
    void specsBuilderWithTwoParametersTest(){

        var specs2 = new RestSpecsBuilder().with(
                $ -> {
                    $.baseUrl2 = googleBaseURL.get();
                    $.headersParams = headers.get();
                    $.bodyString = "";
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
                () -> assertNotNull(specs.getURI(), uriNotNull.get())
        );

    }


    @Tag("specs-simple")
    @Test
    void specsConstructorSimpleTest() {

        var specs = new RestSpecs(googleBaseURL.get(), headers.get(), "");

        assertAll("Just BaseURL, Headers and empty body for Constructor",
                () -> assertNotNull(specs, specsNotNull.get()),
                () -> assertNotNull(specs.getBaseUrl(), baseURLNotNull.get()),
                () -> assertNotNull(specs.getHeaders(), headersNotNull.get()),
                () -> assertNotNull(specs.getBaseClient(), baseClientNotNull.get()),
                () -> assertNotNull(specs.getURI(), uriNotNull.get()));

        /*var response = asyncRequestGET.apply(specs);

        System.out.println("Status Code: "+ response.thenApply(HttpResponse::statusCode).get());
        System.out.println("Body: " + response.get().body());
        System.out.println("Headers: " + response.get().headers());

        System.out.println("\nHttpResponse: " + response.get());*/

    }

}