package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Ceza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Long> {

    // Belirli bir ödünç kaydına ait ceza var mı kontrol et
    Optional<Ceza> findByOduncAlma_OduncId(Long oduncId);

   
    @Query("SELECT c FROM Ceza c WHERE c.oduncAlma.kullanici.kullaniciId = :kullaniciId")
List<Ceza> findByKullaniciId(@Param("kullaniciId") Long kullaniciId); // bunu yeni ekledim kullanıcılar cezalarını görebilsin diye

} 
