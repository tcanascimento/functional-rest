package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import base.RestSpecsBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface BaseUtils {

    /**
     * Works good for POJO; it was not intented to work for RestSpecs
     */
    BiFunction<String, Class, Object> specsConfigLoaderFromFile = (file, clazz) ->
            ConfigBeanFactory.create(
                    ConfigFactory.parseFile(new File(file)).resolve().getConfig("specs"),
                    clazz);

    Function<String, RestSpecs> specsFromFile = file -> {
        Config config = ConfigFactory.parseFile(new File(file)).resolve();
        return new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = config.getConfig("specs").getString("baseUrl");
                    $.endpoint2 = config.getConfig("specs").getString("endpoint");
                    $.headersParams = config.getConfig("specs").getObject("headers").unwrapped();
                    $.queryParams = config.getConfig("specs").getObject("queryParams").unwrapped();
                    $.pathParams = config.getConfig("specs").getObject("pathParams").unwrapped();
                    $.bodyString = config.getConfig("specs").getString("body");
                }).createSpecs();
    };

    BiFunction<String,Map<String,Object>, String> setPathParameters = (endpoint, pars) -> {
        AtomicReference<String> pathParameter = new AtomicReference<>(endpoint);
        pars.keySet().forEach(
                k -> pathParameter.lazySet(pathParameter.get().replaceFirst("\\{".concat(k).concat("\\}"), pars.get(k).toString())));
        return pathParameter.get();
    };


    Function<Map<String,Object>, String[]> mapToStringArray = map -> {

        AtomicReference<List<String>> composition = new AtomicReference<>(new ArrayList<>());

        map.entrySet().iterator().forEachRemaining(i -> {
            composition.get().add(i.getKey());
            composition.get().add(String.valueOf(i.getValue()));
        });

        return composition.get().toArray(new String[0]);
    };

    /**
     *
     * @param url
     * @param queryParams
     * @return a String with Query Parameters. Note: the order is not always guaranteed
     */
    BiFunction<String, Map<String,Object>, String> queryParametersComposition = (url, queryParams) -> {

        AtomicReference<StringBuilder> composition = new AtomicReference<>(new StringBuilder(url.concat("?")));
        Iterator<Map.Entry<String,Object>> iter = queryParams.entrySet().iterator();

        iter.forEachRemaining(i -> {
            composition.get().append(i.getKey()).append("=").append(i.getValue());
            if(iter.hasNext()) composition.get().append("&");
        });

        return composition.get().toString();
    };

    /**
     * Função para gerar Map<String,Object> a partir de uma representação de json em String
     */
    Function<String, Map<String, Object>> generateMapFromString = dataVar ->
            Arrays.stream(dataVar.replace("{", "").replace("}", "").split(","))
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(k -> k[0].strip(), k -> k[1].strip()));

}
