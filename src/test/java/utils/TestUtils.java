package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TestUtils {

    Supplier<String> configFile = () -> "src/test/resources/modelo.conf";

    BiPredicate<List<String>, List<String>> equalsList = (list, list2) ->
            list2.containsAll(list);

    BiPredicate<String, Map<String, Object>> containsOn = (str, objects) ->
            objects.values().stream().allMatch(o -> str.contains(String.valueOf(o)));

    BiPredicate<Map<String, Object>, Map<String, Object>> containsOnMap = (map1, map2) ->
            map2.values().containsAll(map1.values());

    Function<TestSourceTemplate,List<String>> setParams = (data) ->  Arrays.asList(data.getQueryParams().replaceAll("[^a-zA-Z0-9,]","").split(","));

}
