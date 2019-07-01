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

import static org.junit.jupiter.api.Assertions.*;

@Tag("specs")
class RestSpecsTest implements AsyncFunctions, BaseUtils, HttpFunctions, SyncFunctions, TestUtils {


    @Test
    void validaSpecs(){

        final String baseURL = "http://www.google.com";
        final Map<String, Object> headers = Map.of("Content-Type", "application/json");
        var specs2 = new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = baseURL;
                    $.headersParams = headers;
                }).createSpecs();

        assertAll("apenas baseURL e headers",
                () -> assertNotNull(specs2),
                () -> assertTrue(specs2.getURI().toString().equalsIgnoreCase(baseURL)),
                () -> assertEquals(2, specs2.getHeaders().length));

        var response = syncRequestGET.apply(specs2);

        System.out.println("status code: " + response.statusCode() + "\nbody: " + response.body() + "\nheaders: " + response.headers());
    }
}