package baseProject.pojo.response;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	int id;
	String userName;
	String passWord;
	String mobile;
	String email;
}
