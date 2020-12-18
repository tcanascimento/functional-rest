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

public final class RestSpecs {

    private static final Logger LOG = Logger.getLogger(RestSpecs.class.getName());

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

    public RestSpecs build(){
        assert(!this.baseUrl.toString().isBlank()): "you should set a baseURL!";
        assert(!this.requestMethod.isBlank()): "you should provide a request Method!";
        assert(checkParameters()): "you should provide an endpoint for path or query parameters!";
        assert(!headers.isEmpty()): "You should provide headers for request!";

        var tempEndpoint = this.endpoint;
        if(!checkParameters()) tempEndpoint = setPathParameters.apply(this.endpoint, pathParams);
        if(!checkParameters()) tempEndpoint = setQueryParams(tempEndpoint, queryParams);

        if(endpointCheckNull()) {
            assert(!tempEndpoint.contains("{")): "incorrect format for endpoint!";
            setURI(this.baseUrl.toString(), tempEndpoint);
        }

        if(null == baseClient) buildBaseClient();
        return this;
    }

    private Boolean checkParameters(){
        return (this.pathParams != null || this.queryParams != null) ? endpointCheckNull()
                : Boolean.TRUE;
    }

    private Boolean endpointCheckNull(){
        return this.endpoint != null && !this.endpoint.isBlank();
    }

    public RestSpecs baseURL(String baseURL){
        this.uri = URI.create(baseURL);
        this.baseUrl = URI.create(baseURL);
        return this;
    }

    public RestSpecs headersParams(Map<String, Object> headers){
        this.headers = headers;
        return this;
    }

    /**
     * 's' for String, 'i' for InputStream, 'b' for ByteArray -> String Type is set as default.
     * @param responseHandlerType
     */
    public RestSpecs setResponseHandlerType(char responseHandlerType) {
        this.responseHandlerType = responseHandlerType;
        return this;
    }

    public RestSpecs requestMethod(String requestMethod){
        assert(!requestMethod.isBlank()): "you should specify a request method!";
        this.requestMethod = requestMethod;
        return this;
    }

    public RestSpecs body(String body){
        setBody(body);
        return this;
    }

    public RestSpecs body(HttpRequest.BodyPublisher body){
        this.body = body;
        return this;
    }

    public RestSpecs endpoint(String endpoint){
        this.endpoint = endpoint;
        return this;
    }

    public RestSpecs pathParams(Map<String, Object> pathParams){
        this.pathParams = pathParams;
        return this;
    }

    public RestSpecs queryParams(Map<String, Object> queryParams){
        this.queryParams = queryParams;
        return this;
    }

    public RestSpecs baseClient(HttpClient client){
        this.baseClient = client;
        return this;
    }

    public RestSpecs cookieHandler(CookieHandler cookieHandler){
        this.cookieHandler = cookieHandler;
        return this;
    }

    public RestSpecs authenticator(Authenticator authenticator){
        this.authenticator = authenticator;
        return this;
    }

    public RestSpecs timeout(Duration timeout){
        this.timeout = timeout;
        return this;
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

    private void setBody(String reqBody) {
        if(reqBody.endsWith(".json")){
            this.body = Try.of(() -> HttpRequest.BodyPublishers.ofFile(Path.of(reqBody)))
                    .onFailure(throwable -> LOG.severe(throwable.toString()))
                    .getOrElse(HttpRequest.BodyPublishers.ofString(""));
        } else { this.body = HttpRequest.BodyPublishers.ofString(reqBody); }
    }

    private void buildBaseClient() {
        if(cookieHandler != null) {
            this.baseClient = HttpClient
                    .newBuilder()
                    .cookieHandler(cookieHandler)
                    .connectTimeout(timeout)
                    .build();
        } else {
            this.baseClient = HttpClient
                    .newBuilder()
                    .connectTimeout(timeout)
                    .build();
        }
    }

    private void setURI(String baseURL, String endpoint){
        this.uri = URI.create(baseURL).resolve(endpoint);
    }

    @Override
    public String toString() {
        return "RestSpecss: {" +
                "baseUrl:" + baseUrl +
                ", endpoint:'" + endpoint + '\'' +
                ", headers:" + headers +
                ", queryParams:" + queryParams +
                ", pathParams:" + pathParams +
                ", body:" + body +
                ", responseHandlerType:" + responseHandlerType +
                ", authenticator:" + authenticator +
                ", cookieHandler:" + cookieHandler +
                ", timeout:" + timeout +
                ", baseClient:" + baseClient +
                ", uri:" + uri +
                ", requestMethod:'" + requestMethod + '\'' +
                '}';
    }
}
