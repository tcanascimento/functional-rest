package base;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import functions.BaseUtils;
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
import java.util.logging.Logger;

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


    public RestSpecs(String baseUrl, Map<String, Object> headersParams, String body){
        this.baseUrl = URI.create(baseUrl);
        this.uri = URI.create(baseUrl);
        this.headers = headersParams;
        if(body != null) setBody(body); else setBody("");
    }

    public RestSpecs(String baseUrl, String endp, Map<String, Object> headersParams, Map<String, Object> queryParams,
                     Map<String, Object> pathParams, String body) {
        this.baseUrl = URI.create(baseUrl);
        this.headers = headersParams;
        this.endpoint = endp;
        var tempEndpoint = this.endpoint;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        if(getRawEndpoint() != null && !getRawEndpoint().isBlank()) tempEndpoint = setPathParameters.apply(this.endpoint, pathParams);
        if(getRawEndpoint() != null && !getRawEndpoint().isBlank()) tempEndpoint = setQueryParams(tempEndpoint, queryParams);
        if(getRawEndpoint() != null) setURI(baseUrl, tempEndpoint); else this.uri = URI.create(baseUrl);
        if(body != null) setBody(body); else setBody("");
    }

    private void setURI(String baseURL, String endpoint){
        this.uri = URI.create(baseURL).resolve(endpoint);
    }

    public HttpResponse.BodyHandler getResponseBodyHandler(){
        switch (responseHandlerType){
            case 's': return HttpResponse.BodyHandlers.ofString();
            case 'i': return HttpResponse.BodyHandlers.ofInputStream();
            case 'b': return HttpResponse.BodyHandlers.ofByteArray();
            default: return HttpResponse.BodyHandlers.ofString();
        }
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

    public void setBaseUrl(String baseUrl) {
        assert baseUrl.isBlank(): "baseURL cannot be empty!";
        this.baseUrl = URI.create(baseUrl);
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


    private void setBody(String requestBody) {
        if (requestBody.endsWith(".json")) {
            setBody(Path.of(requestBody));
        } else {
            this.body = HttpRequest.BodyPublishers.ofString(requestBody);
        }
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

    /**
     *
     * @param endpoint
     * @param queryParams as Map, it does not guarantee order; so, API should how to resolve parameters indepently of their order.
     * @return
     */
    private String setQueryParams(String endpoint, Map<String, Object> queryParams){
        this.queryParams = queryParams;
        return (queryParams == null || queryParams.size() < 2) ? endpoint : queryParametersComposition.apply(endpoint, queryParams);
    }

    private void buildBaseClient() {
       if(cookieHandler != null) {
           baseClient = HttpClient
                   .newBuilder()
                   .cookieHandler(cookieHandler)
                   .connectTimeout(timeout)
                   .build();
       } else {
           baseClient = HttpClient
                   .newBuilder()
                   .connectTimeout(timeout)
                   .build();
       }
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
                ", responseHandlerType=" + responseHandlerType +
                ", authenticator=" + authenticator +
                ", cookieHandler=" + cookieHandler +
                ", timeout=" + timeout +
                ", baseClient=" + baseClient +
                ", uri=" + uri +
                '}';
    }
}
