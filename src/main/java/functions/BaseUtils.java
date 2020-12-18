package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import io.vavr.Function3;
import io.vavr.control.Try;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface BaseUtils {

    /**
     * Works good for POJO; it was not intented to work for RestSpecs
     */
    @Deprecated
    BiFunction<String, Class, Object> specsConfigLoaderFromFile = (file, clazz) ->
            ConfigBeanFactory.create(
                    ConfigFactory.parseFile(new File(file)).resolve().getConfig("specs"),
                    clazz);

    Function<String, RestSpecs> specsFromFile = file -> {
        Config config = ConfigFactory.parseFile(new File(file)).resolve();

        return new RestSpecs()
                .baseURL(config.getConfig("specs").getString("baseUrl"))
                .endpoint(config.getConfig("specs").getString("endpoint"))
                .headersParams(config.getConfig("specs").getObject("headers").unwrapped())
                .queryParams(config.getConfig("specs").getObject("queryParams").unwrapped())
                .pathParams(config.getConfig("specs").getObject("pathParams").unwrapped())
                .body(config.getConfig("specs").getString("body"))
                .requestMethod(config.getConfig("specs").getString("requestMethod"))
                .build();
    };

    /*
    Function to update RestSpecs for HttpClient within SSL for https requests
     */
    Function3<RestSpecs, Supplier<TrustManager[]>, Function<TrustManager[], SSLContext>, RestSpecs> updateClientSpecs =
            (specs, supplier, ssl) ->
                new RestSpecs().baseURL(specs.getBaseUrl().toString())
                .endpoint(specs.getRawEndpoint())
                .headersParams(specs.getHeadersMap())
                .queryParams(specs.getQueryParams())
                .pathParams(specs.getPathParams())
                .body(specs.getBody())
                .requestMethod(specs.getRequestMethod().method())
                .baseClient(HttpClient.newBuilder().sslContext(ssl.apply(supplier.get())).build())
                .build();

    //todo: refactor
    BiFunction<String,Map<String,Object>, String> setPathParameters = (endpoint, pars) -> {
        AtomicReference<String> pathParameter = new AtomicReference<>(endpoint);
        pars.keySet()
                .forEach(k -> pathParameter.lazySet(pathParameter.get().replaceFirst("\\{".concat(k).concat("\\}"), pars.get(k).toString())));
        return pathParameter.get();
    };


    Function<Map<String,Object>, String[]> mapToStringArray = map -> {
        AtomicReference<List<String>> composition = new AtomicReference<>(new ArrayList<>());
        map.entrySet().iterator().forEachRemaining(i -> {
            composition.get().add(i.getKey());
            composition.get().add(String.valueOf(i.getValue())); });
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
            if(iter.hasNext()) composition.get().append("&"); });
        return composition.get().toString();
    };

    /**
     * Function to generate Map<String, Object> from a json String
     */
    Function<String, Map<String, Object>> generateMapFromString = dataVar ->
            Arrays.stream(dataVar.replace("{", "").replace("}", "").split(","))
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(k -> k[0].strip(), k -> k[1].strip()));


    /**
     * Needed for Https requests. It should be used whithin HttpClient.
     * Ex: HttpClient.newBuilder().sslContext(sslContext.apply(trustAllCerts.get())).build()
     */
    Supplier<TrustManager[]> trustAllCerts = () -> new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }}
    };

    Function<TrustManager[], SSLContext> sslContext = trustManager -> {
        SSLContext sc = Try.of(() -> SSLContext.getInstance("SSL")).get();
        Try.run(() -> sc.init(null, trustManager, new java.security.SecureRandom())).get();
        return sc;
    };


}
