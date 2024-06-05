package maa.belajar_JWT_springboot.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    // Ini keynya pake Random Generator dari "https://generate-random.org/encryption-key-generator?count=1&bytes=256&cipher=aes-256-cbc&string=&password="
    private static final String SECRET_KEY = "6imUScP5CgJImn6GUNyj+akb4hJmJ883JBEchvQ1V5fag8G3tD5ZnPJSV+Ubyr2czN+F11lv1BfISoS4nMccDZlKqtzLbSp4Bqzfaovtffz10UyvXeoPhQ3VOFOoL2pkD5CzO1GVsVvipA5gpkrrIAmEjPnZalpEQQ8Xuw0JSiL6ogt6tyI5qAHCoJNHgsi0EHfVo07hVIWXSgFC44PzkaW2YtSe3eRft9N2AZMR21xsqyHGrzZ5Yce+0zmiSVTydt9V1emz1csi22NjbWlYmYk4ovDNk/brNDOpcK1oLfVoRDaFgkX/TOKjYfyhfbyNql0tHgysL4md8YehSs+9OcTcNOPEzeX4DhrnWzDueqQ=";

    /** TO DO TOMORROW belajar Claims::getSubject dan Function<Claims, T> dari java.util.function */
    public String extractUsername(String jwtToken) {
        return extractClaim(
                jwtToken, /** Input: String = "token-jwt" */
                Claims::getSubject /** Input: Claims::method() ||  (Claims claims) -> claims.method() */
                // Parameter (Claims claims) didapat dari logic method extractClaim, jadi kita hanya perlu memanggil methodnya
                // Kita juga bisa melakukan filtering atau logic tambahan pada object Claims di method ini, dengan syarat harus punya return data
        );
    }

    /** Method untuk mengekstrak satu data aja dalam JWT -> data = claims */
    public <T> T extractClaim(
            String jwtToken, /** Input: String = "token-jwt" */
            // Parameter ini harus berupa fungsi dengan Param input harus object Claims dan harus punya return <T>
            Function<Claims, T> claimsResolver  /** Input: method(Claims claims) -> claims.method() */
    ) {
        final Claims claims = extractAllClaims(jwtToken); // Buat object Claims
        return claimsResolver.apply(claims); // Aktifkan method yang ada pada parameter claimsResolver
    }

    /** Method untuk mengekstrak seluruh data dalam JWT token -> data = Claims */
    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken) // Menguraikan Claims JWS
                .getBody(); // Disini kita bisa getHeader dan signature
    }

    /** Note:
     * Decode Sign Key harus memperhatikan Algoritma apa yang digunakan oleh JWT kita,
     * pada kasus ini kita menggunakan HS256 dan Encryption Code 256 bit
     * */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // Karena kita make algoritma HSA256 jadi pake method hmacSha
    }
}
