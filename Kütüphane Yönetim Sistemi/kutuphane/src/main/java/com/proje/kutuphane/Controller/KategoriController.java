package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.Kategori;
import com.proje.kutuphane.Service.KategoriService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kategori")
public class KategoriController {

    private final KategoriService kategoriService;

    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    @GetMapping("/tum")
    public List<Kategori> tumKategoriler() {
        return kategoriService.tumKategoriler();
    }

    @PostMapping("/ekle")
    public Kategori kategoriEkle(@RequestBody Kategori kategori) {
        return kategoriService.kategoriEkle(kategori);
    }

    @DeleteMapping("/sil/{id}")
    public void kategoriSil(@PathVariable Long id) {
        kategoriService.kategoriSil(id);
    }

    @PutMapping("/guncelle/{id}")
    public Kategori kategoriGuncelle(@PathVariable Long id, @RequestBody Kategori yeniKategori) {
        return kategoriService.kategoriGuncelle(id, yeniKategori);
    }

    @GetMapping("/{id}")
    public Kategori kategoriBul(@PathVariable Long id) {
        return kategoriService.kategoriBulById(id);
    }
}

