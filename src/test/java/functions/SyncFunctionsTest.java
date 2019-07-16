package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.Body;
import utils.MessageSupplier;
import utils.TestUtils;


import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("sync")
class SyncFunctionsTest implements SyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    private Supplier<String> config_sync =  () ->"src/test/resources/sync-get.conf";

    private Supplier<String> baseURL = () -> "https://httpbin.org/get";

    @Tags({@Tag("sync"), @Tag("get")})
    @DisplayName(value = "Sync Http")
    @ParameterizedTest(name = "Exemplos de teste Http Síncrono {index} com [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void syncGetTest(ArgumentsAccessor data) {

        var response = updateRestSpecs.andThen(syncRequestGET).apply(data, specsFromFile.apply(config_sync.get()));

        assumeTrue(response.statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (Body) responseToClass.apply(response, Body.class);

        assertAll(
                () -> assertNotNull(response.body(), notNull.get())/*,
                () -> assertEquals(baseURL.get(), responseObject.getUrl(), objectEqual.get())*/
        );

        System.out.println(response.statusCode());
    }

}