package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.Kitap;
import com.akilli.kutuphane.Service.KitapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kitap") // Tüm endpointler /kitap ile başlar
public class KitapController {

    private final KitapService kitapService;

    // Constructor injection ile KitapService inject edilir
    public KitapController(KitapService kitapService) {
        this.kitapService = kitapService;
    }

    // 1️⃣ Tüm kitapları listele
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping
    public List<Kitap> getAllKitaplar() {
        return kitapService.findAll();
    }

    // 2️⃣ ID ile kitap getir
     @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Kitap> getKitapById(@PathVariable Long id) {
        Optional<Kitap> kitap = kitapService.findById(id);
        return kitap.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build()); // Kitap yoksa 404 döner
    }

    // 3️⃣ Yeni kitap ekle veya mevcut kitabı güncelle
      @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Kitap createKitap(@RequestBody Kitap kitap) {
        return kitapService.save(kitap);
    }

    // 4️⃣ Kitap sil
     @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKitap(@PathVariable Long id) {
        if (kitapService.findById(id).isPresent()) {
            kitapService.deleteById(id);
            return ResponseEntity.ok().build(); // Silme başarılı
        } else {
            return ResponseEntity.notFound().build(); // Kitap yoksa 404
        }
    }

    // 5️⃣ Başlığa göre kitap arama (büyük/küçük harf duyarsız)
     @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/search")
    public List<Kitap> searchKitap(@RequestParam String kitapAd) {
        return kitapService.searchBykitapAd(kitapAd);
    }
}

