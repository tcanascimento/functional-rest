package pojo.temp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Headers{

	@JsonProperty("User-Agent")
	private String userAgent;
	@JsonProperty("Host")
	private String host;
	@JsonProperty("X-Amzn-Trace-Id")
	private String xAmznTraceId;
	@JsonProperty("Content-Type")
	private String contentType;

	public void setUserAgent(String userAgent){
		this.userAgent = userAgent;
	}

	public String getUserAgent(){
		return userAgent;
	}

	public void setHost(String host){
		this.host = host;
	}

	public String getHost(){
		return host;
	}

	public void setXAmznTraceId(String xAmznTraceId){
		this.xAmznTraceId = xAmznTraceId;
	}

	public String getXAmznTraceId(){
		return xAmznTraceId;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	@Override
 	public String toString(){
		return 
			"Headers{" + 
			"user-Agent = '" + userAgent + '\'' + 
			",host = '" + host + '\'' + 
			",x-Amzn-Trace-Id = '" + xAmznTraceId + '\'' + 
			",content-Type = '" + contentType + '\'' + 
			"}";
		}
}
