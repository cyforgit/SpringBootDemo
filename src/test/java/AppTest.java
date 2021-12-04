import cn.hutool.log.Log;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Driver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //手动植入账户
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("waha", "lala");
        defaultSecurityManager.setRealm(simpleAccountRealm);
        //获取提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("waha", "lala");
        subject.login(usernamePasswordToken);

        System.out.println(subject.isAuthenticated());
    }

    @Test
    void shrioAuthTest() {
        //初始化管理
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //手动植入账户
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("waha", "lala", "admin");
        defaultSecurityManager.setRealm(simpleAccountRealm);
        //获取提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("waha", "lala");
        subject.login(usernamePasswordToken);
        System.out.println();
        subject.checkRole("adminad");
    }

    DruidDataSource druidDataSource = new DruidDataSource();

    {
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root1234");
    }

    @Test
    void jdbcRealTest() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("Mark", "123456");
        subject.login(usernamePasswordToken);
        System.out.println(subject.isAuthenticated());
//        subject.checkRole("adminad");
    }

    @Test
    void customRealmTest() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(new MyRealm());
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("Mark", "123456");
        subject.login(usernamePasswordToken);
        System.out.println(subject.isAuthenticated());

    }

    @Test
    void testJsonParse() {
        JSONObject object = JSONObject.parseObject("{\"id\":\"EV-2018022511223320873\",\"create_time\":\"2015-05-20T13:29:35+08:00\",\"resource_type\":\"encrypt-resource\",\"event_type\":\"TRANSACTION.SUCCESS\",\"summary\":\"支付成功\",\"resource\":{\"original_type\":\"transaction\",\"algorithm\":\"AEAD_AES_256_GCM\",\"ciphertext\":\"\",\"associated_data\":\"\",\"nonce\":\"\"}}");
        System.out.println(object.toString());
    }

    @Test
    void testData() throws ParseException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str3 = "1900-01-01 08:05:42";
        String str4 = "1900-01-01 08:05:43";
        Date dat3 = sf.parse(str3);
        Date dat4 = sf.parse(str4);
        Long l3 = dat3.getTime() / 1000;
        Long l4 = dat4.getTime() / 1000;
        System.out.println(l4 - l3);
    }

    @Test
    void testAliAppPay() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "app_id", "your private_key", "json", "GBK", "alipay_public_key", "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl("http://1x891966p2.51mypc.cn:50790/alipay/callback");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "120210817010101004");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");
        bizContent.put("product_code", "QUICK_MSECURITY_PAY");
//bizContent.put("time_expire", "2022-08-01 22:00:00");

//// 商品明细信息，按需传入
//JSONArray goodsDetail = new JSONArray();
//JSONObject goods1 = new JSONObject();
//goods1.put("goods_id", "goodsNo1");
//goods1.put("goods_name", "子商品1");
//goods1.put("quantity", 1);
//goods1.put("price", 0.01);
//goodsDetail.add(goods1);
//bizContent.put("goods_detail", goodsDetail);

