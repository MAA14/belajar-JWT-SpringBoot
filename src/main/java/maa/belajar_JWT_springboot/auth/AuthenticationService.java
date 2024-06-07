package maa.belajar_JWT_springboot.auth;

import lombok.RequiredArgsConstructor;
import maa.belajar_JWT_springboot.config.JwtService;
import maa.belajar_JWT_springboot.user.Role;
import maa.belajar_JWT_springboot.user.User;
import maa.belajar_JWT_springboot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user); // Membungkus data User menjadi JWT
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        /**
         * @FLOW_AUTHENTICATION_MANAGER
         * 1. AuthenticationManager akan mengirim Authentication Object (misal: UsernamePasswordAuthenticationToken) ke AuthenticationProvider
         * 2. AuthenticationProvider akan memanggil database dari table/repository yang di-Inject dengan @Autowired didalam Service ini
         * 3. AuthenticationProvider akan melakukan pengecekan apakah data yang dikirim Client (request) sesuai dengan yang ada di database
         * 4. AuthenticationProvider juga yang melakukan checkPw() dan hashPw() menggunakan BCrypt karena sudah kita setting pada ApplicationConfig.java
         *
         * Output :
         * 1. Apabila tidak cocok datanya maka akan melakukan Throw AuthenticationException
         * 2. Apabila berhasil maka The AuthenticationManager returns a valid Authentication object
         * with the authenticated flag set to true. This object may also contain additional user information
         * retrieved during the authentication process.
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.generateToken(user); // Membungkus data User menjadi JWT
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
