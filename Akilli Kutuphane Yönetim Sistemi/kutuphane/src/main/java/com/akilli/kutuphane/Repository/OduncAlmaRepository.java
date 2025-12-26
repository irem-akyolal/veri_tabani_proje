package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.OduncAlma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OduncAlmaRepository extends JpaRepository<OduncAlma, Long> {

    // Kullanıcının tüm ödünçleri
    List<OduncAlma> findByKullaniciKullaniciId(Long kullaniciId);

    // Hala iade edilmemiş ödünçler
    List<OduncAlma> findByGercekIadeTarihiIsNull();

    // Kullanıcının aktif ödünçleri (iade edilmemiş)
    List<OduncAlma> findByKullaniciKullaniciIdAndGercekIadeTarihiIsNull(Long kullaniciId);

    // Planlanan iade tarihi geçmiş ve hala iade edilmemiş ödünçler
    
}
