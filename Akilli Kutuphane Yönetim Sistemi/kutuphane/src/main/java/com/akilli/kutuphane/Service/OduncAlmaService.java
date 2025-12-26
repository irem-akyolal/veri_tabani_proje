package com.akilli.kutuphane.Service;


import com.akilli.kutuphane.entity.OduncAlma;
import com.akilli.kutuphane.entity.OduncAlma.OduncDurum;
import com.akilli.kutuphane.Repository.KullaniciRepository;
import com.akilli.kutuphane.Repository.OduncAlmaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OduncAlmaService {

    private final OduncAlmaRepository oduncAlmaRepository;
    private final KitapService kitapService;
    private final ApplicationEventPublisher eventPublisher;
    private final KullaniciRepository kullaniciRepository;

    public OduncAlmaService(OduncAlmaRepository oduncAlmaRepository,
                            KitapService kitapService,
                ApplicationEventPublisher eventPublisher,
            KullaniciRepository kullaniciRepository) {
        this.oduncAlmaRepository = oduncAlmaRepository;
        this.kitapService = kitapService;
         this.eventPublisher = eventPublisher;
         this.kullaniciRepository = kullaniciRepository;
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
    // 1. Kullanıcıyı tüm bilgileriyle (Rol dahil) veritabanından çek
    var kullanici = kullaniciRepository.findById(oduncAlma.getKullanici().getKullaniciId())
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

    // 2. Kitabı tüm bilgileriyle çek (İlişki hatası almamak için)
    var kitap = kitapService.findById(oduncAlma.getKitap().getKitapId())
            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));

    // 3. Stok azaltma
    kitapService.stokAzalt(kitap.getKitapId());

    // 4. Eksik bilgileri tamamla
    oduncAlma.setKullanici(kullanici); // Rol artık dolu!
    oduncAlma.setKitap(kitap);
    oduncAlma.setDurum(OduncDurum.oduncte);

    // 5. Kaydet ve döndür
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

           eventPublisher.publishEvent(oduncId);
    }
}






