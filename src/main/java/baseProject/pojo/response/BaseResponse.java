package baseProject.pojo.response;

public class BaseResponse {

	int resultCode = 0;

	String resultMsg = "";

	
	
	public BaseResponse() {
		super();
	}

	public BaseResponse(int resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
