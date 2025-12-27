package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface KitapRepository extends JpaRepository<Kitap, Long> {
   List<Kitap> findBykitapAdContainingIgnoreCase(String kitapAd);

}
