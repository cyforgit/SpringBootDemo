import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.*;

public class commonTest {

    private void add(List<List<Integer>> req) {
        HashMap<Integer, Integer> res = new HashMap<>();
        req.forEach(v -> {
            for (int i = 0; i < v.size(); i++) {
                res.put(i, res.get(i) == null ? v.get(i) : res.get(i) + v.get(i));
            }
        });
        
    }

    @Test
    public void TestJwt() {
        String secret = "secret";//假设服务端秘钥
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //jwt 头部信息
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Date nowDate = new Date();
        Date expireDate = AddDate(nowDate, 2 * 60);//120 分钟过期

        String token = JWT.create()
                .withHeader(map)
                .withIssuer("SERVICE") //对应 paylaod iss 节点：签发人
                .withClaim("loginName", "fishpro")
                .withSubject("this is a token demo")//对应 paylaod sub 节点：主题
                .withAudience("Client")//对应 paylaod aud 节点：受众
                .withIssuedAt(nowDate)//对应 paylaod iat 节点：生效时间
                .withExpiresAt(expireDate) //对应 paylaod  exp 签发人 节点：过期时间
                .sign(algorithm);
        System.out.println(token);
        verifyJWTToken(token);
    }

    private static void verifyJWTToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("SERVICE")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        String subject = jwt.getSubject();
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("loginName");
        System.out.println("自定义 claim：" + claim.asString());

        List<String> audience = jwt.getAudience();
        System.out.println("subject 值：" + subject);
        System.out.println("audience 值：" + audience.get(0));
    }

    private static Date AddDate(Date date, Integer minute) {
        if (null == date)
            date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();

    }

    @Test
    public void T1() {
        Resource resource = new Resource();
        Add_Thread add_thread = new Add_Thread(resource);
        sub_Thread sub_thread = new sub_Thread(resource);
        new Thread(add_thread, "加线程 A").start();
        new Thread(add_thread, "加线程 B").start();
        new Thread(sub_thread, "减线程 X").start();
        new Thread(sub_thread, "减线程 Y").start();
    }
}

class Add_Thread implements Runnable {
    private Resource resource;

    public Add_Thread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 10; i++) {
                resource.add();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class sub_Thread implements Runnable {
    private Resource resource;

    public sub_Thread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                resource.sub();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Resource {
    private int count = 0;
    private boolean flag = true;//false = sub true = add

    public synchronized void add() throws InterruptedException {
        //当前应该执行减法操作，线程进行等待
        if (!flag) {
            super.wait();
        }
        Thread.sleep(100);

        this.count++;    //当去掉this的时候，结果变了。 个人感觉，应该是加了锁的原因，如果不加锁，则count是公共变量，加入后当前count只能被一个线程所操作
        System.out.println("加法操作：" + Thread.currentThread().getName() + "," + (count));
        flag = false;
        super.notifyAll();
    }

    public synchronized void sub() throws InterruptedException {
        //当前应该执行加法操作，线程进行等待
        if (flag) {
            super.wait();
        }
        Thread.sleep(200);
        this.count--;    //当去掉this的时候，结果变了。 个人感觉，应该是加了锁的原因，如果不加锁，则count是公共变量，加入后当前count只能被一个线程所操作
        System.out.println("减法操作：" + Thread.currentThread().getName() + "," + (count));
        flag = true;
        super.notifyAll();
    }
}
