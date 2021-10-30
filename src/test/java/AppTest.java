import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class AppTest {
    static class intThread implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return 0;
        }
    }

    static class myThread implements Callable<String> {
        private String name;
        private long waittime;

        public myThread(String name, long waittime) {
            this.name = name;
            this.waittime = waittime;
        }

        public myThread(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            if (this.waittime != 0) {
                try {
                    System.out.println("wait:" + this.waittime);
                    Thread.sleep(this.waittime);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            return this.name;
        }
    }

    @Test
    public void futureTest() {
        FutureTask ft = new FutureTask(new myThread("1"));
        FutureTask ft2 = new FutureTask(new myThread("2"));
        long timestamp = System.currentTimeMillis();
        ExecutorService threadpool = Executors.newFixedThreadPool(3);
        Future<String> future1 = threadpool.submit(new myThread("1", 3000));
        Future<String> future2 = threadpool.submit(new myThread("2", 2000));
//        threadpool.invokeAll()
        try {
            System.out.println(future2.get());
            System.out.println(future1.get());
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - timestamp);
        } catch (Exception e) {

        }
    }

    @Test
    public void shiroTest() {


        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            System.out.println("auth failed");
        }
        System.out.println(subject.isAuthenticated());
//        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    @Test
    void shiroTest2() {
        //初始化管理
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();

    }
}
