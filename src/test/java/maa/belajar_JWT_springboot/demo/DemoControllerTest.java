package maa.belajar_JWT_springboot.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import maa.belajar_JWT_springboot.auth.AuthenticationService;
import maa.belajar_JWT_springboot.auth.RegisterRequest;
import maa.belajar_JWT_springboot.auth.WebResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import maa.belajar_JWT_springboot.user.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testForbiddenWithoutToken() throws Exception {
        // Gk ada token tidak bisa akses link selain /api/v1/auth/**
        mockMvc.perform(
                get("/api/v1/demo/sayHello")
                        .header("token","")
        ).andExpectAll(
                status().isForbidden() // 403
        );
    }

    @Test
    void testDemoSuccess() throws Exception {
        // Bikin User baru
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("Muhammad Ahya")
                .lastname("Aulia")
                .email("muhammadahya252@gmail.com")
                .password("1234")
                .build();
        // Ambil token hasil register
        String jwtToken = authenticationService.register(registerRequest).getToken();

        mockMvc.perform(
                get("/api/v1/demo/sayHello")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken) // Kita set Header dan awalan token ada di JwtAuthenticationFilter
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals(response.getData(),"Hello World, this is secured end-point.");
            assertNotNull(response.getStatus());
            assertNull(response.getError());
        });
    }
}