package maa.belajar_JWT_springboot.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @RequestMapping("/demo-controller")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello this is secured end-point, it means you still login");
    }

}
