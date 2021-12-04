package example.advice;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TestAdvice {

    @ExceptionHandler
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception e) {

        System.out.println("Test advice handle exception:" + e.getMessage());
        return e.toString();

    }

}
