package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, Long> {

    // Tam ad ve soyad eşleşmesi ile yazar bulma
    Yazar findByYazarAdAndYazarSoyad(String ad, String soyad);

    // Ad veya soyad içinde geçen kısmı arama (büyük/küçük harf duyarsız)
    List<Yazar> findByYazarAdContainingIgnoreCaseOrYazarSoyadContainingIgnoreCase(String ad, String soyad);

    // Alfabetik sıralama (önce ad sonra soyad)
    List<Yazar> findAllByOrderByYazarAdAscYazarSoyadAsc();
}



