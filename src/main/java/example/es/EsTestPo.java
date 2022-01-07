package example.es;

import cn.hutool.core.util.ObjectUtil;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.lang.model.element.TypeElement;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "myproduct")
public class EsTestPo {
    @Id
    String id;
    @Field(type = FieldType.Auto)
    String name;
    @Field(type = FieldType.Keyword)
    String type;

    @Field(type = FieldType.Long)
    long price;
    @Field(type = FieldType.Keyword, index = false)
    String images;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    String chsName;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    String testName;
}
