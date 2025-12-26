package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.Security.JwtUtil;
import com.akilli.kutuphane.Service.KullaniciService;
import com.akilli.kutuphane.dto.LoginRequest;
import com.akilli.kutuphane.dto.RegisterRequest;
import com.akilli.kutuphane.entity.Kullanici;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KullaniciService kullaniciService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          KullaniciService kullaniciService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kullaniciService = kullaniciService;
    }

    // LOGIN
// AuthController.java - Güncellenmiş Hali

@PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest request) {
    try {
        // 1. Kimlik doğrulama yapılır
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. KULLANICIYI VERİTABANINDAN BUL (Rol bilgisini almak için)
        // KullaniciService içinde email ile kullanıcıyı bulan bir metodun olduğunu varsayıyorum
        Kullanici kullanici = kullaniciService.findByEposta(request.getEmail());

        // 3. TOKEN ÜRETİRKEN ROLÜ DE GÖNDER (Artık 2 parametre: email ve rol)
        // kullanici.getRol().name() -> Enum ise ismini String olarak gönderir
        String token = jwtUtil.generateToken(request.getEmail(), kullanici.getRol().name());
        
        return new LoginResponse(token);

    } catch (AuthenticationException e) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Hatalı email veya şifre"
        );
    }
}

    // REGISTER
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {

        Kullanici kullanici = new Kullanici();
        kullanici.setKullaniciAd(request.getAd());
        kullanici.setKullaniciSoyad(request.getSoyad());
        kullanici.setKullaniciEposta(request.getEmail());
        kullanici.setKullaniciSifre(request.getPassword());
        kullanici.setRol(Kullanici.Rol.ogrenci);

      // Şifre service katmanında hashlenir
        kullaniciService.register(kullanici);
    }

    // Login response
    static class LoginResponse {
        private final String token;
        public LoginResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
}



// Burada hem ğiriş hemde kayıt işlemi yapılyor