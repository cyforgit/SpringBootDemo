package baseProject.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import baseProject.pojo.User;

/**
 * 接口无法编写方法体，所以如果需要动态SQL，可以使用内部类提供sql provider * 在provider中，推荐使用new SQL()方法，方便排查
 */
@Mapper
public interface UserDao {

    @Select("SELECT * FROM users")
    @Results({ @Result(property = "id", column = "ID"), @Result(property = "userName", column = "USERNAME"),
            @Result(property = "passWord", column = "PASSWORD"), @Result(property = "mobile", column = "MOBILE"),
            @Result(property = "email", column = "EMAIL"), })
    public List<User> getUsers();

    @SelectProvider(type = UserDaoProvider.class, method = "getUserFromProvider")
    public List<User> getUserFromProvider(@Param("id") int id);

    @Select("SELECT * FROM users WHERE ID= #{id}")
    @Results({ @Result(property = "id", column = "ID"), @Result(property = "userName", column = "USERNAME"),
            @Result(property = "passWord", column = "PASSWORD"), @Result(property = "mobile", column = "MOBILE"),
            @Result(property = "email", column = "EMAIL"), })
    public List<User> getUserById(@Param("id") int id);

    @Insert("INSERT INTO `testdb`.`users` (`USERNAME`, `PASSWORD`, `MOBILE`, `EMAIL`) VALUES ('name4', 'passwd4', '123456', 'kkkkai'); ")
    void insertUser(User user);

    @Delete("DELETE FROM users WHERE USERNAME=#{username}")
    public void deteleUserByUserName(@Param("username") String username);

    @Update("UPDATE users SET USERNAME=#{username} WHERE ID=#{id}")
    public int updateUsernameById(@Param("username") String username, @Param("id") int id);

    class UserDaoProvider {
        public String getUserFromProvider(Map<String, Object> map) {
            String quaryString = new SQL() {
                {
                    SELECT("*");
                    FROM("users");
                    if ((int) map.get("id") != 1) {
                        WHERE("ID=#{id}");
                    } else {

                    }
                }
            }.toString();
            System.out.println(quaryString);
            return quaryString;
        }
    }
}
