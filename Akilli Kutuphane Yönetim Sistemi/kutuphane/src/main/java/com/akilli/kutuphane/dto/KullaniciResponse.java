package com.akilli.kutuphane.dto;

import com.akilli.kutuphane.entity.Kullanici;

public class KullaniciResponse {

    private Long id;
    private String ad;
    private String soyad;
    private String email;
    private String rol;

    // 🔴 BU METOT ŞART
    public static KullaniciResponse fromEntity(Kullanici k) {
        KullaniciResponse dto = new KullaniciResponse();
        dto.id = k.getKullaniciId();
        dto.ad = k.getKullaniciAd();
        dto.soyad = k.getKullaniciSoyad();
        dto.email = k.getKullaniciEposta();
        dto.rol = k.getRol().name();
        return dto;
    }

    // getter'lar
    public Long getId() { return id; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}



// şifre dışarı görünmesin diye
