package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.Kullanici;
import com.akilli.kutuphane.Service.KullaniciService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.akilli.kutuphane.dto.KullaniciResponse;


import java.util.List;


@RestController
@RequestMapping("/kullanici")
public class KullaniciController {

    private final KullaniciService kullaniciService;

    public KullaniciController(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }

    // 1️⃣ Tüm kullanıcıları listele
      @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Kullanici> getAllKullanicilar() {
        return kullaniciService.findAll();
    }

    // 2️⃣ ID ile kullanıcı getir
@PreAuthorize("hasAnyRole('admin','ogrenci')")
@GetMapping("/{id}")
public ResponseEntity<KullaniciResponse> getKullaniciById(@PathVariable Long id) {

    Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();

    String email = auth.getName();

    Kullanici currentUser =
            kullaniciService.findByEposta(email);
                    

    if (currentUser.getRol() == Kullanici.Rol.ogrenci &&
        !currentUser.getKullaniciId().equals(id)) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Kullanici kullanici = kullaniciService.findById(id); // Kutu açıldı, direkt nesne geldi
    return ResponseEntity.ok(KullaniciResponse.fromEntity(kullanici));

}

    // 4️⃣ Kullanıcı sil
     @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKullanici(@PathVariable Long id) {
        kullaniciService.deleteById(id); 
    return ResponseEntity.ok().build();
    }

    // 5️⃣ E-posta ile kullanıcı sorgulama
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/eposta")
    public ResponseEntity<Kullanici> getKullaniciByEposta(@RequestParam String eposta) {
        Kullanici kullanici = kullaniciService.findByEposta(eposta);
        return ResponseEntity.ok(kullanici);
    }
}

