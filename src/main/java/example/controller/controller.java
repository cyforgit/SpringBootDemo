package example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
