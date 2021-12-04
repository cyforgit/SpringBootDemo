package example.wechatpay;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.xml.internal.ws.message.ByteArrayAttachment;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.crypto.interfaces.PBEKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.Base64;
import java.util.concurrent.locks.Lock;

@Service
@Data
@Slf4j
public class WechatPayService {

    @Autowired
    WechatPayConfig wechatPayConfig;
    //下单签名 小程序和其他签名方式不同
    //小程序
    private static final int appTypeMiniApp = 1;
    private CloseableHttpClient closeableHttpClient;
    private AutoUpdateCertificatesVerifier verifier;
    private PrivateKey privateKey;

    //    @PostConstruct
    synchronized void init() throws UnsupportedEncodingException {
        if (closeableHttpClient == null) {
            PrivateKey privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wechatPayConfig.getPrivateKeyPath().getBytes(StandardCharsets.UTF_8)));
            this.privateKey = privateKey;
            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(wechatPayConfig.getMerchantId(), new PrivateKeySigner(wechatPayConfig.getMerchantSerialNo(), privateKey)),
                    wechatPayConfig.getApiV3Secret().getBytes(StandardCharsets.UTF_8));
            this.verifier = verifier;
            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(wechatPayConfig.getMerchantId(), wechatPayConfig.getMerchantSerialNo(), privateKey)
                    .withValidator(new WechatPay2Validator(verifier));
            closeableHttpClient = builder.build();
            log.info("wechat http client build success:{}", closeableHttpClient.toString());
        }
    }

    private CloseableHttpClient getHttpClient() throws UnsupportedEncodingException {
        if (closeableHttpClient != null) {
            return closeableHttpClient;
        } else {
            init();
            return closeableHttpClient;
        }
    }


    public Object closeOrder(String orderNo) {
        JSONObject res = new JSONObject();
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + orderNo + "/close");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        JSONObject body = new JSONObject();
        body.put("mchid", wechatPayConfig.getMerchantId());


        httpPost.setEntity(new StringEntity(body.toJSONString(), "UTF-8"));
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.NO_CONTENT.value()) {
                log.info("close order success");
            } else {
                log.error("close order failed ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    //查看支付订单支付状态
    public Object checkOrder(String orderNo) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/=" + orderNo + "?mchid=" + wechatPayConfig.getMerchantId());
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse response = this.closeableHttpClient.execute(httpGet);

            String bodyAsString = EntityUtils.toString(response.getEntity());
            JSONObject resp = JSONObject.parseObject(bodyAsString);

        } catch (Exception e) {
            log.error("checkOrder error:{}", e.toString());
            return null;
        }

        return null;
    }

    //订单回调
    public JSONObject notifyPay(String body, String headerTimestamp, String headerNonce, String headerSignature, String headerSerial) {
        log.info("notify pay body:{} timestamp:{},nonce :{},signature:{},serial:{}", body, headerTimestamp, headerNonce, headerSignature, headerSerial);
        JSONObject response = new JSONObject();
        StringBuffer buffer = new StringBuffer();
        buffer.append(headerTimestamp).append("\n");
        buffer.append(headerNonce).append("\n");
        buffer.append(body).append("\n");
        //验证签名
        if (verifier.verify(headerSerial, buffer.toString().getBytes(StandardCharsets.UTF_8), headerSignature)) {
            //解密数据
            AesUtil aesUtil = new AesUtil(wechatPayConfig.getApiV3Secret().getBytes(StandardCharsets.UTF_8));
            JSONObject bodyObj = JSONObject.parseObject(body);
            JSONObject resourceObj = bodyObj.getJSONObject("resource");
            if (resourceObj == null) {
                log.error("invalid resource info:{}", body);
            } else {
                String ciphertext = resourceObj.getString("ciphertext");
                String associatedData = resourceObj.getString("associated_data");
                String nonce = resourceObj.getString("nonce");
                try {
                    String callbackContent = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
                    //验证订单
                    verifyOrder();
                    //更新订单 todo
                    updateOrder();
                    //
                    response.put("code", "SUCCESS");
                    response.put("message", "成功");
                } catch (Exception e) {
                    log.error("decrypt resource error:{}", e.toString());
                    throw new RuntimeException("invalid decrypt resource");
                }


            }
        } else {
            response.put("code", "FAIL");
            response.put("message", "invalid sign");
        }


        return response;
    }

    public void verifyOrder() {
    }

    public void updateOrder() {
    }


    public JSONObject createOrder(String description, String outTradeNo, String attach, int total, String openId, int appType) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");

