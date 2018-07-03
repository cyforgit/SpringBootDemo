package baseProject;

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

import baseProject.App;
import baseProject.dao.UserDao;

@TestPropertySource("classpath:springboottest.cfg")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@PowerMockIgnore("javax.management.*") // 解决包冲突
@SpringBootTest
public class BasicTest {

    @Autowired
    UserDao userDao;

    @Test
    public void userH2DaoTest() {
        System.out.println("****");
        System.out.println(userDao.getUsers().size()+"->num");
    }
}
