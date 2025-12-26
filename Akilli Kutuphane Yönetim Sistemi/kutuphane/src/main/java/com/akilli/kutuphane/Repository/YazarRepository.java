package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, Long> {
    // Yazar adını veya soyadını içeren kayıtları getirir (büyük/küçük harf duyarsız)
    List<Yazar> findByYazarAdContainingIgnoreCaseOrYazarSoyadContainingIgnoreCase(String yazarAd, String yazarSoyad);

}