// 请求body参数
        JSONObject requestBody = new JSONObject();
        requestBody.put("appid", wechatPayConfig.getAppId());
        requestBody.put("mchid", wechatPayConfig.getMerchantId());
        requestBody.put("description", description);
        requestBody.put("out_trade_no", outTradeNo);
        //订单失效时间 支付单号两个小时失效 所以失效时间不能超过两个小时
//        requestBody.put("time_expire"time_expire)
        //附加信息可以在回调时携带返回
        if (!StringUtils.isEmpty(attach)) {
            requestBody.put("attach", attach);
        }
        requestBody.put("notify_url", wechatPayConfig.getPayNotifyCallbackUrl());
        JSONObject amount = new JSONObject();
        amount.put("total", total);
        amount.put("currency", "CNY");
        requestBody.put("amount", amount);
        JSONObject payer = new JSONObject();
        payer.put("openid", openId);
        requestBody.put("payer", payer);

//        String reqdata = "{"
//                + "\"amount\": {"
//                + "\"total\": 100,"
//                + "\"currency\": \"CNY\""
//                + "},"
//                + "\"mchid\": \"1900006891\","
//                + "\"description\": \"Image形象店-深圳腾大-QQ公仔\","
//                + "\"notify_url\": \"https://www.weixin.qq.com/wxpay/pay.php\","
//                + "\"payer\": {"
//                + "\"openid\": \"o4GgauE1lgaPsLabrYvqhVg7O8yA\"" + "},"
//                + "\"out_trade_no\": \"1217752501201407033233388881\","
//                + "\"goods_tag\": \"WXG\","
//                + "\"appid\": \"wxdace645e0bc2c424\"" + "}";
        StringEntity entity = new StringEntity(requestBody.toJSONString(), "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

//完成签名并执行请求
        log.info("createOrder request:{} body:{}", httpPost.toString(), requestBody.toJSONString());
        CloseableHttpResponse response = getHttpClient().execute(httpPost);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                log.info("success,return body = " + EntityUtils.toString(response.getEntity()));
                JSONObject responseObj = JSONObject.parseObject(responseBody);

                String prepayId = "";
                if (!(responseObj.get("prepay_id") instanceof String) || StringUtils.isEmpty(responseObj.get("prepay_id"))) {
                    throw new RuntimeException("invalid wechat create order response : invalid prepay_id");
                } else {
                    prepayId = (String) responseObj.get("prepay_id");
                }
                switch (appType) {
                    case appTypeMiniApp: {
                        StringBuffer signInfo = new StringBuffer();
                        String timestamp = System.currentTimeMillis() / 1000 + "";
                        String nonceStr = RandomUtil.randomString(32);

                        signInfo.append(wechatPayConfig.getAppId()).append("\n");
                        signInfo.append(timestamp).append("\n");
                        signInfo.append(nonceStr).append("\n");
                        signInfo.append("prepay_id=" + prepayId).append("\n");
                        log.info("signInfo:{}", signInfo.toString());
                        String sign = this.sign(signInfo.toString().getBytes(StandardCharsets.UTF_8));
                        log.info("sign :{}", sign);
                        responseObj.put("timeStamp", timestamp);
                        responseObj.put("nonceStr", nonceStr);
                        responseObj.put("package", "prepay_id=" + prepayId);
                        responseObj.put("signType", "RSA");
                        responseObj.put("paySign", sign);
                    }
                    default: {
                        log.info("invalid app type:{}", appType);
                    }
                }
                return responseObj;
            } else if (statusCode == 204) {
                log.info("success but no body");
                throw new RuntimeException("createOrder missing preorderid");
            } else {
                System.out.println("failed,resp code = " + statusCode + ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }


    }

    String sign(byte[] message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

}
