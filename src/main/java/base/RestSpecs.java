package base;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import functions.BaseUtils;
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

import static io.vavr.API.$;
import static io.vavr.API.Case;

public final class RestSpecs implements BaseUtils {

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


    public RestSpecs(String baseUrl, Map<String, Object> headersParams, String body, String requestMethod){
        this.baseUrl = URI.create(baseUrl);
        this.uri = URI.create(baseUrl);
        this.headers = headersParams;
        assert(!requestMethod.isBlank()): "you should specify a request method!";
        this.requestMethod = requestMethod;
        if(body != null) setBody(body); else setBody("");
    }

    public RestSpecs(String baseUrl, String endp, Map<String, Object> headersParams, Map<String, Object> queryParams,
                     Map<String, Object> pathParams, String body, String requestMethod) {
        this.baseUrl = URI.create(baseUrl);
        this.headers = headersParams;
        this.endpoint = endp;
        var tempEndpoint = this.endpoint;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        assert(!requestMethod.isBlank()): "you should specify a request method!";
        this.requestMethod = requestMethod;
        if(getRawEndpoint() != null && !getRawEndpoint().isBlank()) tempEndpoint = setPathParameters.apply(this.endpoint, pathParams);
        if(getRawEndpoint() != null && !getRawEndpoint().isBlank()) tempEndpoint = setQueryParams(tempEndpoint, queryParams);
        if(getRawEndpoint() != null) setURI(baseUrl, tempEndpoint); else this.uri = URI.create(baseUrl);
        if(body != null) setBody(body); else setBody("");
    }

    private void setURI(String baseURL, String endpoint){
        this.uri = URI.create(baseURL).resolve(endpoint);
    }

    public HttpResponse.BodyHandler getResponseBodyHandler(){
        return API.Match(this.responseHandlerType).of(
                Case($('s'), HttpResponse.BodyHandlers.ofString()),
                Case($('i'), HttpResponse.BodyHandlers.ofInputStream()),
                Case($('b'), HttpResponse.BodyHandlers.ofByteArray()),
//                Case($('l'), HttpResponse.BodyHandlers.fromLineSubscriber(Subscriber<String>)),
                Case($(), (Supplier<HttpResponse.BodyHandler<String>>) HttpResponse.BodyHandlers::ofString));
    }

    public char getResponseHandlerType() {
        return responseHandlerType;
    }

    /**
     * 's' for String, 'i' for InputStream, 'b' for ByteArray -> String Type is set as default.
     * @param responseHandlerType
     */
    public void setResponseHandlerType(char responseHandlerType) {
        this.responseHandlerType = responseHandlerType;
    }

    public void setBaseUrl(String url) {
        assert(!url.isBlank()): "baseURL cannot be empty!";
        this.baseUrl = URI.create(url);
    }

    public URI getURI() {
        return uri;
    }

    public URI getBaseUrl(){
        return baseUrl;
    }

    public String[] getHeaders() {
        return mapToStringArray.apply(headers);
    }

    public Map<String, Object> getHeadersMap() {return headers;}

    public void setHeaders(Map<String, Object> headers) {
                this.headers = headers;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public HttpRequest.BodyPublisher getBody() {
        return body;
    }

    public void setBody(){
        this.body = HttpRequest.BodyPublishers.noBody();
    }

    public void setRequestMethod(String method){
        this.requestMethod = method;
    }

    private void setBody(String requestBody) {
        if (requestBody.endsWith(".json")) setBody(Path.of(requestBody)); else this.body = HttpRequest.BodyPublishers.ofString(requestBody);
    }

    public void setBody(byte[] body){
        this.body = HttpRequest.BodyPublishers.ofByteArray(body);
    }

    /**
     * @param filePathBody
     */
    private void setBody(Path filePathBody){
        Try.of(()-> this.body = HttpRequest.BodyPublishers.ofFile(filePathBody))
                .onFailure(throwable -> LOG.severe(throwable.toString()));
    }

    public void setBody(HttpRequest.BodyPublisher body){
        this.body = body;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }

    public void setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

    public HttpClient getBaseClient() {
        if(null == baseClient) buildBaseClient();
        return baseClient;
    }

    public void setBaseClient(HttpClient client){
        this.baseClient = client;
    }

    public String getRawEndpoint() {
        return endpoint;
    }

    public String getFormatedEndpoint(){
        var tempEndpoint = setPathParameters.apply(this.endpoint, pathParams);
        tempEndpoint = setQueryParams(tempEndpoint, queryParams);
        return tempEndpoint;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    private String setPathParams(Map<String, Object> pathParams, String endpoint) {
        this.pathParams = pathParams;
        return (pathParams != null && pathParams.size() % 2 == 0) ? setPathParameters.apply(endpoint, pathParams) : endpoint;
    }

    public void setURI(String uri){
        this.uri = URI.create(uri);
    }

    public HttpRequest getRequestMethod(){
        return API.Match(this.getRawRequestMethod().toUpperCase()).of(
                Case($("GET"), baseRequestBuilder().GET().build()),
                Case($("POST"), baseRequestBuilder().POST(this.getBody()).build()),
                Case($("PUT"), baseRequestBuilder().PUT(this.getBody()).build()),
                Case($("DELETE"), baseRequestBuilder().DELETE().build()));
    }

    public String getRawRequestMethod(){
        return this.requestMethod;
    }

    private HttpRequest.Builder baseRequestBuilder(){
        return HttpRequest
                .newBuilder()
                .uri(this.getURI())
                .timeout(this.getTimeout())
                .headers(this.getHeaders());
    }

    /**
     *
     * @param endpoint
     * @param queryPars as Map, it does not guarantee order; so, API should how to resolve parameters.
     * @return
     */
    private String setQueryParams(String endpoint, Map<String, Object> queryPars){
        this.queryParams = queryPars;
        return (queryPars == null || queryPars.isEmpty()) ? endpoint : queryParametersComposition.apply(endpoint, queryPars);
    }

    private void buildBaseClient() {
        this.baseClient = cookieHandler != null ? HttpClient.newBuilder().cookieHandler(cookieHandler)
                .connectTimeout(timeout).build() :
                HttpClient.newBuilder().connectTimeout(timeout).build();
    }


    @Override
    public String toString() {
        return "RestSpecs{" +
                "baseUrl=" + baseUrl +
                ", endpoint=" + endpoint +
                ", formated endpoint=" + getFormatedEndpoint() +
                ", headers=" + headers.values() +
                ", queryParams=" + queryParams +
                ", pathParams=" + pathParams +
                ", body=" + body +
                ", requestMethod=" + requestMethod +
                ", responseHandlerType=" + responseHandlerType +
                ", authenticator=" + authenticator +
                ", cookieHandler=" + cookieHandler +
                ", timeout=" + timeout +
                ", baseClient=" + baseClient +
                ", uri=" + uri +
                '}';
    }
}
