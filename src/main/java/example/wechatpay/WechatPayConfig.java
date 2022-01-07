package example.wechatpay;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@Data
public class WechatPayConfig {

    String apiKey;
    //商户证书序列号
    String apiKeySerialNo;
    String privateKeyPath;
    //api v3 密钥
    String apiV3Secret;
    //商户号
    String merchantId;
    //商户序列号
    String merchantSerialNo;
    //回调地址
    String payNotifyCallbackUrl;
    String appId;
}
