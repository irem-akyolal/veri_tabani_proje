package com.proje.kutuphane.Service;

import com.proje.kutuphane.entity.Kategori;
import com.proje.kutuphane.Repository.KategoriRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    // Tüm kategorileri listele
    public List<Kategori> tumKategoriler() {
        return kategoriRepository.findAll();
    }

    // Yeni kategori ekle
    public Kategori kategoriEkle(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    // Kategori sil
    public void kategoriSil(Long id) {
        kategoriRepository.deleteById(id);
    }

    // Kategori güncelle
    public Kategori kategoriGuncelle(Long id, Kategori yeniKategori) {
        Kategori mevcut = kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
        mevcut.setKategoriAd(yeniKategori.getKategoriAd());
        return kategoriRepository.save(mevcut);
    }

    // ID ile kategori bul
    public Kategori kategoriBulById(Long id) {
        return kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    // Tam isim ile kategori bul
    public Kategori kategoriBulByAd(String ad) {
        return kategoriRepository.findByKategoriAd(ad);
    }

    // Kategori adı var mı kontrol
    public boolean kategoriVarMi(String ad) {
        return kategoriRepository.existsByKategoriAd(ad);
    }

    // Kısmi arama (case-insensitive)
    public List<Kategori> kategoriAra(String ad) {
        return kategoriRepository.findByKategoriAdContainingIgnoreCase(ad);
    }

    // Alfabetik sıralama
    public List<Kategori> alfabetikKategoriler() {
        return kategoriRepository.findAllByOrderByKategoriAdAsc();
    }
}



