package example.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public
class MyArrBody {
    @NotEmpty
    List<String> data;
}