package samples;

import base.RestSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.SyncFunctions;
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

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class SampleTest implements SyncFunctions, AsyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    Supplier<String> barURL = () -> "http://localhost";
    Supplier<Map<String, Object>> headers = () -> Map.of("Content-Type", "application/json");
    Supplier<String> configSync =  () -> "src/test/resources/sync-get.conf";

    @Test
    void asyncSampleTest() throws ExecutionException, InterruptedException {

        var specs = new RestSpecs(barURL.get().concat("/get"), headers.get(), "");
        var response = asyncRequestGET.apply(specs);

        assertAll( "Validação básica",
                () -> assertNotNull(response.get().body()),
                () -> assertEquals(200, response.get().statusCode())
        );

    }

    @Test
    void syncSampleTest() throws IOException {

        var specs = new RestSpecs(barURL.get().concat("/post"), headers.get(), "");
        var response = syncRequestPost.apply(specs);

        assertAll( "Validação básica",
                () -> assertNotNull(response.body()),
                () -> assertEquals(200, response.statusCode())
        );

        var body = new ObjectMapper().readValue(response.body().toString(), ResponseObject.class);

    }

    @Test
    void specsFromConfSampleTest(){

        //currying
        var response = specsFromFile.andThen(asyncRequestGET).apply(configSync.get());
        assertAll( "Validação básica",
                () -> assertNotNull(response.get().body()),
                () -> assertEquals(200, response.get().statusCode())
        );
    }

    @Tags({@Tag("parameterized"), @Tag("sample")})
    @DisplayName(value = "Async Http")
    @ParameterizedTest(name = "Exemplos de teste Http Assíncrono {index} com [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void paramaterizedSampleTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {

        AtomicReference<CompletableFuture<HttpResponse>> response = new AtomicReference<>();
        AtomicReference<ResponseObject> responseObject = new AtomicReference<>();

        assumingThat(data.getString(0).equalsIgnoreCase("/get"), () -> {

            response.lazySet(updateRestSpecs.andThen(asyncRequestGET).apply(data, specsFromFile.apply(configSync.get())));

            assumeTrue(response.get().get().statusCode() >= data.getInteger(1), statusCode200.get());
            responseObject.lazySet((ResponseObject) responseToClass.apply(response.get().get(), ResponseObject.class));

            response.get().join().request();

            assertAll(
                    () -> assertNotNull(response.get().get().body(), notNull.get())
                    ,
                    () -> assertEquals(barURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
            ); }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/post"), () -> {

                    response.lazySet(updateRestSpecs.andThen(asyncRequestPOST).apply(data, specsFromFile.apply(configSync.get())));

                    assumeTrue(response.get().get().statusCode() >= data.getInteger(1), statusCode200.get());
                    responseObject.lazySet((ResponseObject) responseToClass.apply(response.get().get(), ResponseObject.class));
                    assertAll(
                            () -> assertNotNull(response.get().get().body(), notNull.get()),
                            () -> assertEquals(barURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
                    );
                }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/put"), () -> {

                    response.lazySet(updateRestSpecs.andThen(asyncRequestPUT).apply(data, specsFromFile.apply(configSync.get())));

                    assumeTrue(response.get().get().statusCode() >= data.getInteger(1), statusCode200.get());
//                    var responseObject = (Body) responseToClass.apply(response.get().get(), Body.class);
                    responseObject.lazySet((ResponseObject) responseToClass.apply(response.get().get(), ResponseObject.class));
                    assertAll(
                            () -> assertNotNull(response.get().get().body(), notNull.get()),
                            () -> assertEquals(barURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
                    );
                }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/delete"), () -> {

                    response.lazySet(updateRestSpecs.andThen(asyncRequestDELETE).apply(data, specsFromFile.apply(configSync.get())));

                    assumeTrue(response.get().get().statusCode() >= data.getInteger(1), statusCode200.get());
                    responseObject.lazySet((ResponseObject) responseToClass.apply(response.get().get(), ResponseObject.class));
//                    var responseObject = (Body) responseToClass.apply(response.get().get(), Body.class);
                    assertAll(
                            () -> assertNotNull(response.get().get().body(), notNull.get()),
                            () -> assertEquals(barURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get()));
                }
        );

    }


}
