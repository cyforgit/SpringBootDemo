package baseProject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import baseProject.pojo.response.BaseResponse;

@RestController
public class MvcController {

	@Value("${kkkkk}")
	public String key;

	@RequestMapping("/index")
	public BaseResponse getBaseResponse() {
		return new BaseResponse(0, key);
	}

}
