//package samples;
//
//import base.BaseRest;
//import base.RestSpecs;
//import base.RestSpecsBuilder;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigBeanFactory;
//import com.typesafe.config.ConfigFactory;
//import functions.BaseUtils;
//import functions.Helpers;
//import functions.HttpFunctions;
//import functions.RestFunctions;
//import io.vavr.Lazy;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import pojo.ResponseObject;
//import utils.MessageSupplier;
//import utils.TestUtils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Properties;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assumptions.assumeTrue;
//
//class SampleTest implements RestFunctions, Helpers, HttpFunctions, BaseUtils, MessageSupplier, TestUtils {
//
//    private Lazy<Map<String, Object>> headers = Lazy.of(() -> Map.of("Content-Type", "application/json"));
//
//    @Test
//    void asyncSampleTest() {
//
//        var specs = new RestSpecs(httpBinBaseURL.get().concat("/get"), headers.get(), "", "get");
//        var response = asyncRequest.apply(specs);
//
//        assertAll( "Simple validation",
//                () -> assertNotNull(response.get().body()),
//                () -> assertEquals(200, response.get().statusCode()));
//
//    }
//
//    @Test
//    void syncSampleTest() throws IOException {
//
//        var specs = new RestSpecs(httpBinBaseURL.get().concat("/post"), headers.get(), "", "post");
//        var response = syncRequest.apply(specs).get();
//        var body = new ObjectMapper().readValue(response.body().toString(), ResponseObject.class);
//
//        assertAll( "Simple validation",
//                () -> assertNotNull(response.body()),
//                () -> assertEquals(200, response.statusCode()),
//                () -> assertEquals(specs.getBaseUrl().toString(), body.getUrl()));
//
//
//
//        System.out.println("body: "+ body);
//
//    }
//
//    @Tag("specsfromfile")
//    @Test
//    void specsFromConfSampleTest(){
//
//        var specs = specsFromFile.apply(configSync.get());
//        var response = asyncRequest.apply(specs);
//        assertAll( "Simple validation",
//                () -> assertNotNull(response.get().body()),
//                () -> assertEquals(200, response.get().statusCode()));
//    }
//
//    @Tags({@Tag("parameterized"), @Tag("sample")})
//    @DisplayName(value = "Async Http")
//    @ParameterizedTest(name = "Exemplos de teste Http Assíncrono {index} com [{arguments}]")
//    @CsvFileSource(resources = "/sync-data.csv", numLinesToSkip = 1)
//    void paramaterizedSampleTest(ArgumentsAccessor data) throws ExecutionException, InterruptedException {
//
//        var method = data.getString(0);
//
//        var specs = updateRestSpecs.apply(data,specsFromFile.apply(configSync.get()));
//
//        var response = asyncRequest.apply(specs);
//
//        assumeTrue(response.get().statusCode() >= data.getInteger(2), statusCode200.get());
//
//        var responseObject = (ResponseObject) responseToClass.apply(response.get(), ResponseObject.class);
//
//        response.join().request();
//
//        assertAll("Testing Methods",
//                () -> assertNotNull(response.get().body(), notNull.get()),
//                () -> assertEquals(httpBinBaseURL.get().concat(data.getString(0)), responseObject.getUrl(), objectContentEquals.get()));
//
//    }
//
//    protected static String getProp() {
//        Properties props = new Properties();
//        try {
//            props.load(new FileInputStream("src/test/resources/configs.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return props.getProperty("csv");
//    }
//
//    private static final String ALGO = getProp();
//
//    @Test
//    @Tag("props")
//    void propsTest(){
//        //System.out.println(ALGO);
//
//        var rate = 1.50;
//        var amount = 100.00;
//        var value = 98.50;
//
//        assertEquals(0, Double.compare(Math.abs(value) + rate, amount));
//    }
//
//
//    /*
//    {
//        Config config = ConfigFactory.parseFile(new File(file)).resolve();
//        return new RestSpecsBuilder().with(
//                ($) -> {
//                    $.baseUrl2 = config.getConfig("specs").getString("baseUrl");
//                    $.endpoint2 = config.getConfig("specs").getString("endpoint");
//                    $.headersParams = config.getConfig("specs").getObject("headers").unwrapped();
//                    $.queryParams = config.getConfig("specs").getObject("queryParams").unwrapped();
//                    $.pathParams = config.getConfig("specs").getObject("pathParams").unwrapped();
//                    $.bodyString = config.getConfig("specs").getString("body");
//                    $.requestMethod = config.getConfig("specs").getString("requestMethod");
//                }).createSpecs();
//    };
//     */
////    Function<String, Object> specsToClass = file -> ConfigBeanFactory.create(
////            ConfigFactory.parseFile(new File(file)).resolve().getConfig("specs"),
////            BaseRest.class);
//
//
//}
