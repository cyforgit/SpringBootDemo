package baseProject.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

	@Select("SELECT * FROM users LIMIT 1")
	
}
