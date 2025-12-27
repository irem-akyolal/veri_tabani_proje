package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Kullanici;
import com.akilli.kutuphane.Repository.KullaniciRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

   public KullaniciService(KullaniciRepository kullaniciRepository,
                            PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Tüm kullanıcıları getirir
    public List<Kullanici> findAll() {
        return kullaniciRepository.findAll();
    }

    // ID ile kullanıcı getirir
    public Kullanici findById(Long id) {
        return kullaniciRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı, ID: " + id));
    }

    // Yeni kullanıcı ekler veya mevcut kullanıcıyı günceller
    //public Kullanici save(Kullanici kullanici) {
        //return kullaniciRepository.save(kullanici);
    //} // kaldırılması öneriliyor.


    // Kayıt (REGISTER) - Şifreyi BCrypt ile encode eder
    public Kullanici register(Kullanici kullanici) {

        if (existsByEposta(kullanici.getKullaniciEposta())) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı");
        }

        kullanici.setKullaniciSifre(
                passwordEncoder.encode(kullanici.getKullaniciSifre())
        );

        if (kullanici.getRol() == null) {
            kullanici.setRol(Kullanici.Rol.ogrenci);
        }

        return kullaniciRepository.save(kullanici);
    }

    // Kullanıcıyı siler
    public void deleteById(Long id) {
        kullaniciRepository.deleteById(id);
    }

    // E-posta ile kullanıcı bulur
    public Kullanici findByEposta(String eposta) {
        return kullaniciRepository.findByKullaniciEposta(eposta)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + eposta));
    }

    // E-posta var mı kontrol eder (kayıtlı mı)
    public boolean existsByEposta(String eposta) {
        return kullaniciRepository.existsByKullaniciEposta(eposta);
    }
}

// buaraya da kayıt olma ve giriş eklendi ve şifre burada hashlendi.