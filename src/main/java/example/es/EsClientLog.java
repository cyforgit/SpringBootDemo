package example.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(createIndex = true, indexName = "clilog-alias")
public class EsClientLog {
    @Id
    String id;
    @Field(type = FieldType.Auto)
    String clientId;
    @Field(type = FieldType.Auto)
    String traceId;
    @Field(type = FieldType.Auto)
    String type;
    @Field(type = FieldType.Auto)
    String keyword;
    @Field(type = FieldType.Auto)
    String msg;
    @Field(type = FieldType.Date)
    Long timestamp;


}
