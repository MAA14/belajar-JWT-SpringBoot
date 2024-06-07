package maa.belajar_JWT_springboot.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return WebResponse.<AuthenticationResponse>builder()
                .data(service.register(request))
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    @PostMapping(
            path = "/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return WebResponse.<AuthenticationResponse>builder()
                .data(service.authenticate(request))
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
