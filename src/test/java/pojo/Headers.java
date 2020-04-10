
package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Headers {

    @JsonProperty("Connection")
    private String mConnection;
    @JsonProperty("Content-Length")
    private String mContentLength;
    @JsonProperty("Content-Type")
    private String mContentType;
    @JsonProperty("Host")
    private String mHost;
    @JsonProperty("Http2-Settings")
    private String mHttp2Settings;
    @JsonProperty("Upgrade")
    private String mUpgrade;
    @JsonProperty("User-Agent")
    private String mUserAgent;
    @JsonProperty("X-Amzn-Trace-Id")
    private String mXAmznTraceId;

    public String getConnection() {
        return mConnection;
    }

    public void setConnection(String connection) {
        mConnection = connection;
    }

    public String getContentLength() {
        return mContentLength;
    }

    public void setContentLength(String contentLength) {
        mContentLength = contentLength;
    }

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

    public String getHttp2Settings() {
        return mHttp2Settings;
    }

    public void setHttp2Settings(String http2Settings) {
        mHttp2Settings = http2Settings;
    }

    public String getUpgrade() {
        return mUpgrade;
    }

    public void setUpgrade(String upgrade) {
        mUpgrade = upgrade;
    }

    public String getUserAgent() {
        return mUserAgent;
    }

    public void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    public String getmConnection() {
        return mConnection;
    }

    public void setmConnection(String mConnection) {
        this.mConnection = mConnection;
    }

    public String getmContentLength() {
        return mContentLength;
    }

    public void setmContentLength(String mContentLength) {
        this.mContentLength = mContentLength;
    }

    public String getmContentType() {
        return mContentType;
    }

    public void setmContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    public String getmHost() {
        return mHost;
    }

    public void setmHost(String mHost) {
        this.mHost = mHost;
    }

    public String getmHttp2Settings() {
        return mHttp2Settings;
    }

    public void setmHttp2Settings(String mHttp2Settings) {
        this.mHttp2Settings = mHttp2Settings;
    }

    public String getmUpgrade() {
        return mUpgrade;
    }

    public void setmUpgrade(String mUpgrade) {
        this.mUpgrade = mUpgrade;
    }

    public String getmUserAgent() {
        return mUserAgent;
    }

    public void setmUserAgent(String mUserAgent) {
        this.mUserAgent = mUserAgent;
    }

    public String getmXAmznTraceId() {
        return mXAmznTraceId;
    }

    public void setmXAmznTraceId(String mXAmznTraceId) {
        this.mXAmznTraceId = mXAmznTraceId;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "mConnection='" + mConnection + '\'' +
                ", mContentLength='" + mContentLength + '\'' +
                ", mContentType='" + mContentType + '\'' +
                ", mHost='" + mHost + '\'' +
                ", mHttp2Settings='" + mHttp2Settings + '\'' +
                ", mUpgrade='" + mUpgrade + '\'' +
                ", mUserAgent='" + mUserAgent + '\'' +
                '}';
    }
}
