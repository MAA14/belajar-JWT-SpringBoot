package maa.belajar_JWT_springboot.demo;

import maa.belajar_JWT_springboot.auth.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @RequestMapping("/sayHello")
    public WebResponse<String> sayHello() {
        return WebResponse.<String>builder()
                .data("Hello World, this is secured end-point.")
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

}
