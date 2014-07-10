package vo;

public class ResponseResult {

	private int statusCode=200;
	private String message = "OK";
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static ResponseResult OK(){
		ResponseResult res= new ResponseResult();
		return res;
	}
	public static ResponseResult SYSERROR(){
		ResponseResult res= new ResponseResult();
		res.setMessage("system error");
		res.setStatusCode(500);
		return res;
	}
	public static ResponseResult BUSERROR(){
		ResponseResult res= new ResponseResult();
		res.setMessage("business error");
		res.setStatusCode(400);
		return res;
	}
}
