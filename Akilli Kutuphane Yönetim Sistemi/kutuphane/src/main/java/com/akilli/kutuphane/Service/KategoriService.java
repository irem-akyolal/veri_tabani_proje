package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Kategori;
import com.akilli.kutuphane.Repository.KategoriRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    // Tüm kategorileri getirir
    public List<Kategori> findAll() {
        return kategoriRepository.findAll();
    }

    // ID ile kategori getirir
    public Optional<Kategori> findById(Long id) {
        return kategoriRepository.findById(id);
    }

    // Yeni kategori ekler veya mevcut kategori günceller
    public Kategori save(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    // Kategoriyi siler
    public void deleteById(Long id) {
        kategoriRepository.deleteById(id);
    }

    // Tam isim ile kategori bulur
    public Kategori findByKategoriAd(String kategoriAd) {
        return kategoriRepository.findByKategoriAd(kategoriAd);
    }

    // Kategori adı var mı kontrol eder
    public boolean existsByKategoriAd(String kategoriAd) {
        return kategoriRepository.existsByKategoriAd(kategoriAd);
    }
}

