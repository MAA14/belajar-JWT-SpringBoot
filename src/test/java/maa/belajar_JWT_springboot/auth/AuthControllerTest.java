package maa.belajar_JWT_springboot.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import maa.belajar_JWT_springboot.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerTest.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("Muhammad Ahya")
                .lastname("Aulia")
                .email("muhammmadahya252@gmail.com")
                .password("1234")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AuthenticationResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AuthenticationResponse>>() {});

            // Harus ada tokennya
            assertNotNull(response.getData().getToken());
            assertEquals(response.getStatus(),"200 OK");

            // Harusnya tidak error
            assertNull(response.getError());
        });
    }

    @Test
    void testAuthenticatedForbidden() throws Exception {
        // Harusnya Forbidden karena blm ada Usernya
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("muhammadahya252@gmail.com")
                .password("1234")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void testAuthenticatedWrongPassword() throws Exception {
        // Bikin User baru
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("Muhammad Ahya")
                .lastname("Aulia")
                .email("muhammadahya252@gmail.com")
                .password("1234")
                .build();
        authenticationService.register(registerRequest);

        // Bikin Request untuk Authenticate (login)
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("muhammadahya252@gmail.com")
                .password("12345-wrong")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    void testAuthenticatedSuccess() throws Exception {
        // Bikin User baru
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("Muhammad Ahya")
                .lastname("Aulia")
                .email("muhammadahya252@gmail.com")
                .password("1234")
                .build();
        authenticationService.register(registerRequest);

        // Bikin Request untuk Authenticate (login)
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("muhammadahya252@gmail.com")
                .password("1234")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AuthenticationResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AuthenticationResponse>>() {});

            assertEquals(response.getStatus(),"200 OK");
            assertNotNull(response.getData().getToken());
            assertNotEquals(response.getData().getToken(),"");

            // Harusnya tidak error
            assertNull(response.getError());

        });
    }

}