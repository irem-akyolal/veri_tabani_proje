package com.proje.kutuphane.Repository;

import com.proje.kutuphane.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KitapRepository extends JpaRepository<Kitap, Long> {

    // Kitap adı ile tam eşleşme
    List<Kitap> findByKitapAd(String kitapAd);

    // Kitap adı içinde geçen kısmı arama (büyük/küçük harf duyarsız)
    List<Kitap> findByKitapAdContainingIgnoreCase(String kitapAd);

    // Yazar ID ile kitapları bulma
    List<Kitap> findByYazarYazarId(Long yazarId);

    // Kategori ID ile kitapları bulma
    List<Kitap> findByKategoriKategoriId(Long kategoriId);

    // Alfabetik sıralama (kitap adına göre)
    List<Kitap> findAllByOrderByKitapAdAsc();
}

