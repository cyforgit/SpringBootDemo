import example.App;
import example.es.EsConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class EsTest {


    @Autowired
    EsConfig esConfig;

    @Test
    public void testEsCreateIndex() {
        System.out.println("123");
    }
}
