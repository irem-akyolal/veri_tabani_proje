package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.Yazar;
import com.akilli.kutuphane.Service.YazarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/yazar")
public class YazarController {

    private final YazarService yazarService;

    public YazarController(YazarService yazarService) {
        this.yazarService = yazarService;
    }

    // 1️⃣ Tüm yazarları listele
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Yazar> getAllYazarlar() {
        return yazarService.findAll();
    }

    // 2️⃣ ID ile yazar getir
     @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Yazar> getYazarById(@PathVariable Long id) {
        Optional<Yazar> yazar = yazarService.findById(id);
        return yazar.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3️⃣ Yeni yazar ekle veya güncelle
     @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Yazar createYazar(@RequestBody Yazar yazar) {
        return yazarService.save(yazar);
    }

    // 4️⃣ Yazar sil
     @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteYazar(@PathVariable Long id) {
        if (yazarService.findById(id).isPresent()) {
            yazarService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5️⃣ Ad veya soyada göre arama
    @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping("/search")
    public List<Yazar> searchYazar(@RequestParam String keyword) {
        return yazarService.searchByName(keyword);
    }
}

