package baseProject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import baseProject.dao.UserRedisDao;

@TestPropertySource("classpath:springboottest.cfg")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@PowerMockIgnore("javax.management.*") // 解决包冲突
@SpringBootTest
// @DataRedisTest 使用此注释，则进行测试时相关的bean不会被加载进context
public class RedisTest {

    @Autowired
    UserRedisDao userRedisDao;

    @Test
    public void testRedis() {

    }

}
