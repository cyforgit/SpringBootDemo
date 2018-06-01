package baseProject.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import baseProject.pojo.User;

@Mapper
public interface UserDao {

	@Select("SELECT * FROM users LIMIT 3")
	@Results({ @Result(property = "id", column = "ID"), @Result(property = "userName", column = "USERNAME"),
			@Result(property = "passWord", column = "PASSWORD"), @Result(property = "mobile", column = "MOBILE"),
			@Result(property = "email", column = "EMAIL"), })
	List<User> getUsers();

	@Select("SELECT * FROM users WHERE ID= #{id}")
	@Results({ @Result(property = "id", column = "ID"), @Result(property = "userName", column = "USERNAME"),
			@Result(property = "passWord", column = "PASSWORD"), @Result(property = "mobile", column = "MOBILE"),
			@Result(property = "email", column = "EMAIL"), })
	List<User> getUserById(@Param("id") int id);
	
	@Insert("INSERT INTO `testdb`.`users` (`USERNAME`, `PASSWORD`, `MOBILE`, `EMAIL`) VALUES ('name4', 'passwd4', '123456', 'kkkkai'); ")
	void insertUser();
}
