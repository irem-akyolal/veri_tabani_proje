package com.proje.kutuphane.Service;

import com.proje.kutuphane.entity.Yazar;
import com.proje.kutuphane.Repository.YazarRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class YazarService {

    private final YazarRepository yazarRepository;

    public YazarService(YazarRepository yazarRepository) {
        this.yazarRepository = yazarRepository;
    }

    public List<Yazar> tumYazarlar() {
        return yazarRepository.findAll();
    }

    public Yazar yazarEkle(Yazar yazar) {
        return yazarRepository.save(yazar);
    }

    public void yazarSil(Long id) {
        yazarRepository.deleteById(id);
    }

    public Yazar yazarGuncelle(Long id, Yazar yeniYazar) {
        Yazar mevcut = yazarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı"));
        mevcut.setYazarAd(yeniYazar.getYazarAd());
        mevcut.setYazarSoyad(yeniYazar.getYazarSoyad());
        return yazarRepository.save(mevcut);
    }

    public Yazar yazarBulById(Long id) {
        return yazarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı"));
    }
}

