package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;
import pojo.ResponseObject;
import utils.HelperFunctions;
import utils.MessageSupplier;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("sync")
class SyncFunctionsTest extends HelperFunctions implements SyncFunctions, BaseUtils, MessageSupplier, TestUtils {

    @Tags({@Tag("sync")})
    @DisplayName(value = "Sync Http")
    @ParameterizedTest(name = "Sync sample Http test {index} with [{arguments}]")
    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
    void syncTest(ArgumentsAccessor data) {

        var method = data.getString(0);

        var response = updateRestSpecs.andThen(mapRequestMethodToSync.apply(method)).apply(data, specsFromFile.apply(configSync.get()));

        assumeTrue(response.statusCode() >= data.getInteger(1), statusCode200.get());

        var responseObject = (ResponseObject) responseToClass.apply(response, ResponseObject.class);

        assertAll(
                () -> assertNotNull(response.body(), notNull.get()),
                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));

    }

}