package cn.tongdun.web.security.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by binsong.li on 2019-08-13 13:28
 */
@Controller
public class HelloController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/")
    public String hello(){
        return "hello";
    }
}
