package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.Ceza;
import com.proje.kutuphane.Service.CezaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ceza")
public class CezaController {

    private final CezaService cezaService;

    public CezaController(CezaService cezaService) {
        this.cezaService = cezaService;
    }

    @GetMapping("/tum")
    public List<Ceza> tumCezalar() {
        return cezaService.tumCezalar();
    }

    @PostMapping("/ekle")
    public Ceza cezaEkle(@RequestBody Ceza ceza) {
        return cezaService.cezaEkle(ceza);
    }

    @DeleteMapping("/sil/{id}")
    public void cezaSil(@PathVariable Long id) {
        cezaService.cezaSil(id);
    }

    @GetMapping("/odunc/{oduncId}")
    public Ceza cezaBulByOdunc(@PathVariable Long oduncId) {
        return cezaService.cezaBulByOduncId(oduncId);
    }

    @GetMapping("/odenmemis")
    public List<Ceza> odenmemisCezalar() {
        return cezaService.odenmemisCezalar();
    }

    @GetMapping("/odenmemis/kullanici/{kullaniciId}")
    public List<Ceza> kullaniciOdenmemisCezalari(@PathVariable Long kullaniciId) {
        return cezaService.kullaniciOdenmemisCezalari(kullaniciId);
    }
}