//// 扩展信息，按需传入
//JSONObject extendParams = new JSONObject();
//extendParams.put("sys_service_provider_id", "2088511833207846");
//bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());
        AlipayTradeAppPayResponse response = null;
        try {
            response = alipayClient.sdkExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    @Test
    void testAliPrePay() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2021000118653954", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCtC9Q3+cytzCCHUOruhEl50em5P3PBW9Xff5Pl5V8z/eOUbFUIkUs6Z9p856zMqP7i5pOLd7DlZ4UkLB1ft7zlgqml2BcVMkdrUDsKFr5zRSS3a7CrpVYS5NN9OG+Y/W/nlneNEEpyRGp7PqRNQPSxpamaKms3+d3cLxCf6jmG0vVGhX69b+5TPBg+YiqDkik+pU5ei5eDo3DmYTpgWug+lNI0NDiFwV7/9gBpAK6Huyys9WlSIOmW6DLsIDfVdOhxkdB5/YHsHwCG2/yf/Gs/jLCy/EMUQQLlkkq7ITIFALlQi1+4/epeUMmj0OoEV0bFDOCoX1zn5bms5EitvCQNAgMBAAECggEAc+WJQ9gtCnW9j4NVZVv6Lfm2FNj3eRg9vMugTYf65EJ1yrFnni5ZeQuXXLJ5nDTPST5usSj/tBVKKbjWD4ZqoDiL0MRSaWLcTHZWpGIpOeCkP6iDkeJRLKRPNP24WHndGfk/pp2zjqZdvp9swUTw3Y37coJYfwv9WHu5IbFox++mREolPsG8VMsk8yTu8Q8v97D0mqOz2adrOugIu/57qFPgNAtVEJgXeP3fPsUtNXiSJhjIRu+H2qUA8gkq6M6W9lR6tjsVhcYOmIC+9r9ImAa+VimPo1j9WgoCNjVptMrwjQC6ij2Pkxf2qzTbC8owaqrbxtIpCXYVNu9dIWg8LQKBgQDYkDEu/gzRBo/f96SN33RTUE4ggTwey6rq2J53zl/nmabvyZ4x0oVHxG5JMAhF1/+vWb3KoNQ74TTBIVkVJOet5nfqgS+NzRES1ahi3jtbzrwMDgovELPra7HIFwlRc5BtXZ63AiAP4zLuCppXEeffU6Dx5jA7EkDrhMfm2krbtwKBgQDMjvJiC1eYUwb5Eb4eaUdlnizVn4hAquMhluDN/WZTbhEgVsswAt5yJx1m6DmBC2Ipy75VbBaFO20ex6IxkXNaEVkUgRK0+x+oHtean3zQVqkk7YA2zfE0vKTQiWsQkrfPpl9+wafpNaxg/VEcG9ryIzU+7R9SSsNGhOOFo1RGWwKBgQDMf6zSETiNEcM+3RzYk8/G9Kgx7ZeWBQEK7y8FNy49B00qVZs6tO8CjC0Qk3jca1GYhgfAlgqT2N/2F8WFP+o+vskx+nerUZ/zxypQ4kXvNdNBFujq9LzRbErfBVHZRmV1wNglPK6GEid/vSdtXQ02SERM6O9WeLAxNpuGFVBnFQKBgQCHrnFbUwahBMFlR72BUneHEanPcGfh7PeG+hrWxzOuNx2pA6j2iZCeyw8XiQVLcQ+FhZ0T8APoD4jLl8Mfeci3fzZFAXA1FzTiQMLlzs/tKO48jkQzFdreDhqML/oSHh9J3qMM4srLR0y1cJDyQg+x+GSC/ql93hU0m0SHCdUGbQKBgQC3brRgOcO50VxWKNi4xC4Sq7YRJWAHNvWRqhhC51+X30CTQUpLDuUgl7dd29wY/d6WUJ7t9Xhjj05l/IYtkVsA1DKE0ao4v5sjW/XC9GbnE4l08/RpA1LH4GoH2Q8VCfuhPZvWXHLqtIze0qSb5ECNwav7ZOQ3Q/K/My5QCpllKg=="
                , "json", "utf-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo0ppMjbnkJE7a4iyLELHlpEhoQoQFnfArZuY+pK6g/H5FMXH7jSn818a9JDyo2U2Shwc/mEbmSNL0pfebPmrZLJQm2MaYoQEh2XgweQ3Yx/o6vgBmkGmi4YGNp7yxaSLDZuf/LKGE6NWTEc0/NWvUFhuD6GLHAvb/3a5mpLu2zO/Nl6aIJibpq3g4bzDQRIBm0qXWv4QGhlGY+/CxtjG6B+cUDce1nFhlUam+vDL3eQ0PgRRwO2Tk7wKbiVuoC0oOzy2gItVxrI4OS6Hb5eETnQHswCygWU/iqMvxngD7aVXfqsKOizYdMCWra5v0OQmn1WaHiIg+ABjl2nWcAQkBQIDAQAB", "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest(); //创建API对应的request类
        request.setNotifyUrl("http://1x891966p2.51mypc.cn:50790/alipay/callback");
        request.setBizContent("{" +
                "\"out_trade_no\":\"202108170101010099\"," + //商户订单号
                "\"total_amount\":\"88.88\"," +
                "\"subject\":\"Iphone6 16G\"," +
                "\"store_id\":\"NJ_001\"," +
                "\"timeout_express\":\"90m\"}"); //订单允许的最晚付款时间
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
    }

    @Test
    void testCheckOrderPay() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2021000118653954", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCtC9Q3+cytzCCHUOruhEl50em5P3PBW9Xff5Pl5V8z/eOUbFUIkUs6Z9p856zMqP7i5pOLd7DlZ4UkLB1ft7zlgqml2BcVMkdrUDsKFr5zRSS3a7CrpVYS5NN9OG+Y/W/nlneNEEpyRGp7PqRNQPSxpamaKms3+d3cLxCf6jmG0vVGhX69b+5TPBg+YiqDkik+pU5ei5eDo3DmYTpgWug+lNI0NDiFwV7/9gBpAK6Huyys9WlSIOmW6DLsIDfVdOhxkdB5/YHsHwCG2/yf/Gs/jLCy/EMUQQLlkkq7ITIFALlQi1+4/epeUMmj0OoEV0bFDOCoX1zn5bms5EitvCQNAgMBAAECggEAc+WJQ9gtCnW9j4NVZVv6Lfm2FNj3eRg9vMugTYf65EJ1yrFnni5ZeQuXXLJ5nDTPST5usSj/tBVKKbjWD4ZqoDiL0MRSaWLcTHZWpGIpOeCkP6iDkeJRLKRPNP24WHndGfk/pp2zjqZdvp9swUTw3Y37coJYfwv9WHu5IbFox++mREolPsG8VMsk8yTu8Q8v97D0mqOz2adrOugIu/57qFPgNAtVEJgXeP3fPsUtNXiSJhjIRu+H2qUA8gkq6M6W9lR6tjsVhcYOmIC+9r9ImAa+VimPo1j9WgoCNjVptMrwjQC6ij2Pkxf2qzTbC8owaqrbxtIpCXYVNu9dIWg8LQKBgQDYkDEu/gzRBo/f96SN33RTUE4ggTwey6rq2J53zl/nmabvyZ4x0oVHxG5JMAhF1/+vWb3KoNQ74TTBIVkVJOet5nfqgS+NzRES1ahi3jtbzrwMDgovELPra7HIFwlRc5BtXZ63AiAP4zLuCppXEeffU6Dx5jA7EkDrhMfm2krbtwKBgQDMjvJiC1eYUwb5Eb4eaUdlnizVn4hAquMhluDN/WZTbhEgVsswAt5yJx1m6DmBC2Ipy75VbBaFO20ex6IxkXNaEVkUgRK0+x+oHtean3zQVqkk7YA2zfE0vKTQiWsQkrfPpl9+wafpNaxg/VEcG9ryIzU+7R9SSsNGhOOFo1RGWwKBgQDMf6zSETiNEcM+3RzYk8/G9Kgx7ZeWBQEK7y8FNy49B00qVZs6tO8CjC0Qk3jca1GYhgfAlgqT2N/2F8WFP+o+vskx+nerUZ/zxypQ4kXvNdNBFujq9LzRbErfBVHZRmV1wNglPK6GEid/vSdtXQ02SERM6O9WeLAxNpuGFVBnFQKBgQCHrnFbUwahBMFlR72BUneHEanPcGfh7PeG+hrWxzOuNx2pA6j2iZCeyw8XiQVLcQ+FhZ0T8APoD4jLl8Mfeci3fzZFAXA1FzTiQMLlzs/tKO48jkQzFdreDhqML/oSHh9J3qMM4srLR0y1cJDyQg+x+GSC/ql93hU0m0SHCdUGbQKBgQC3brRgOcO50VxWKNi4xC4Sq7YRJWAHNvWRqhhC51+X30CTQUpLDuUgl7dd29wY/d6WUJ7t9Xhjj05l/IYtkVsA1DKE0ao4v5sjW/XC9GbnE4l08/RpA1LH4GoH2Q8VCfuhPZvWXHLqtIze0qSb5ECNwav7ZOQ3Q/K/My5QCpllKg=="
                , "json", "utf-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo0ppMjbnkJE7a4iyLELHlpEhoQoQFnfArZuY+pK6g/H5FMXH7jSn818a9JDyo2U2Shwc/mEbmSNL0pfebPmrZLJQm2MaYoQEh2XgweQ3Yx/o6vgBmkGmi4YGNp7yxaSLDZuf/LKGE6NWTEc0/NWvUFhuD6GLHAvb/3a5mpLu2zO/Nl6aIJibpq3g4bzDQRIBm0qXWv4QGhlGY+/CxtjG6B+cUDce1nFhlUam+vDL3eQ0PgRRwO2Tk7wKbiVuoC0oOzy2gItVxrI4OS6Hb5eETnQHswCygWU/iqMvxngD7aVXfqsKOizYdMCWra5v0OQmn1WaHiIg+ABjl2nWcAQkBQIDAQAB", "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
        request.setBizContent("{" +
                "\"out_trade_no\":\"202108170101010099\"" +
                "}"); //设置业务参数
        AlipayTradeQueryResponse response = null;//通过alipayClient调用API，获得对应的response类
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.print(response.getBody());
    }

    @Test
    void testAliPagePay() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2021000118653954", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCtC9Q3+cytzCCHUOruhEl50em5P3PBW9Xff5Pl5V8z/eOUbFUIkUs6Z9p856zMqP7i5pOLd7DlZ4UkLB1ft7zlgqml2BcVMkdrUDsKFr5zRSS3a7CrpVYS5NN9OG+Y/W/nlneNEEpyRGp7PqRNQPSxpamaKms3+d3cLxCf6jmG0vVGhX69b+5TPBg+YiqDkik+pU5ei5eDo3DmYTpgWug+lNI0NDiFwV7/9gBpAK6Huyys9WlSIOmW6DLsIDfVdOhxkdB5/YHsHwCG2/yf/Gs/jLCy/EMUQQLlkkq7ITIFALlQi1+4/epeUMmj0OoEV0bFDOCoX1zn5bms5EitvCQNAgMBAAECggEAc+WJQ9gtCnW9j4NVZVv6Lfm2FNj3eRg9vMugTYf65EJ1yrFnni5ZeQuXXLJ5nDTPST5usSj/tBVKKbjWD4ZqoDiL0MRSaWLcTHZWpGIpOeCkP6iDkeJRLKRPNP24WHndGfk/pp2zjqZdvp9swUTw3Y37coJYfwv9WHu5IbFox++mREolPsG8VMsk8yTu8Q8v97D0mqOz2adrOugIu/57qFPgNAtVEJgXeP3fPsUtNXiSJhjIRu+H2qUA8gkq6M6W9lR6tjsVhcYOmIC+9r9ImAa+VimPo1j9WgoCNjVptMrwjQC6ij2Pkxf2qzTbC8owaqrbxtIpCXYVNu9dIWg8LQKBgQDYkDEu/gzRBo/f96SN33RTUE4ggTwey6rq2J53zl/nmabvyZ4x0oVHxG5JMAhF1/+vWb3KoNQ74TTBIVkVJOet5nfqgS+NzRES1ahi3jtbzrwMDgovELPra7HIFwlRc5BtXZ63AiAP4zLuCppXEeffU6Dx5jA7EkDrhMfm2krbtwKBgQDMjvJiC1eYUwb5Eb4eaUdlnizVn4hAquMhluDN/WZTbhEgVsswAt5yJx1m6DmBC2Ipy75VbBaFO20ex6IxkXNaEVkUgRK0+x+oHtean3zQVqkk7YA2zfE0vKTQiWsQkrfPpl9+wafpNaxg/VEcG9ryIzU+7R9SSsNGhOOFo1RGWwKBgQDMf6zSETiNEcM+3RzYk8/G9Kgx7ZeWBQEK7y8FNy49B00qVZs6tO8CjC0Qk3jca1GYhgfAlgqT2N/2F8WFP+o+vskx+nerUZ/zxypQ4kXvNdNBFujq9LzRbErfBVHZRmV1wNglPK6GEid/vSdtXQ02SERM6O9WeLAxNpuGFVBnFQKBgQCHrnFbUwahBMFlR72BUneHEanPcGfh7PeG+hrWxzOuNx2pA6j2iZCeyw8XiQVLcQ+FhZ0T8APoD4jLl8Mfeci3fzZFAXA1FzTiQMLlzs/tKO48jkQzFdreDhqML/oSHh9J3qMM4srLR0y1cJDyQg+x+GSC/ql93hU0m0SHCdUGbQKBgQC3brRgOcO50VxWKNi4xC4Sq7YRJWAHNvWRqhhC51+X30CTQUpLDuUgl7dd29wY/d6WUJ7t9Xhjj05l/IYtkVsA1DKE0ao4v5sjW/XC9GbnE4l08/RpA1LH4GoH2Q8VCfuhPZvWXHLqtIze0qSb5ECNwav7ZOQ3Q/K/My5QCpllKg=="
                , "json", "utf-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo0ppMjbnkJE7a4iyLELHlpEhoQoQFnfArZuY+pK6g/H5FMXH7jSn818a9JDyo2U2Shwc/mEbmSNL0pfebPmrZLJQm2MaYoQEh2XgweQ3Yx/o6vgBmkGmi4YGNp7yxaSLDZuf/LKGE6NWTEc0/NWvUFhuD6GLHAvb/3a5mpLu2zO/Nl6aIJibpq3g4bzDQRIBm0qXWv4QGhlGY+/CxtjG6B+cUDce1nFhlUam+vDL3eQ0PgRRwO2Tk7wKbiVuoC0oOzy2gItVxrI4OS6Hb5eETnQHswCygWU/iqMvxngD7aVXfqsKOizYdMCWra5v0OQmn1WaHiIg+ABjl2nWcAQkBQIDAQAB", "RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl("" +
                "");
        request.setReturnUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "20210817010101005");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
