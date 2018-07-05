package baseProject;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import baseProject.dao.UserDao;
import baseProject.pojo.User;
import baseProject.service.TransactionService;

//@TestPropertySource("classpath:springboottest.cfg")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@PowerMockIgnore("javax.management.*") // 解决包冲突
@SpringBootTest
public class UserDaoTest {

    @Autowired
    UserDao userDao;
    @Autowired
    TransactionService tranService;

    @Test
    public void tranTest() {
        List<User> result = userDao.getUserById(1);
        result.forEach((user) -> {
            System.out.println(user);
        });
        try {
            tranService.deleteUserIfName(1, "name1");
        } catch (Exception e) {
            System.out.println(e);
        }
        List<User> result2 = userDao.getUserById(1);
        result2.forEach((user) -> {
            System.out.println(user);
        });
        try {
            tranService.deleteUserIfName(2, "name1");
        } catch (Exception e) {
            System.out.println(e);
        }
        List<User> result3 = userDao.getUserById(1);
        result3.forEach((user) -> {
            System.out.println(user);
        });
    }

    @Ignore
    @Test
    public void mapperTest() {
        List<User> result = userDao.getUsers();
        System.out.println(result.size());
        result.forEach((user) -> {
            System.out.println(user);
        });
    }

    @Ignore
    @Test
    public void mapperTest2() {
        System.out.println("****");
        List<User> result = userDao.getUserFromProvider(2);
        System.out.println(result.size());
        result.forEach((user) -> {
            System.out.println(user);
        });
    }

    @Ignore
    @Test
    public void mapperTest3() {
        List<User> result = userDao.getUserById(1);
        System.out.println(result.size());
        result.forEach((user) -> {
            System.out.println(user);
        });
    }

    @Ignore
    @Test
    public void mapperTest4() {
        List<User> result = userDao.getUsers();
        System.out.println(result.size());
        result.forEach((user) -> {
            System.out.println(user);
        });
    }
}
