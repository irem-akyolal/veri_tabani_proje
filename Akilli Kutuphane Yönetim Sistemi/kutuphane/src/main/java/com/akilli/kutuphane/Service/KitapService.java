package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Kitap;
import com.akilli.kutuphane.Repository.KitapRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KitapService {

    private final KitapRepository kitapRepository;

    public KitapService(KitapRepository kitapRepository) {
        this.kitapRepository = kitapRepository;
    }

    // Tüm kitapları getirir
    public List<Kitap> findAll() {
        return kitapRepository.findAll();
    }

    // ID ile kitap getirir
    public Optional<Kitap> findById(Long id) {
        return kitapRepository.findById(id);
    }

    // Yeni kitap ekler veya mevcut kitabı günceller
    public Kitap save(Kitap kitap) {
        return kitapRepository.save(kitap);
    }

    // Kitabı siler
    public void deleteById(Long id) {
        kitapRepository.deleteById(id);
    }

    // Kitap başlığına göre arama (büyük/küçük harf duyarsız)
    public List<Kitap> searchBykitapAd(String kitapAd) {
        return kitapRepository.findBykitapAdContainingIgnoreCase(kitapAd);
    }


@Transactional
public void stokAzalt(Long kitapId) {
    Kitap kitap = kitapRepository.findById(kitapId)
            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));

    if (kitap.getMevcutStok() <= 0) {
        throw new RuntimeException("Kitap stokta yok");
    }

    kitap.setMevcutStok(kitap.getMevcutStok() - 1);
    kitapRepository.save(kitap);
}

@Transactional
public void stokArttir(Long kitapId) {
    Kitap kitap = kitapRepository.findById(kitapId)
            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));

    if (kitap.getMevcutStok() >= kitap.getToplamStok()) {
        throw new RuntimeException("Mevcut stok toplam stoğu aşamaz");
    }

    kitap.setMevcutStok(kitap.getMevcutStok() + 1);
    kitapRepository.save(kitap);
}




}




// stok işlemlerini burada yapacağım onun için kod ekliyorum