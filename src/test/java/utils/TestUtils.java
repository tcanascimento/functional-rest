package utils;

import base.RestSpecs;
import functions.AsyncFunctions;
import io.vavr.API;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.vavr.API.$;
import static io.vavr.API.Case;

public interface TestUtils {

    Supplier<String> configSync =  () -> "src/test/resources/sync-get.conf";

    Supplier<String> configFile = () -> "src/test/resources/modelo.conf";

    Supplier<String> httpBinBaseURL = () -> "https://httpbin.org";

    Supplier<String> googleBaseURL = () -> "http://www.google.com";

    Supplier<Map<String,Object>> headers = () -> Map.of("Content-Type", "application/json");

    BiPredicate<List<String>, List<String>> equalsList = (list, list2) ->
            list2.containsAll(list);

    BiPredicate<String, Map<String, Object>> containsOn = (str, objects) ->
            objects.values().stream().allMatch(o -> str.contains(String.valueOf(o)));

    BiPredicate<Map<String, Object>, Map<String, Object>> containsOnMap = (map1, map2) ->
            map2.values().containsAll(map1.values());

    Function<TestSourceTemplate,List<String>> setParams = (data) ->  Arrays.asList(data.getQueryParams().replaceAll("[^a-zA-Z0-9,]","").split(","));

    BiFunction<ArgumentsAccessor, RestSpecs, RestSpecs> updateRestSpecs = (data, specs) ->
        new RestSpecs(specs.getBaseUrl().toString(), data.getString(0), specs.getHeadersMap(), specs.getQueryParams(), specs.getPathParams(),"");

}
