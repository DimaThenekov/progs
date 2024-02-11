package utility;

public class ExitObject {
	public boolean isSuccessfully = false;
	public Object returnObject = null;
	public ExitObject(){ returnObject = ""; }
	public ExitObject(boolean isSuccessfully, Object returnObject){ this.isSuccessfully = isSuccessfully; this.returnObject = returnObject; }
	public void print(String s) { returnObject = returnObject==null?s:((String)returnObject) + s; }
	public void println(String s) { returnObject = returnObject==null?s + "\n":((String)returnObject) + s + "\n"; }
	public ExitObject isOk() { isSuccessfully = true; return this; }
	public ExitObject isNotOk() { isSuccessfully = false; return this; }
}