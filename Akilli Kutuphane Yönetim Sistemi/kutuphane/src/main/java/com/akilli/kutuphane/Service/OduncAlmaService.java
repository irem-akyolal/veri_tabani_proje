package com.akilli.kutuphane.Service;


import com.akilli.kutuphane.entity.OduncAlma;
import com.akilli.kutuphane.entity.OduncAlma.OduncDurum;
import com.akilli.kutuphane.entity.Ceza;
import com.akilli.kutuphane.Repository.OduncAlmaRepository;
import com.akilli.kutuphane.Repository.CezaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OduncAlmaService {

    private final OduncAlmaRepository oduncAlmaRepository;
    private final KitapService kitapService;
    private final CezaRepository cezaRepository;
    private final MailService mailService;

    public OduncAlmaService(OduncAlmaRepository oduncAlmaRepository,
                            KitapService kitapService,
                        CezaRepository cezaRepository,
                    MailService mailService) {
        this.oduncAlmaRepository = oduncAlmaRepository;
        this.kitapService = kitapService;
        this.cezaRepository = cezaRepository;
        this.mailService = mailService;
    }

    // 1️⃣ Tüm ödünç kayıtlarını getirir
    public List<OduncAlma> findAll() {
        return oduncAlmaRepository.findAll();
    }

    // 2️⃣ ID ile ödünç kaydını getirir
    public Optional<OduncAlma> findById(Long id) {
        return oduncAlmaRepository.findById(id);
    }

    // 3️⃣ Yeni ödünç kaydı ekler veya günceller + stok azaltma
    @Transactional
    public OduncAlma save(OduncAlma oduncAlma) {
        // Stok azaltma KitapService ile
        kitapService.stokAzalt(oduncAlma.getKitap().getKitapId());

        // KRİTİK: Veritabanına kaydetmeden önce durumu setle
    // Eğer enum sınıfın OduncDurum ise:
        oduncAlma.setDurum(OduncDurum.oduncte);

        // Ödünç kaydını kaydet
        return oduncAlmaRepository.save(oduncAlma);
    }

    // 4️⃣ Ödünç kaydını siler
    public void deleteById(Long id) {
        oduncAlmaRepository.deleteById(id);
    }

    // 5️⃣ Belirli bir kullanıcının tüm ödünçlerini getirir
    public List<OduncAlma> findByKullaniciId(Long kullaniciId) {
        return oduncAlmaRepository.findByKullaniciKullaniciId(kullaniciId);
    }

    // 6️⃣ Henüz iade edilmemiş tüm ödünçleri getirir
    public List<OduncAlma> findAktifOdunc() {
        return oduncAlmaRepository.findByGercekIadeTarihiIsNull();
    }

    // 7️⃣ Belirli bir kullanıcının aktif (iade edilmemiş) ödünçlerini getirir
    public List<OduncAlma> findAktifOduncByKullaniciId(Long kullaniciId) {
        return oduncAlmaRepository.findByKullaniciKullaniciIdAndGercekIadeTarihiIsNull(kullaniciId);
    }

    // 8️⃣ İade işlemi + stok artırma
    @Transactional
    public void iadeEt(Long oduncId) {
        OduncAlma odunc = oduncAlmaRepository.findById(oduncId)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı"));

        // İade tarihi ekle
        odunc.setGercekIadeTarihi(LocalDate.now());

        // KRİTİK: Durumu iade_edildi olarak güncelle
        odunc.setDurum(OduncDurum.iade_edildi);

        oduncAlmaRepository.save(odunc);

        // Stok artırma KitapService ile
        kitapService.stokArttir(odunc.getKitap().getKitapId());

         // Trigger ile oluşmuş ceza varsa mail gönder
        Optional<Ceza> cezaOpt = cezaRepository.findByOduncAlma_OduncId(oduncId);
        cezaOpt.ifPresent(ceza -> mailService.sendCezaMail(ceza));
    }
}






