
package pojo;


import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ResponseObject {

    @JsonProperty("args")
    private Args mArgs;
    @JsonProperty("data")
    private String mData;
    @JsonProperty("files")
    private Files mFiles;
    @JsonProperty("form")
    private Form mForm;
    @JsonProperty("headers")
    private Headers mHeaders;
    @JsonProperty("json")
    private Object mJson;
    @JsonProperty("origin")
    private String mOrigin;
    @JsonProperty("url")
    private String mUrl;

    public Args getArgs() {
        return mArgs;
    }

    public void setArgs(Args args) {
        mArgs = args;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    public Files getFiles() {
        return mFiles;
    }

    public void setFiles(Files files) {
        mFiles = files;
    }

    public Form getForm() {
        return mForm;
    }

    public void setForm(Form form) {
        mForm = form;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Headers headers) {
        mHeaders = headers;
    }

    public Object getJson() {
        return mJson;
    }

    public void setJson(Object json) {
        mJson = json;
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
        return "ResponseObject{" +
                "mArgs=" + mArgs +
                ", mData='" + mData + '\'' +
                ", mFiles=" + mFiles +
                ", mForm=" + mForm +
                ", mHeaders=" + mHeaders +
                ", mJson=" + mJson +
                ", mOrigin='" + mOrigin + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
