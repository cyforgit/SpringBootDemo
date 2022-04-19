package example.controller;

import com.alipay.api.domain.VoucherDescDetail;
import example.asyncService.CommonAsyncService;
import example.bo.MyArrBody;
import example.es.EsTestPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@Slf4j
public class controller {
    @Autowired
    ElasticsearchRestTemplate template;

    @Autowired
    CommonAsyncService asyncService;

    @RequestMapping("/async")
    public Object async() {
        long timestamp = System.currentTimeMillis();

        Future<String> a = asyncService.getA(100);
        Future<String> b = asyncService.getB(100);

        try {
            System.out.println(a.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        try {
//            System.out.println(b.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return System.currentTimeMillis() - timestamp;


    }


    @RequestMapping("/es")
    public Object es() {
        CriteriaQuery query = new CriteriaQuery(new Criteria().and(new Criteria("name").is("李四")));
        System.out.println(query.getCriteria().toString());
        SearchHits<EsTestPo> search = template.search(query, EsTestPo.class);
        System.out.println(search.getTotalHits());
        search.stream().forEach(v -> System.out.println(v.toString()));
        return "s";
    }

    @RequestMapping("/home")
    ResponseEntity<Object> home(HttpServletResponse response) {
        return new ResponseEntity<>(new HashMap<String, String>() {{
            put("name", "张三");
        }}, HttpStatus.UNAUTHORIZED);


    }

    @RequestMapping("/myerror")
    String myerror() {
        throw new RuntimeException("this is my error");
    }

    @RequestMapping("/postArr")
    String myarr(@Validated @RequestBody MyArrBody myArrBody) {
        System.out.println(myArrBody.toString());
        return "success";
    }


}
