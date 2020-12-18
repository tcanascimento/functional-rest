package base;

import io.vavr.API;
import io.vavr.control.Try;

import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static functions.BaseUtils.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;

public class Specs {

    private static final Logger LOG = Logger.getLogger(Builder.class.getName());

    private URI baseUrl;
    private String endpoint;
    private Map<String, Object> headers;
    private Map<String, Object> queryParams;
    private Map<String, Object> pathParams;
    private HttpRequest.BodyPublisher body;
    private char responseHandlerType = 's';
    private Authenticator authenticator;
    private CookieHandler cookieHandler;
    private Duration timeout = Duration.ofSeconds(90);
    private HttpClient baseClient;
    private URI uri;

    private String requestMethod;

    private Specs(){}

    public static class Builder{

        private Specs tempSpecs;

        public Builder(){
            tempSpecs = new Specs();
        }

        Specs build(){
            Specs specs = tempSpecs;
            tempSpecs = new Specs();

            assert(!this.tempSpecs.baseUrl.toString().isBlank()): "you should set a baseURL!";
            assert(!this.tempSpecs.requestMethod.isBlank()): "you should provide a request Method!";
            assert(checkParameters()): "you should provide an endpoint for path or query parameters!";
            assert(!this.tempSpecs.headers.isEmpty()): "You should provide headers for request!";

            var tempEndpoint = this.tempSpecs.endpoint;
            if(!checkParameters()) tempEndpoint = setPathParameters.apply(this.tempSpecs.endpoint, this.tempSpecs.pathParams);
            if(!checkParameters()) tempEndpoint = (this.tempSpecs.queryParams == null || this.tempSpecs.queryParams.isEmpty()) ? tempEndpoint : queryParametersComposition.apply(tempEndpoint, this.tempSpecs.queryParams);

            if(endpointCheckNull()) {
                assert(!tempEndpoint.contains("{")): "incorrect format for endpoint!";
                setURI(this.tempSpecs.baseUrl.toString(), tempEndpoint);
            }

            if(null == this.tempSpecs.baseClient) buildBaseClient();

            return specs;
        }

        private Boolean checkParameters(){
            return (this.tempSpecs.pathParams != null || this.tempSpecs.queryParams != null) ? endpointCheckNull()
                    : Boolean.TRUE;
        }

        private Boolean endpointCheckNull(){
            return this.tempSpecs.endpoint != null && !this.tempSpecs.endpoint.isBlank();
        }

        private void setBody(String reqBody) {
            if(reqBody.endsWith(".json")){
                this.tempSpecs.body = Try.of(() -> HttpRequest.BodyPublishers.ofFile(Path.of(reqBody)))
                        .onFailure(throwable -> LOG.severe(throwable.toString()))
                        .getOrElse(HttpRequest.BodyPublishers.ofString(""));
            } else { this.tempSpecs.body = HttpRequest.BodyPublishers.ofString(reqBody); }
        }

        private void buildBaseClient() {
            if(this.tempSpecs.cookieHandler != null) {
                this.tempSpecs.baseClient = HttpClient
                        .newBuilder()
                        .cookieHandler(this.tempSpecs.cookieHandler)
                        .connectTimeout(this.tempSpecs.timeout)
                        .build();
            } else {
                this.tempSpecs.baseClient = HttpClient
                        .newBuilder()
                        .connectTimeout(this.tempSpecs.timeout)
                        .build();
            }
        }

        private void setURI(String baseURL, String endpoint){
            this.tempSpecs.uri = URI.create(baseURL).resolve(endpoint);
        }

        public Builder baseURL(String baseURL){
            this.tempSpecs.uri = URI.create(baseURL);
            this.tempSpecs.baseUrl = URI.create(baseURL);
            return this;
        }

        public Builder headersParams(Map<String, Object> headers){
            this.tempSpecs.headers = headers;
            return this;
        }

        /**
         * 's' for String, 'i' for InputStream, 'b' for ByteArray -> String Type is set as default.
         * @param responseHandlerType
         */
        public Builder setResponseHandlerType(char responseHandlerType) {
            this.tempSpecs.responseHandlerType = responseHandlerType;
            return this;
        }

