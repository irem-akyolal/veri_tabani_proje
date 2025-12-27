package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {

    // Tam isim ile kategori bulma
    Kategori findByKategoriAd(String kategoriAd);

    // Kategori adı var mı kontrol
    boolean existsByKategoriAd(String kategoriAd);

} 
