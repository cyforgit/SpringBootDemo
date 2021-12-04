package example.es;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
@ConfigurationProperties(prefix = "es")
public class EsConfig {


    String host;
    int port;
    String scheme;

    @PostConstruct
    void init() {
        System.out.println(this);
    }


}
