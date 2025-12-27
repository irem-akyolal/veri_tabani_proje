package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Ceza;
import com.akilli.kutuphane.Repository.CezaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CezaService {

    private final CezaRepository cezaRepository;
    

    public CezaService(CezaRepository cezaRepository) {
        this.cezaRepository = cezaRepository;
       
    }


    // Tüm cezaları getirir
    public List<Ceza> findAll() {
        return cezaRepository.findAll();
    }

    // ID ile ceza getirir
    public Optional<Ceza> findById(Long id) {
        return cezaRepository.findById(id);
    }

    // Yeni ceza ekler veya mevcut cezayı günceller
    public Ceza save(Ceza ceza) {
        return cezaRepository.save(ceza);
    }

    // Ceza kaydını siler
    public void deleteById(Long id) {
        cezaRepository.deleteById(id);
    }


    public List<Ceza> findByKullaniciId(Long kullaniciId) {
    // Repository'deki @Query ile yazdığımız metodu çağırıyoruz
    return cezaRepository.findByKullaniciId(kullaniciId); // bunu yeni ekledim kullanıcı cezalarını görebilsin diye
}

    
}

