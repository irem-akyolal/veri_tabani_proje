package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.Ceza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Long> {

    // OduncAlma üzerinden ceza bulma
    Ceza findByOduncAlmaOduncId(Long oduncAlmaId);

    // Ödenmemiş cezaları listeleme
    List<Ceza> findByOdemeDurumu(com.proje.kutuphane.entity.Ceza.OdemeDurumu odemeDurumu);

    // Ödenmemiş cezaları ve kullanıcıya göre listeleme (OduncAlma ilişkisi üzerinden)
    List<Ceza> findByOduncAlmaKullaniciKullaniciIdAndOdemeDurumu(
            Long kullaniciId, com.proje.kutuphane.entity.Ceza.OdemeDurumu odemeDurumu);
} 
