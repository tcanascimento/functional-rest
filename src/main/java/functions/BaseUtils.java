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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface BaseUtils {

    /**
     * Funciona bem para objetos POJO -Não foi configurado para funcionar com RestSpecs.
     */
    BiFunction<String, Class, Object> specsConfigLoaderFromFile = (file, clazz) ->
            ConfigBeanFactory.create(
                    ConfigFactory.parseFile(new File(file)).resolve().getConfig("specs"),
                    clazz);

    Function<String, RestSpecs> specsFromFile = (file) -> {
        Config config = ConfigFactory.parseFile(new File(file)).resolve();
        return new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = config.getConfig("specs").getString("baseUrl");
                    $.endpoint2 = config.getConfig("specs").getString("endpoint");
                    $.headersParams = config.getConfig("specs").getObject("headers").unwrapped();
                    $.queryParams = config.getConfig("specs").getObject("queryParams").unwrapped();
                    $.pathParams = config.getConfig("specs").getObject("pathParams").unwrapped();
                    $.bodyString = config.getConfig("specs").getString("body");
                }
        ).createSpecs();
    };


    //todo: shame on you!
    BiFunction<String,Map<String,Object>, String> setPathParameters = (endpoint, pars) -> {
        AtomicReference<String> temp = new AtomicReference<>(endpoint);
        pars.keySet().forEach(
                k -> temp.lazySet(temp.get().replaceFirst("\\{".concat(k).concat("\\}"), pars.get(k).toString())));
        return temp.get();
    };


    //todo: terrible man!
    Function<Map<String,Object>, String[]> mapToStringArray = map -> {

        Iterator<Map.Entry<String,Object>> iter = map.entrySet().iterator();

        if(!iter.hasNext()) return new String[0];
        List<String> composition = new ArrayList<>();
        //isso poderia fazer com que um array ímpar fosse validado, porém, a interface Map do Java não permite criar Map com tamanho ímpar
        String[] lista = new String[(map.size() * 2)];

        while (iter.hasNext()){
            Map.Entry<String,Object> temp = iter.next();
            String key = temp.getKey();
            String value = String.valueOf(temp.getValue());
            composition.add(key);
//            composition.add(",");
            composition.add(value);
//            if(iter.hasNext()) composition.add(",");
        }

        for(int i = 0; i < composition.size(); i++){
            lista[i] = composition.get(i);
        }

        return lista;
    };

    /**
     *
     * @param url
     * @param queryParams
     * @return an String with Query Parameters. Note: the order is not always guaranteed
     */
    BiFunction<String, Map<String,Object>, String> queryParametersComposition = (url, queryParams) -> {
        Iterator<Map.Entry<String,Object>> iter = queryParams.entrySet().iterator();

        if(!iter.hasNext()) return url;
        StringBuilder composition = new StringBuilder();
        composition.append(url).append("?");

        while (iter.hasNext()){
            Map.Entry<String,Object> temp = iter.next();
            String key = temp.getKey();
            String value = String.valueOf(temp.getValue());
            composition.append(key).append("=").append(value);
            if(iter.hasNext()) composition.append("&");
        }

        return composition.toString();
    };

    /**
     * Função para gerar Map<String,Object> a partir de uma representação de json em String
     */
    Function<String, Map<String, Object>> generateMapFromString = dataVar ->
            Arrays.stream(dataVar.replace("{", "").replace("}", "").split(","))
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(k -> k[0].strip(), k -> k[1].strip()));

}
