package maa.belajar_JWT_springboot.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ErrorController {

    // AuthenticationManager atau Provider akan mengirim Throw AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<WebResponse<String>> authenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<String>builder()
                        .error(exception.getMessage())
                        .build()
                );
    }
}
