package com.akilli.kutuphane.Repository;

import com.akilli.kutuphane.entity.Ceza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Long> {

    // Belirli bir ödünç kaydına ait ceza var mı kontrol et
    Optional<Ceza> findByOduncAlma_OduncId(Long oduncId);

   
    

} 
