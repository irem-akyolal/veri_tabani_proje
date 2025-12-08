package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.OduncAlma;
import com.proje.kutuphane.Service.OduncAlmaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/oduncler")
public class OduncAlmaController {

    private final OduncAlmaService oduncAlmaService;

    public OduncAlmaController(OduncAlmaService oduncAlmaService) {
        this.oduncAlmaService = oduncAlmaService;
    }

    // Tüm ödünçleri getir
    @GetMapping
    public ResponseEntity<List<OduncAlma>> getAllOduncler() {
        return ResponseEntity.ok(oduncAlmaService.getAllOduncler());
    }

    // Kullanıcının tüm ödünçleri
    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<OduncAlma>> getKullaniciOduncleri(@PathVariable Long kullaniciId) {
        return ResponseEntity.ok(oduncAlmaService.getKullaniciOduncleri(kullaniciId));
    }

    // Kitaba göre tüm ödünçler
    @GetMapping("/kitap/{kitapId}")
    public ResponseEntity<List<OduncAlma>> getKitapOduncleri(@PathVariable Long kitapId) {
        return ResponseEntity.ok(oduncAlmaService.getKitapOduncleri(kitapId));
    }

    // Aktif ödünçler (iade edilmemiş)
    @GetMapping("/aktif")
    public ResponseEntity<List<OduncAlma>> getAktifOduncler() {
        return ResponseEntity.ok(oduncAlmaService.getAktifOduncler());
    }

    // Kullanıcının aktif ödünçleri
    @GetMapping("/kullanici/{kullaniciId}/aktif")
    public ResponseEntity<List<OduncAlma>> getKullaniciAktifOduncleri(@PathVariable Long kullaniciId) {
        return ResponseEntity.ok(oduncAlmaService.getKullaniciAktifOduncleri(kullaniciId));
    }

    // Belirli tarihten sonra alınan ödünçler
    @GetMapping("/sonrasi")
    public ResponseEntity<List<OduncAlma>> getOdunclerSonrasi(@RequestParam String tarih) {
        LocalDate localDate = LocalDate.parse(tarih);
        return ResponseEntity.ok(oduncAlmaService.getOdunclerSonrasi(localDate));
    }

    // Planlanan iade tarihi geçmiş ve hala iade edilmemiş ödünçler
    @GetMapping("/gecikmis")
    public ResponseEntity<List<OduncAlma>> getGecikmisOduncler(@RequestParam String bugun) {
        LocalDate localDate = LocalDate.parse(bugun);
        return ResponseEntity.ok(oduncAlmaService.getGecikmisOduncler(localDate));
    }
}


