
package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {

    @JsonProperty("args")
    private Args mArgs;
    @JsonProperty("headers")
    private Headers mHeaders;
    @JsonProperty("origin")
    private String mOrigin;
    @JsonProperty("url")
    private String mUrl;

    public Body() {
    }

    public Args getArgs() {
        return mArgs;
    }

    public void setArgs(Args args) {
        mArgs = args;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Headers headers) {
        mHeaders = headers;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return "Body{" +
                "mArgs=" + mArgs +
                ", mHeaders=" + mHeaders +
                ", mOrigin='" + mOrigin + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
