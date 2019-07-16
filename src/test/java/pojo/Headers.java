
package pojo;


import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Headers {

    @JsonProperty("Content-Type")
    private String mContentType;
    @JsonProperty("Host")
    private String mHost;
    @JsonProperty("User-Agent")
    private String mUserAgent;

    public Headers(){}

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public String getUserAgent() {
        return mUserAgent;
    }

    public void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    public Headers(String mContentType) {
        this.mContentType = mContentType;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "mContentType='" + mContentType + '\'' +
                ", mHost='" + mHost + '\'' +
                ", mUserAgent='" + mUserAgent + '\'' +
                '}';
    }
}
