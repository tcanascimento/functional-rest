package pojo.temp;

public class ResponseTemp{
	private Args args;
	private Headers headers;
	private String data;
	private Form form;
	private String origin;
	private Files files;
	private Object json;
	private String url;

	public void setArgs(Args args){
		this.args = args;
	}

	public Args getArgs(){
		return args;
	}

	public void setHeaders(Headers headers){
		this.headers = headers;
	}

	public Headers getHeaders(){
		return headers;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setForm(Form form){
		this.form = form;
	}

	public Form getForm(){
		return form;
	}

	public void setOrigin(String origin){
		this.origin = origin;
	}

	public String getOrigin(){
		return origin;
	}

	public void setFiles(Files files){
		this.files = files;
	}

	public Files getFiles(){
		return files;
	}

	public void setJson(Object json){
		this.json = json;
	}

	public Object getJson(){
		return json;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ResponseTemp{" + 
			"args = '" + args + '\'' + 
			",headers = '" + headers + '\'' + 
			",data = '" + data + '\'' + 
			",form = '" + form + '\'' + 
			",origin = '" + origin + '\'' + 
			",files = '" + files + '\'' + 
			",json = '" + json + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}
