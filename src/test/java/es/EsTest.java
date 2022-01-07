package es;

import example.App;
import example.es.EsDao;
import example.es.EsTestPo;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.temporal.Temporal;
import java.util.Optional;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
@Logger
public class EsTest {

    @Autowired
    ElasticsearchRestTemplate template;

    @Autowired
    EsDao esDao;

    @Test
    public void testInsert() {
        EsTestPo po = new EsTestPo("15", "李四", "3", 12, "5","中国李四","中国李四");
        esDao.save(po);
    }

    @Test
    public void testUpdate() {
        EsTestPo po = new EsTestPo("444", "2", "1", 11334, "999","法国张三","法国张三" );
        esDao.save(po);
    }

    @Test
    public void testCreateIndex() {
        System.out.println("创建索引");
        template.indexOps(IndexCoordinates.of("myproduct")).create();
    }

    @Test
    public void testDelIndex() {
        System.out.println("删除索引");
        template.indexOps(IndexCoordinates.of("product")).delete();
    }

    @Test
    public void testGet() {

        Optional<EsTestPo> po = esDao.findById("1");
        po.ifPresent(esTestPo -> System.out.println(esTestPo.toString()));

    }

    @Test
    public void testPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 3, sort);
        Page<EsTestPo> poPage = esDao.findAll(pageRequest);
        poPage.forEach(p -> System.out.println(p.toString()));
    }

    @Test
    public void testSearch() {

        CriteriaQuery query = new CriteriaQuery(new Criteria().and(new Criteria("name").matches("2")).and(new Criteria("price").lessThan(111)));
        System.out.println(query.getCriteria().toString());
        SearchHits<EsTestPo> search = template.search(query, EsTestPo.class);
        System.out.println(search.getTotalHits());
        search.stream().forEach(v -> System.out.println(v.toString()));
    }
}
