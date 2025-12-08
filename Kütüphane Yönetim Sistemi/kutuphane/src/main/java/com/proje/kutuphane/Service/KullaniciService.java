package com.proje.kutuphane.Service;

import com.proje.kutuphane.entity.Kullanici;
import com.proje.kutuphane.Repository.KullaniciRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;

    public KullaniciService(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    // Tüm kullanıcıları listele
    public List<Kullanici> tumKullanicilar() {
        return kullaniciRepository.findAll();
    }

    // Yeni kullanıcı ekle
    public Kullanici kullaniciEkle(Kullanici kullanici) {
        return kullaniciRepository.save(kullanici);
    }

    // Kullanıcı sil
    public void kullaniciSil(Long id) {
        kullaniciRepository.deleteById(id);
    }

    // Kullanıcı güncelle
    public Kullanici kullaniciGuncelle(Long id, Kullanici yeniKullanici) {
        Kullanici mevcut = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        mevcut.setKullaniciAd(yeniKullanici.getKullaniciAd());
        mevcut.setKullaniciSoyad(yeniKullanici.getKullaniciSoyad());
        mevcut.setKullaniciEposta(yeniKullanici.getKullaniciEposta());
        mevcut.setKullaniciSifre(yeniKullanici.getKullaniciSifre());
        mevcut.setRol(yeniKullanici.getRol());

        return kullaniciRepository.save(mevcut);
    }

    // ID ile kullanıcı bul
    public Kullanici kullaniciBulById(Long id) {
        return kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    // E-posta ile kullanıcı bul
    public Kullanici kullaniciBulByEposta(String eposta) {
        return kullaniciRepository.findByKullaniciEposta(eposta);
    }

    // E-posta ve şifre ile kullanıcı bul (login)
    public Kullanici kullaniciGiris(String eposta, String sifre) {
        return kullaniciRepository.findByKullaniciEpostaAndKullaniciSifre(eposta, sifre);
    }

    // Ad veya soyad ile arama
    public List<Kullanici> kullaniciAra(String ad, String soyad) {
        return kullaniciRepository.findByKullaniciAdContainingIgnoreCaseOrKullaniciSoyadContainingIgnoreCase(ad, soyad);
    }

    // Tam ad ve soyad eşleşmesi ile bul
    public List<Kullanici> kullaniciBulByAdSoyad(String ad, String soyad) {
        return kullaniciRepository.findByKullaniciAdAndKullaniciSoyad(ad, soyad);
    }

    // E-posta var mı kontrol
    public boolean epostaVarMi(String eposta) {
        return kullaniciRepository.existsByKullaniciEposta(eposta);
    }
}


