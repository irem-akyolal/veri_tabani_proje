package com.akilli.kutuphane.Controller;

import com.akilli.kutuphane.entity.Ceza;
import com.akilli.kutuphane.Service.CezaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ceza") // Tüm endpointler /ceza ile başlar
public class CezaController {

    private final CezaService cezaService;

    // Constructor injection ile CezaService inject edilir
    public CezaController(CezaService cezaService) {
        this.cezaService = cezaService;
    }

    // 1️⃣ Tüm cezaları listele
   @PreAuthorize("hasAnyRole('admin','ogrenci')")
    @GetMapping
    public List<Ceza> getAllCezalar() {
        return cezaService.findAll();
    }

    // 2️⃣ ID ile ceza getir
     @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<Ceza> getCezaById(@PathVariable Long id) {
        Optional<Ceza> ceza = cezaService.findById(id);
        return ceza.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build()); // Ceza yoksa 404 döner
    }

    // 3️⃣ Yeni ceza ekle veya mevcut cezayı güncelle
     @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Ceza createCeza(@RequestBody Ceza ceza) {
        return cezaService.save(ceza);
    }

    // 4️⃣ Ceza kaydını sil
     @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCeza(@PathVariable Long id) {
        if (cezaService.findById(id).isPresent()) {
            cezaService.deleteById(id);
            return ResponseEntity.ok().build(); // Silme başarılı
        } else {
            return ResponseEntity.notFound().build(); // Ceza yoksa 404
        }
    }
}

