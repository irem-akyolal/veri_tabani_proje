package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.Kullanici;
import com.proje.kutuphane.Service.KullaniciService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kullanici")
public class KullaniciController {

    private final KullaniciService kullaniciService;

    public KullaniciController(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }

    @GetMapping("/tum")
    public List<Kullanici> tumKullanicilar() {
        return kullaniciService.tumKullanicilar();
    }

    @PostMapping("/ekle")
    public Kullanici kullaniciEkle(@RequestBody Kullanici kullanici) {
        return kullaniciService.kullaniciEkle(kullanici);
    }

    @DeleteMapping("/sil/{id}")
    public void kullaniciSil(@PathVariable Long id) {
        kullaniciService.kullaniciSil(id);
    }

    @PutMapping("/guncelle/{id}")
    public Kullanici kullaniciGuncelle(@PathVariable Long id, @RequestBody Kullanici yeniKullanici) {
        return kullaniciService.kullaniciGuncelle(id, yeniKullanici);
    }

    @GetMapping("/{id}")
    public Kullanici kullaniciBul(@PathVariable Long id) {
        return kullaniciService.kullaniciBulById(id);
    }
}

