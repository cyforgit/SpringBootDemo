package example.controller;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class alipayController {

    @RequestMapping("/alipay/callback")
    public Object alipayTest(@RequestBody String bodyStr) {
        System.out.println(bodyStr);
        return "success";
    }


}