        public Builder requestMethod(String requestMethod){
            assert(!requestMethod.isBlank()): "you should specify a request method!";
            this.tempSpecs.requestMethod = requestMethod;
            return this;
        }

        public Builder body(String body){
            setBody(body);
            return this;
        }

        public Builder body(HttpRequest.BodyPublisher body){
            this.tempSpecs.body = body;
            return this;
        }

        public Builder endpoint(String endpoint){
            this.tempSpecs.endpoint = endpoint;
            return this;
        }

        public Builder pathParams(Map<String, Object> pathParams){
            this.tempSpecs.pathParams = pathParams;
            return this;
        }

        public Builder queryParams(Map<String, Object> queryParams){
            this.tempSpecs.queryParams = queryParams;
            return this;
        }

        public Builder baseClient(HttpClient client){
            this.tempSpecs.baseClient = client;
            return this;
        }

        public Builder cookieHandler(CookieHandler cookieHandler){
            this.tempSpecs.cookieHandler = cookieHandler;
            return this;
        }

        public Builder authenticator(Authenticator authenticator){
            this.tempSpecs.authenticator = authenticator;
            return this;
        }

        public Builder timeout(Duration timeout){
            this.tempSpecs.timeout = timeout;
            return this;
        }

    }


    public HttpRequest getRequestMethod(){
        return API.Match(this.requestMethod.toUpperCase()).of(
                Case($("GET"), baseRequestBuilder().GET().build()),
                Case($("POST"), baseRequestBuilder().POST(body).build()),
                Case($("PUT"), baseRequestBuilder().PUT(body).build()),
                Case($("DELETE"), baseRequestBuilder().DELETE().build()),
                Case($(), baseRequestBuilder().GET().build()));
    }

    public String[] getHeaders() {
        return mapToStringArray.apply(headers);
    }

    public Map<String, Object> getHeadersMap(){
        return headers;
    }

    /**
     *
     * @param endpoint
     * @param queryPars as Map, it does not guarantee order; so, API should know how to resolve parameters.
     * @return
     */
    private String setQueryParams(String endpoint, Map<String, Object> queryPars){
        this.queryParams = queryPars;
        return (queryPars == null || queryPars.isEmpty()) ? endpoint : queryParametersComposition.apply(endpoint, queryPars);
    }

    public String getFormatedEndpoint(){
        var tempEndpoint = setPathParameters.apply(this.endpoint, pathParams);
        tempEndpoint = setQueryParams(tempEndpoint, queryParams);
        return tempEndpoint;
    }


    public URI getBaseUrl() {
        return baseUrl;
    }

    public String getRawEndpoint() {
        return endpoint;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    public HttpRequest.BodyPublisher getBody() {
        return body;
    }

    public char getResponseHandlerType() {
        return responseHandlerType;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public HttpClient getBaseClient() {
        return baseClient;
    }

    public URI getUri() {
        return uri;
    }

    public HttpResponse.BodyHandler getResponseBodyHandler(){
        return API.Match(this.responseHandlerType).of(
                Case($('s'), HttpResponse.BodyHandlers.ofString()),
                Case($('i'), HttpResponse.BodyHandlers.ofInputStream()),
                Case($('b'), HttpResponse.BodyHandlers.ofByteArray()),
//                Case($('l'), HttpResponse.BodyHandlers.fromLineSubscriber(Subscriber<String>)),
                Case($(), (Supplier<HttpResponse.BodyHandler<String>>) HttpResponse.BodyHandlers::ofString));
    }


    private HttpRequest.Builder baseRequestBuilder(){
        return HttpRequest
                .newBuilder()
                .uri(this.getUri())
                .timeout(this.getTimeout())
                .headers(this.getHeaders());
    }


    @Override
    public String toString() {
        return "Specs{" +
                "baseUrl=" + baseUrl +
                ", endpoint='" + endpoint + '\'' +
                ", headers=" + headers +
                ", queryParams=" + queryParams +
                ", pathParams=" + pathParams +
                ", body=" + body +
                ", responseHandlerType=" + responseHandlerType +
                ", authenticator=" + authenticator +
                ", cookieHandler=" + cookieHandler +
                ", timeout=" + timeout +
                ", baseClient=" + baseClient +
                ", uri=" + uri +
                ", requestMethod='" + requestMethod + '\'' +
                '}';
    }
}
