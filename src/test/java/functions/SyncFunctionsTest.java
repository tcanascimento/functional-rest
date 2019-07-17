package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.MessageSupplier;
import utils.TestUtils;

import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("sync")
class SyncFunctionsTest implements SyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    private Supplier<String> config_sync =  () -> "src/test/resources/sync-get.conf";

//    private Supplier<String> httpURL = () -> "https://httpbin.org";
    private Supplier<String> httpURL = () -> "http://localhost:8080";

    @Tags({@Tag("sync")})
    @DisplayName(value = "Sync Http")
    @ParameterizedTest(name = "Exemplos de teste Http Síncrono {index} com [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void syncTest(ArgumentsAccessor data) {

        AtomicReference<HttpResponse> response = new AtomicReference<>();

        assumingThat(data.getString(0).equalsIgnoreCase("/get"), () -> {

            response.set(updateRestSpecs.andThen(syncRequestGET).apply(data, specsFromFile.apply(config_sync.get())));

                    assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());
                    var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);
                    assertAll(
                            () -> assertNotNull(response.get().body(), notNull.get()),
                            () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.getUrl(), objectEqual.get())
                    ); }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/post"), () -> {

            response.set(updateRestSpecs.andThen(syncRequestPost).apply(data, specsFromFile.apply(config_sync.get())));

            assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());
            var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);
            assertAll(
                    () -> assertNotNull(response.get().body(), notNull.get()),
                    () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.getUrl(), objectEqual.get())
            );
        }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/put"), () -> {

            response.set(updateRestSpecs.andThen(syncRequestPUT).apply(data, specsFromFile.apply(config_sync.get())));

            assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());
            var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);
            assertAll(
                    () -> assertNotNull(response.get().body(), notNull.get()),
                    () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.getUrl(), objectEqual.get())
            );
        }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/delete"), () -> {

            response.set(updateRestSpecs.andThen(syncRequestDELETE).apply(data, specsFromFile.apply(config_sync.get())));

            assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());
            var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);
            assertAll(
                    () -> assertNotNull(response.get().body(), notNull.get()),
                    () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.getUrl(), objectEqual.get()));
                }
        );
    }

}