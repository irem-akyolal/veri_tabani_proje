package com.akilli.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "kullanici")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici implements UserDetails{

    public enum Rol {
        ogrenci,
        admin
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kullanici_id")
    private Long kullaniciId;

    @Column(name = "kullanici_ad", nullable = false, length = 50)
    private String kullaniciAd;

    @Column(name = "kullanici_soyad", nullable = false, length = 50)
    private String kullaniciSoyad;

    @Column(name = "kullanici_eposta", nullable = false, unique = true, length = 100)
    private String kullaniciEposta;

    @Column(name = "kullanici_sifre", nullable = false)
    private String kullaniciSifre;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;


    
    @OneToMany(mappedBy = "kullanici")
    @JsonIgnore // sonsuz bir döngü olmasın diye
    private List<OduncAlma> oduncAlmalar;


     @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return kullaniciSifre;
    }

    @Override
    public String getUsername() {
        return kullaniciEposta;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
