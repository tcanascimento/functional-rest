package base;

import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.SyncFunctions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.util.Map;
import java.util.function.Supplier;

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

//        var response = syncRequestGET.apply(specs2);

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
    void specsConstructorSimpleTest(){

        var specs = new RestSpecs(baseURL.get(), headers.get(), "");

        assertAll("Just BaseURL, Headers and empty body for Constructor",
                () -> assertNotNull(specs, "Specs cannot be null!"),
                () -> assertNotNull(specs.getBaseUrl(), "BaseURL cannot be null!"),
                () -> assertNotNull(specs.getHeaders(), "Headers cannot be null!"),
                () -> assertNotNull(specs.getBaseClient(), "BaseClient cannot be null!"),
                () -> assertNotNull(specs.getURI(), "URI cannot be null!")
        );
    }
}