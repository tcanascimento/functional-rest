package utils;

import io.vavr.Lazy;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;


public interface TestUtils {

    Lazy<String> configSync =  Lazy.of(() -> "src/test/resources/sync-get.conf");

    Lazy<String> configFile = Lazy.of(() -> "src/test/resources/modelo.conf");

    Lazy<String> httpBinBaseURL = Lazy.of(() -> "https://httpbin.org");

    Lazy<String> googleBaseURL = Lazy.of(() -> "http://www.google.com");

    Lazy<Map<String,Object>> headers = Lazy.of(() -> Map.of("Content-Type", "application/json"));

    BiPredicate<List<String>, List<String>> equalsList = (list, list2) ->
            list2.containsAll(list);

    BiPredicate<String, Map<String, Object>> containsOn = (str, objects) ->
            objects.values().stream().allMatch(o -> str.contains(String.valueOf(o)));

    BiPredicate<Map<String, Object>, Map<String, Object>> containsOnMap = (map1, map2) ->
            map2.values().containsAll(map1.values());

    Function<TestSourceTemplate,List<String>> setParams = (data) ->  Arrays.asList(data.getQueryParams().replaceAll("[^a-zA-Z0-9,]","").split(","));

    BiFunction<ArgumentsAccessor, RestSpecs, RestSpecs> updateRestSpecs = (data, specs) ->
        new RestSpecs(specs.getBaseUrl().toString(), data.getString(0), specs.getHeadersMap(), specs.getQueryParams(), specs.getPathParams(),"", data.getString(1));

}
