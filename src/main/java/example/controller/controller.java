package example.controller;

import cn.hutool.log.Log;
import example.bo.MyArrBody;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
public class controller {

    @RequestMapping("/home")
    String home() {
        return "Hello World!";
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
