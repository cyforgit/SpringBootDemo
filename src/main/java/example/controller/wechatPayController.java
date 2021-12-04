package example.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import example.wechatpay.WechatPayService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

@RestController
public class wechatPayController {

//    @Value("${merchantId}")
    private String merchantId;
//    @Value("${merchantSerialNumber}")
    private String merchantSerialNumber;
//    @Value("${merchantPrivateKey}")
    private String merchantPrivateKey;
//    @Value("${wechatpayCertificates}")
    private String wechatpayCertificates;
    CloseableHttpClient httpClient;
    @Autowired
    WechatPayService wechatPayService;

    //每次回调时会携带证书序列号 时间戳、随机串和签名
    private void checkCallbackSign(HttpServletRequest request) {
        String headerTimestamp = request.getHeader("Wechatpay-Timestamp");
        String headerNonce = request.getHeader("Wechatpay-Nonce");

    }


    public Object callback(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        String headerTimestamp = request.getHeader("Wechatpay-Timestamp");
        String headerNonce = request.getHeader("Wechatpay-Nonce");
        String headerSignature = request.getHeader("Wechatpay-Signature");
        String headerSerial = request.getHeader("Wechatpay-Serial");
        return wechatPayService.notifyPay(buffer.toString(), headerTimestamp, headerNonce, headerSignature, headerSerial);
    }



    public void checkPayOrder() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/4200000889202103303311396384?mchid=1230000109");
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);
    }

    public void CreateOrder() throws Exception {
//请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");

// 请求body参数
        String reqdata = "{"
                + "\"amount\": {"
                + "\"total\": 100,"
                + "\"currency\": \"CNY\""
                + "},"
                + "\"mchid\": \"1900006891\","
                + "\"description\": \"Image形象店-深圳腾大-QQ公仔\","
                + "\"notify_url\": \"https://www.weixin.qq.com/wxpay/pay.php\","
                + "\"payer\": {"
                + "\"openid\": \"o4GgauE1lgaPsLabrYvqhVg7O8yA\"" + "},"
                + "\"out_trade_no\": \"1217752501201407033233388881\","
                + "\"goods_tag\": \"WXG\","
                + "\"appid\": \"wxdace645e0bc2c424\"" + "}";
        StringEntity entity = new StringEntity(reqdata, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

//完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode + ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

}
