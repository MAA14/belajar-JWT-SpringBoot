package maa.belajar_JWT_springboot.config;

import lombok.RequiredArgsConstructor;
import maa.belajar_JWT_springboot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        /** Ini lambda expression yang bisa langsung @Override function dari Interface */
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        /** @NOTE: Code aslinya seperti ini dan @IMPORTANT UserDetailsService merupakan sebuah Interface bukan Class
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
         */
    }

    /**
     * AuthenticationProvider adalah Data Access Object (DAO) yang dapat mengambil data dari UserDetails seperti Username dan Password
     * Disini juga tempat terjadinya pengecekan username dan password
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Menentukan Class atau Object seperti apa yang ingin di Access datanya
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Menentukan Algorithma yang dapat men-Encode password User dari Database kita
        return authenticationProvider;
    }

    /**
     * Authentication Manager merupakan tempat kumpulan Method-Method
     * yang dapat membantu kita dalam melakukan Authenticate user berdasarkan Username dan Password
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Pake BCrypt untuk men-Encode password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
