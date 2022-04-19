package example.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsLogDao extends ElasticsearchRepository<EsClientLog, String> {
}
