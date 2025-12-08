package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {

    // E-posta ile kullanıcı bulma
    Kullanici findByKullaniciEposta(String eposta);

    // E-posta ve şifre ile kullanıcı bulma (login)
    Kullanici findByKullaniciEpostaAndKullaniciSifre(String eposta, String sifre);

    // E-posta var mı kontrol
    boolean existsByKullaniciEposta(String eposta);

    // Ad veya soyad içinde geçen kısmı arama
    List<Kullanici> findByKullaniciAdContainingIgnoreCaseOrKullaniciSoyadContainingIgnoreCase(String ad, String soyad);

    // Tam ad ve soyad eşleşmesi
    List<Kullanici> findByKullaniciAdAndKullaniciSoyad(String ad, String soyad);
} 

