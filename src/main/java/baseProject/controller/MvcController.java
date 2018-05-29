package baseProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import baseProject.pojo.response.BaseResponse;

@RestController
public class MvcController {

	@RequestMapping("/index")
	public BaseResponse getBaseResponse() {
		return new BaseResponse();
	}

}
