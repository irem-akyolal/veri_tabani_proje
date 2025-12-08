package com.proje.kutuphane.Service;

import com.proje.kutuphane.entity.Ceza;
import com.proje.kutuphane.Repository.CezaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CezaService {

    private final CezaRepository cezaRepository;

    public CezaService(CezaRepository cezaRepository) {
        this.cezaRepository = cezaRepository;
    }

    // Tüm cezaları listele
    public List<Ceza> tumCezalar() {
        return cezaRepository.findAll();
    }

    // Ceza ekle
    public Ceza cezaEkle(Ceza ceza) {
        return cezaRepository.save(ceza);
    }

    // Ceza sil
    public void cezaSil(Long cezaId) {
        cezaRepository.deleteById(cezaId);
    }

    // Ödünç alma işlemi üzerinden ceza bul
    public Ceza cezaBulByOduncId(Long oduncId) {
        return cezaRepository.findByOduncAlmaOduncId(oduncId);
    }

    // Ödenmemiş tüm cezaları listele
    public List<Ceza> odenmemisCezalar() {
        return cezaRepository.findByOdemeDurumu(Ceza.OdemeDurumu.bekliyor);
    }

    // Kullanıcıya ait ödenmemiş cezaları listele
    public List<Ceza> kullaniciOdenmemisCezalari(Long kullaniciId) {
        return cezaRepository.findByOduncAlmaKullaniciKullaniciIdAndOdemeDurumu(
                kullaniciId, Ceza.OdemeDurumu.bekliyor);
    }
}