//bizContent.put("time_expire", "2022-08-01 22:00:00");

//// 商品明细信息，按需传入
//JSONArray goodsDetail = new JSONArray();
//JSONObject goods1 = new JSONObject();
//goods1.put("goods_id", "goodsNo1");
//goods1.put("goods_name", "子商品1");
//goods1.put("quantity", 1);
//goods1.put("price", 0.01);
//goodsDetail.add(goods1);
//bizContent.put("goods_detail", goodsDetail);

//// 扩展信息，按需传入
//JSONObject extendParams = new JSONObject();
//extendParams.put("sys_service_provider_id", "2088511833207846");
//bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    @Test
    void testAliPayOrder() {

//实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2021000118653954", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCtC9Q3+cytzCCHUOruhEl50em5P3PBW9Xff5Pl5V8z/eOUbFUIkUs6Z9p856zMqP7i5pOLd7DlZ4UkLB1ft7zlgqml2BcVMkdrUDsKFr5zRSS3a7CrpVYS5NN9OG+Y/W/nlneNEEpyRGp7PqRNQPSxpamaKms3+d3cLxCf6jmG0vVGhX69b+5TPBg+YiqDkik+pU5ei5eDo3DmYTpgWug+lNI0NDiFwV7/9gBpAK6Huyys9WlSIOmW6DLsIDfVdOhxkdB5/YHsHwCG2/yf/Gs/jLCy/EMUQQLlkkq7ITIFALlQi1+4/epeUMmj0OoEV0bFDOCoX1zn5bms5EitvCQNAgMBAAECggEAc+WJQ9gtCnW9j4NVZVv6Lfm2FNj3eRg9vMugTYf65EJ1yrFnni5ZeQuXXLJ5nDTPST5usSj/tBVKKbjWD4ZqoDiL0MRSaWLcTHZWpGIpOeCkP6iDkeJRLKRPNP24WHndGfk/pp2zjqZdvp9swUTw3Y37coJYfwv9WHu5IbFox++mREolPsG8VMsk8yTu8Q8v97D0mqOz2adrOugIu/57qFPgNAtVEJgXeP3fPsUtNXiSJhjIRu+H2qUA8gkq6M6W9lR6tjsVhcYOmIC+9r9ImAa+VimPo1j9WgoCNjVptMrwjQC6ij2Pkxf2qzTbC8owaqrbxtIpCXYVNu9dIWg8LQKBgQDYkDEu/gzRBo/f96SN33RTUE4ggTwey6rq2J53zl/nmabvyZ4x0oVHxG5JMAhF1/+vWb3KoNQ74TTBIVkVJOet5nfqgS+NzRES1ahi3jtbzrwMDgovELPra7HIFwlRc5BtXZ63AiAP4zLuCppXEeffU6Dx5jA7EkDrhMfm2krbtwKBgQDMjvJiC1eYUwb5Eb4eaUdlnizVn4hAquMhluDN/WZTbhEgVsswAt5yJx1m6DmBC2Ipy75VbBaFO20ex6IxkXNaEVkUgRK0+x+oHtean3zQVqkk7YA2zfE0vKTQiWsQkrfPpl9+wafpNaxg/VEcG9ryIzU+7R9SSsNGhOOFo1RGWwKBgQDMf6zSETiNEcM+3RzYk8/G9Kgx7ZeWBQEK7y8FNy49B00qVZs6tO8CjC0Qk3jca1GYhgfAlgqT2N/2F8WFP+o+vskx+nerUZ/zxypQ4kXvNdNBFujq9LzRbErfBVHZRmV1wNglPK6GEid/vSdtXQ02SERM6O9WeLAxNpuGFVBnFQKBgQCHrnFbUwahBMFlR72BUneHEanPcGfh7PeG+hrWxzOuNx2pA6j2iZCeyw8XiQVLcQ+FhZ0T8APoD4jLl8Mfeci3fzZFAXA1FzTiQMLlzs/tKO48jkQzFdreDhqML/oSHh9J3qMM4srLR0y1cJDyQg+x+GSC/ql93hU0m0SHCdUGbQKBgQC3brRgOcO50VxWKNi4xC4Sq7YRJWAHNvWRqhhC51+X30CTQUpLDuUgl7dd29wY/d6WUJ7t9Xhjj05l/IYtkVsA1DKE0ao4v5sjW/XC9GbnE4l08/RpA1LH4GoH2Q8VCfuhPZvWXHLqtIze0qSb5ECNwav7ZOQ3Q/K/My5QCpllKg=="
                , "json", "utf-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo0ppMjbnkJE7a4iyLELHlpEhoQoQFnfArZuY+pK6g/H5FMXH7jSn818a9JDyo2U2Shwc/mEbmSNL0pfebPmrZLJQm2MaYoQEh2XgweQ3Yx/o6vgBmkGmi4YGNp7yxaSLDZuf/LKGE6NWTEc0/NWvUFhuD6GLHAvb/3a5mpLu2zO/Nl6aIJibpq3g4bzDQRIBm0qXWv4QGhlGY+/CxtjG6B+cUDce1nFhlUam+vDL3eQ0PgRRwO2Tk7wKbiVuoC0oOzy2gItVxrI4OS6Hb5eETnQHswCygWU/iqMvxngD7aVXfqsKOizYdMCWra5v0OQmn1WaHiIg+ABjl2nWcAQkBQIDAQAB", "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl("http://1x891966p2.51mypc.cn:50790/alipay/callback"); // 接收支付结果的异步通知地址
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "20210817010101004");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");
        //商品类型 0-virtual 1-material
        bizContent.put("goods_type", 1);
        bizContent.put("product_code", "QUICK_MSECURITY_PAY");
        AlipayTradeAppPayResponse response = null;//获取需提交的form表单
        try {
            response = alipayClient.sdkExecute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        String submitFormData = response.getBody();
        System.out.println(submitFormData);
    }


}
