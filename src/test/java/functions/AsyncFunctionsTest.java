package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.MessageSupplier;
import utils.TestUtils;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("async")
class AsyncFunctionsTest implements AsyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    private Supplier<String> configSync =  () -> "src/test/resources/sync-get.conf";
    private Supplier<String> httpURL = () -> "https://httpbin.org";
//    private Supplier<String> httpURL = () -> "http://localhost:8080"; //o objeto de retorno no docker é diferente do Http


    @Tags({@Tag("sync")})
    @DisplayName(value = "Async Http")
    @ParameterizedTest(name = "Exemplos de teste Http Assíncrono {index} com [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void asyncTest(ArgumentsAccessor data) {

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
                    () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
            ); }
        );

        assumingThat(data.getString(0).equalsIgnoreCase("/post"), () -> {

                    response.lazySet(updateRestSpecs.andThen(asyncRequestPOST).apply(data, specsFromFile.apply(configSync.get())));

                    assumeTrue(response.get().get().statusCode() >= data.getInteger(1), statusCode200.get());
//                    var responseObject = (Body) responseToClass.apply(response.get().get(), Body.class);
                    responseObject.lazySet((ResponseObject) responseToClass.apply(response.get().get(), ResponseObject.class));
                    assertAll(
                            () -> assertNotNull(response.get().get().body(), notNull.get()),
                            () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
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
                            () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get())
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
                            () -> assertEquals(httpURL.get().concat(data.getString(0)), responseObject.get().getUrl(), objectEqual.get()));
                }
        );

    }
}
