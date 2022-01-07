//package example.es;
//
//
//import cn.hutool.core.util.ObjectUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import lombok.Data;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//@Data
//public class EsConfig extends AbstractElasticsearchConfiguration {
//
//
//    String host;
//    int port;
//    String scheme;
//
//    @PostConstruct
//    void init() {
//        System.out.println(this);
//    }
//
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }
//}
