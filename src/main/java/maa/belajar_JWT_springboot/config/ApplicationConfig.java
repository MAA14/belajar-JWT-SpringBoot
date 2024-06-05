package maa.belajar_JWT_springboot.config;

import lombok.RequiredArgsConstructor;
import maa.belajar_JWT_springboot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
}
