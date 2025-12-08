package com.proje.kutuphane.Service;

import com.proje.kutuphane.Repository.OduncAlmaRepository;
import com.proje.kutuphane.entity.OduncAlma;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OduncAlmaService {

    private final OduncAlmaRepository oduncAlmaRepository;

    public OduncAlmaService(OduncAlmaRepository oduncAlmaRepository) {
        this.oduncAlmaRepository = oduncAlmaRepository;
    }

    // Tüm ödünçler
    public List<OduncAlma> getAllOduncler() {
        return oduncAlmaRepository.findAll();
    }

    // Kullanıcının tüm ödünçleri
    public List<OduncAlma> getKullaniciOduncleri(Long kullaniciId) {
        return oduncAlmaRepository.findByKullaniciKullaniciId(kullaniciId);
    }

    // Kitaba göre tüm ödünçler
    public List<OduncAlma> getKitapOduncleri(Long kitapId) {
        return oduncAlmaRepository.findByKitapKitapId(kitapId);
    }

    // Hala iade edilmemiş ödünçler
    public List<OduncAlma> getAktifOduncler() {
        return oduncAlmaRepository.findByGercekIadeTarihiIsNull();
    }

    // Kullanıcının aktif ödünçleri
    public List<OduncAlma> getKullaniciAktifOduncleri(Long kullaniciId) {
        return oduncAlmaRepository.findByKullaniciKullaniciIdAndGercekIadeTarihiIsNull(kullaniciId);
    }

    // Belirli tarihten sonra alınan ödünçler
    public List<OduncAlma> getOdunclerSonrasi(LocalDate tarih) {
        return oduncAlmaRepository.findByOduncTarihiAfter(tarih);
    }

    // Planlanan iade tarihi geçmiş ve hala iade edilmemiş ödünçler
    public List<OduncAlma> getGecikmisOduncler(LocalDate bugun) {
        return oduncAlmaRepository.findByPlanlananIadeTarihiBeforeAndGercekIadeTarihiIsNull(bugun);
    }
}





