package samples;

import base.RestSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.RestFunctions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.HelperFunctions;
import utils.MessageSupplier;
import utils.TestUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class SampleTest implements RestFunctions, HelperFunctions, HttpFunctions, BaseUtils, MessageSupplier, TestUtils {

    private Supplier<Map<String, Object>> headers = () -> Map.of("Content-Type", "application/json");

    @Test
    void asyncSampleTest() {

        var specs = new RestSpecs(httpBinBaseURL.get().concat("/get"), headers.get(), "");
        var response = asyncRequest.apply(requestGET.apply(specs), specs);

        assertAll( "Validação básica",
                () -> assertNotNull(response.get().body()),
                () -> assertEquals(200, response.get().statusCode()));

    }

    @Test
    void syncSampleTest() throws IOException {

        var specs = new RestSpecs(httpBinBaseURL.get().concat("/post"), headers.get(), "");
        var response = syncRequest.apply(requestPOST.apply(specs), specs);

        assertAll( "Validação básica",
                () -> assertNotNull(response.body()),
                () -> assertEquals(200, response.statusCode()));

        var body = new ObjectMapper().readValue(response.body().toString(), ResponseObject.class);

    }

    @Test
    void specsFromConfSampleTest(){

        var specs = specsFromFile.apply(configSync.get());
        var response = asyncRequest.apply(requestGET.apply(specs), specs);
        assertAll( "Validação básica",
                () -> assertNotNull(response.get().body()),
                () -> assertEquals(200, response.get().statusCode()));
    }

    @Tags({@Tag("parameterized"), @Tag("sample")})
    @DisplayName(value = "Async Http")
    @ParameterizedTest(name = "Exemplos de teste Http Assíncrono {index} com [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void paramaterizedSampleTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {

        var method = data.getString(0);

        var specs = updateRestSpecs.apply(data,specsFromFile.apply(configSync.get()));

        var response = asyncRequestFunction.apply(mapRequestMethod.apply(method), specs);

        assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);

        response.join().request();

        assertAll("Testing Methods",
                () -> assertNotNull(response.get().body(), notNull.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }

}
