package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.HelperFunctions;
import utils.MessageSupplier;
import utils.TestUtils;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("async")
class FunctionsTest implements HelperFunctions, RestFunctions, BaseUtils, MessageSupplier, TestUtils {

    @Tags({@Tag("sync")})
    @DisplayName(value = "Async Http")
    @ParameterizedTest(name = "Async sample Http test {index} with [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void asyncTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {

        var method = data.getString(0);

        var specs = updateRestSpecs.apply(data,specsFromFile.apply(configSync.get()));

        var response = asyncRequest.apply(mapRequestMethod.apply(method).apply(specs), specs);

        assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);

        response.join().request();

        assertAll(
                () -> assertNotNull(response.get().body(), notNull.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }

    @Tags({@Tag("sync")})
    @DisplayName(value = "Sync Http")
    @ParameterizedTest(name = "Sync sample Http test {index} with [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void syncTest(ArgumentsAccessor data) {

        var method = data.getString(0);

        var specs = specsFromFile.apply(configSync.get());

        var response = syncRequest.apply(mapRequestMethod.apply(method).apply(specs), specs);

        assumeTrue(response.statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (ResponseObject) responseToClass.apply(response, ResponseObject.class);

        assertAll(
                () -> assertNotNull(response.body(), notNull.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }
}
