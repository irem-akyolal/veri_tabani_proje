package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {

    // E-posta ile kullanıcı bulma
    Optional<Kullanici> findByKullaniciEposta(String eposta);

    // E-posta var mı kontrol
    boolean existsByKullaniciEposta(String eposta);

    
}

