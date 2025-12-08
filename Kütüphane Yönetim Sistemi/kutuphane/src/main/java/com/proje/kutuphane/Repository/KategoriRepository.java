package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {

    // Tam isim ile kategori bulma
    Kategori findByKategoriAd(String kategoriAd);

    // Kategori adı var mı kontrol
    boolean existsByKategoriAd(String kategoriAd);

    // Kısmi arama (case-insensitive)
    List<Kategori> findByKategoriAdContainingIgnoreCase(String kategoriAd);

    // Alfabetik sıralama
    List<Kategori> findAllByOrderByKategoriAdAsc();
} 



