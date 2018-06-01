package baseProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baseProject.dao.UserDao;
import baseProject.pojo.response.BaseResponse;

@RestController
public class MvcController {

	@Value("${kkkkk}")
	public String key;

	@Autowired
	UserDao userDao;

	@RequestMapping("/index")
	public BaseResponse getBaseResponse() {
		userDao.getUsers().forEach((obj) -> {
			System.out.println(obj.toString());
		});

		return new BaseResponse(0, key);
	}

}
