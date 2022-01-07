package example.es;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
@Document(indexName = "myproduct",useServerConfiguration = false)
public interface EsDao extends ElasticsearchRepository<EsTestPo, String> {
}
