package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.Kitap;
import com.proje.kutuphane.Service.KitapService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kitap")
public class KitapController {

    private final KitapService kitapService;

    public KitapController(KitapService kitapService) {
        this.kitapService = kitapService;
    }

    @GetMapping("/tum")
    public List<Kitap> tumKitaplar() {
        return kitapService.tumKitaplar();
    }

    @PostMapping("/ekle")
    public Kitap kitapEkle(@RequestBody Kitap kitap) {
        return kitapService.kitapEkle(kitap);
    }

    @DeleteMapping("/sil/{id}")
    public void kitapSil(@PathVariable Long id) {
        kitapService.kitapSil(id);
    }

    @PutMapping("/guncelle/{id}")
    public Kitap kitapGuncelle(@PathVariable Long id, @RequestBody Kitap yeniKitap) {
        return kitapService.kitapGuncelle(id, yeniKitap);
    }

    @GetMapping("/{id}")
    public Kitap kitapBul(@PathVariable Long id) {
        return kitapService.kitapBulById(id);
    }
}

