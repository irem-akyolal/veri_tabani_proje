package com.proje.kutuphane.Service;

import com.proje.kutuphane.entity.Kitap;
import com.proje.kutuphane.Repository.KitapRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KitapService {

    private final KitapRepository kitapRepository;

    public KitapService(KitapRepository kitapRepository) {
        this.kitapRepository = kitapRepository;
    }

    // Tüm kitapları listele
    public List<Kitap> tumKitaplar() {
        return kitapRepository.findAll();
    }

    // Yeni kitap ekle
    public Kitap kitapEkle(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    // Kitap sil
    public void kitapSil(Long id) {
        kitapRepository.deleteById(id);
    }

    // Kitap güncelle
    public Kitap kitapGuncelle(Long id, Kitap yeniKitap) {
        Kitap mevcut = kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));

        mevcut.setKitapAd(yeniKitap.getKitapAd());
        mevcut.setToplamStok(yeniKitap.getToplamStok());
        mevcut.setMevcutStok(yeniKitap.getMevcutStok());
        mevcut.setYazar(yeniKitap.getYazar());
        mevcut.setKategori(yeniKitap.getKategori());

        return kitapRepository.save(mevcut);
    }

    // ID ile kitap bul
    public Kitap kitapBulById(Long id) {
        return kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));
    }

    // Kitap adına göre tam arama
    public List<Kitap> kitapBulByAd(String ad) {
        return kitapRepository.findByKitapAd(ad);
    }

    // Kitap adına göre kısmi arama
    public List<Kitap> kitapAra(String ad) {
        return kitapRepository.findByKitapAdContainingIgnoreCase(ad);
    }

    // Yazar ID ile kitapları bul
    public List<Kitap> kitaplariYazaraGoreBul(Long yazarId) {
        return kitapRepository.findByYazarYazarId(yazarId);
    }

    // Kategori ID ile kitapları bul
    public List<Kitap> kitaplariKategoriyeGoreBul(Long kategoriId) {
        return kitapRepository.findByKategoriKategoriId(kategoriId);
    }

    // Kitapları alfabetik sırala
    public List<Kitap> alfabetikKitaplar() {
        return kitapRepository.findAllByOrderByKitapAdAsc();
    }
}



