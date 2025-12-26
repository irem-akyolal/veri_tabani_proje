package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.OduncAlma;
import com.akilli.kutuphane.Service.OduncAlmaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odunc")
public class OduncAlmaController {

    private final OduncAlmaService oduncAlmaService;

    public OduncAlmaController(OduncAlmaService oduncAlmaService) {
        this.oduncAlmaService = oduncAlmaService;
    }

    // 1️⃣ Tüm ödünç kayıtlarını listele
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin/hepsi")
    public List<OduncAlma> getAllOdunc() {
        return oduncAlmaService.findAll();
    }

    // 2️⃣ ID ile ödünç kaydını getir
     @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<OduncAlma> getOduncById(@PathVariable Long id) {
        Optional<OduncAlma> odunc = oduncAlmaService.findById(id);
        return odunc.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3️⃣ Yeni ödünç kaydı ekle veya güncelle
   //@PreAuthorize("hasAnyRole('admin','ogrenci')")
    @PostMapping
   public ResponseEntity<OduncAlma> createOdunc(@RequestBody OduncAlma oduncAlma) { 
    OduncAlma saved = oduncAlmaService.save(oduncAlma);
    // Map yerine direkt kaydedilen nesneyi döndür
    return ResponseEntity.ok(saved);
}

    // 4️⃣ Ödünç kaydını sil
   @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOdunc(@PathVariable Long id) {
        if (oduncAlmaService.findById(id).isPresent()) {
            oduncAlmaService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5️⃣ Kullanıcının tüm ödünçlerini getir
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/kullanici/{kullaniciId}")
    public List<OduncAlma> getOduncByKullanici(@PathVariable Long kullaniciId) {
        return oduncAlmaService.findByKullaniciId(kullaniciId);
    }

    // 6️⃣ Kullanıcının aktif (iade edilmemiş) ödünçlerini getir
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/kullanici/{kullaniciId}/aktif")
    public List<OduncAlma> getAktifOduncByKullanici(@PathVariable Long kullaniciId) {
        return oduncAlmaService.findAktifOduncByKullaniciId(kullaniciId);
    }

    // 7️⃣ Tüm aktif (iade edilmemiş) ödünçleri listele
   @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/aktif")
    public List<OduncAlma> getAllAktifOdunc() {
        return oduncAlmaService.findAktifOdunc();
    }

   @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @PostMapping("/{oduncId}/iade")
    public ResponseEntity<Void> iadeEt(@PathVariable Long oduncId) {
    oduncAlmaService.iadeEt(oduncId);
    return ResponseEntity.ok().build();
}


}

