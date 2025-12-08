package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.OduncAlma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OduncAlmaRepository extends JpaRepository<OduncAlma, Long> {

    // Kullanıcının tüm ödünçleri
    List<OduncAlma> findByKullaniciKullaniciId(Long kullaniciId);

    // Kitaba göre tüm ödünçler
    List<OduncAlma> findByKitapKitapId(Long kitapId);

    // Hala iade edilmemiş ödünçler
    List<OduncAlma> findByGercekIadeTarihiIsNull();

    // Kullanıcının aktif ödünçleri (iade edilmemiş)
    List<OduncAlma> findByKullaniciKullaniciIdAndGercekIadeTarihiIsNull(Long kullaniciId);

    // Belirli tarihten sonra alınan ödünçler
    List<OduncAlma> findByOduncTarihiAfter(LocalDate tarih);

    // Planlanan iade tarihi geçmiş ve hala iade edilmemiş ödünçler
    List<OduncAlma> findByPlanlananIadeTarihiBeforeAndGercekIadeTarihiIsNull(LocalDate bugun);
}




