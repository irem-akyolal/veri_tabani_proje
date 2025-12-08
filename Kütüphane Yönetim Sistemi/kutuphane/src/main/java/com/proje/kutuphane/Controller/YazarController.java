package com.proje.kutuphane.Controller;

import com.proje.kutuphane.entity.Yazar;
import com.proje.kutuphane.Service.YazarService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/yazar")
public class YazarController {

    private final YazarService yazarService;

    public YazarController(YazarService yazarService) {
        this.yazarService = yazarService;
    }

    @GetMapping("/tum")
    public List<Yazar> tumYazarlar() {
        return yazarService.tumYazarlar();
    }

    @PostMapping("/ekle")
    public Yazar yazarEkle(@RequestBody Yazar yazar) {
        return yazarService.yazarEkle(yazar);
    }

    @DeleteMapping("/sil/{id}")
    public void yazarSil(@PathVariable Long id) {
        yazarService.yazarSil(id);
    }

    @PutMapping("/guncelle/{id}")
    public Yazar yazarGuncelle(@PathVariable Long id, @RequestBody Yazar yeniYazar) {
        return yazarService.yazarGuncelle(id, yeniYazar);
    }

    @GetMapping("/{id}")
    public Yazar yazarBul(@PathVariable Long id) {
        return yazarService.yazarBulById(id);
    }
}

