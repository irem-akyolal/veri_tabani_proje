package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.Kategori;
import com.akilli.kutuphane.Service.KategoriService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kategori") // Tüm endpointler /kategori ile başlar
public class KategoriController {

    private final KategoriService kategoriService;

    // Constructor injection ile KategoriService inject edilir
    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    // 1️⃣ Tüm kategorileri listele
   @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping
    public List<Kategori> getAllKategoriler() {
        return kategoriService.findAll();
    }

    // 2️⃣ ID ile kategori getir
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Kategori> getKategoriById(@PathVariable Long id) {
        Optional<Kategori> kategori = kategoriService.findById(id);
        return kategori.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build()); // Kategori yoksa 404
    }

    // 3️⃣ Yeni kategori ekle veya mevcut kategoriyi güncelle
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<Kategori> createKategori(@RequestBody Kategori kategori) {
        if(kategoriService.existsByKategoriAd(kategori.getKategoriAd())) {
            return ResponseEntity.badRequest().build(); // Kategori adı zaten mevcut
        }
        Kategori savedKategori = kategoriService.save(kategori);
        return ResponseEntity.ok(savedKategori);
    }

    // 4️⃣ Kategoriyi sil
     @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKategori(@PathVariable Long id) {
        if(kategoriService.findById(id).isPresent()) {
            kategoriService.deleteById(id);
            return ResponseEntity.ok().build(); // Silme başarılı
        } else {
            return ResponseEntity.notFound().build(); // Kategori yoksa 404
        }
    }

    // 5️⃣ Tam isim ile kategori sorgulama
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/isim")
    public ResponseEntity<Kategori> getKategoriByAd(@RequestParam String kategoriAd) {
        Kategori kategori = kategoriService.findByKategoriAd(kategoriAd);
        if(kategori != null) {
            return ResponseEntity.ok(kategori);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
