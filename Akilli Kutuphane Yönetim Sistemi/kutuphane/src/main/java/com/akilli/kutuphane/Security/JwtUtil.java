package com.akilli.kutuphane.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ⚠️ Üretim ortamında bu anahtarı ortam değişkenlerinden (Environment Variable) almalısın.
    private static final String SECRET_KEY = "supersecretkey12345supersecretkey12345678";

    // HMAC anahtarını oluşturur
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 1. TOKEN ÜRETME (Username ve Rol bilgisini gömer)
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", role); // Frontend'in admin/öğrenci ayrımı yapması için kritik

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Saat geçerli
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. TOKEN'DAN USERNAME (EMAIL) ÇIKARMA
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. TOKEN'DAN ROL ÇIKARMA
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }

    // 4. GENEL CLAIM OKUMA YARDIMCISI
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 5. TOKEN PARSE ETME (Doğrulama Anahtarı ile)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 6. TOKEN GEÇERLİLİK KONTROLÜ
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 7. SÜRE KONTROLÜ
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}




