package maa.belajar_JWT_springboot.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Biar bisa di-inject
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter -> supaya setiap HTTP Request masuk ke sini dulu

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        /**
         * Note : Token JWT nanti akan kita beri kode awalan untuk memverifikasi bahwa itu kode JWT bikinan kita
         * Note : Token JWT yang asli akan ditaruh setelah kode awalan kita
         * Note : contoh kode awalan pada kasus ini adalah "Bearer "
         * */
        final String authHeader = request.getHeader("Authorization"); // Disini tempat token JWT nya
        final String jwtToken;
        final String userEmail;

        /** Filter 1 : Check is request has JWT token or not? */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return; // langsung return tanpa melanjutkan ke filterChain berikutnya
        }

        /** Filter 2 : Ekstrak JWT token menjadi data aslinya, Check apakah datanya cocok */
        jwtToken = authHeader.substring(7); // ambil token JWT yang asli setelah kata "Bearer " dan spasinya
        userEmail = JwtService.extractUsername(jwtToken); // ekstrak datanya
    }

}
