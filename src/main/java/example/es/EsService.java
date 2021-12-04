package example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EsService {

    RestHighLevelClient client;

    final
    EsConfig esConfig;

    public EsService(EsConfig esConfig) {
        this.esConfig = esConfig;
    }


    @PostConstruct
    public synchronized void init() {
        System.out.println("init es");
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esConfig.getHost(), esConfig.getPort(), esConfig.getScheme())));
    }

}
