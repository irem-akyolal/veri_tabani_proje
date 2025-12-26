package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Yazar;
import com.akilli.kutuphane.Repository.YazarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YazarService {

    private final YazarRepository yazarRepository;

    public YazarService(YazarRepository yazarRepository) {
        this.yazarRepository = yazarRepository;
    }

    // Tüm yazarları getir
    public List<Yazar> findAll() {
        return yazarRepository.findAll();
    }

    // ID ile yazar getir
    public Optional<Yazar> findById(Long id) {
        return yazarRepository.findById(id);
    }

    // Yeni yazar ekle veya güncelle
    public Yazar save(Yazar yazar) {
        return yazarRepository.save(yazar);
    }

    // Yazar sil
    public void deleteById(Long id) {
        yazarRepository.deleteById(id);
    }

    // Ad veya soyada göre arama
    public List<Yazar> searchByName(String keyword) {
        return yazarRepository.findByYazarAdContainingIgnoreCaseOrYazarSoyadContainingIgnoreCase(keyword, keyword);
    }
}


