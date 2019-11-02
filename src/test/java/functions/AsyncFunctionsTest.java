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
class AsyncFunctionsTest extends HelperFunctions implements AsyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    @Tags({@Tag("sync")})
    @DisplayName(value = "Async Http")
    @ParameterizedTest(name = "Async sample Http test {index} with [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void asyncTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {

        var method = data.getString(0);

        var response = updateRestSpecs.andThen(mapRequestMethodToAsync.apply(method)).apply(data, specsFromFile.apply(configSync.get()));

        assumeTrue(response.get().statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);

        response.join().request();

        assertAll(
                () -> assertNotNull(response.get().body(), notNull.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectEqual.get()));

    }
}
