const token = localStorage.getItem('jwtToken');
let currentUser = null;

// 1. UYGULAMA BAŞLATMA
async function init() {
    if (!token) { 
        window.location.href = 'login.html'; 
        return; 
    }

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const email = payload.sub;

        const response = await fetch(`/kullanici/eposta?eposta=${email}`, {
            headers: { 'Authorization': 'Bearer ' + token }
        });
        
        if (!response.ok) throw new Error("Oturum geçersiz.");
        
        currentUser = await response.json();
        
        document.getElementById('welcomeMsg').innerText = `👤 ${currentUser.kullaniciAd} ${currentUser.kullaniciSoyad} (${currentUser.rol})`;
        
        if(currentUser.rol === 'admin') {
            document.getElementById('adminPanel').classList.remove('hidden');
            const adminHeader = document.getElementById('adminActionHeader');
            if(adminHeader) adminHeader.classList.remove('hidden');
            
            loadAdminData();
            loadAllOduncAdmin();
            loadAllFinesAdmin();
            loadAllUsers();
        } else {
            document.getElementById('studentPanel').classList.remove('hidden');
            loadMyBorrows();
        }
        
        loadBooks();

    } catch (error) {
        console.error("Başlatma hatası:", error);
        logout();
    }
}

// 2. KİTAP LİSTELEME
function loadBooks() {
    fetch('/kitap', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        const body = document.getElementById('booksTableBody');
        if(!body) return;
        body.innerHTML = '';
        data.forEach(book => {
            let adminBtn = (currentUser && currentUser.rol === 'admin') 
                ? `<td><button class="btn-danger" onclick="deleteKitap(${book.kitapId})">Sil</button></td>` : '';
            
            body.innerHTML += `<tr>
                <td><strong>${book.kitapAd}</strong></td>
                <td>${book.yazar ? (book.yazar.yazarAd + " " + book.yazar.yazarSoyad) : "Bilinmiyor"}</td>
                <td>${book.kategori ? book.kategori.kategoriAd : "Yok"}</td>
                <td>${book.mevcutStok} / ${book.toplamStok}</td>
                <td><button class="btn-primary" onclick="oduncAl(${book.kitapId})" ${book.mevcutStok <= 0 ? 'disabled style="background:gray"' : ''}>Ödünç Al</button></td>
                ${adminBtn}
            </tr>`;
        });
    });
}

// 3. KULLANICI YÖNETİMİ
function loadAllUsers() {
    fetch('/kullanici', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        const body = document.getElementById('userTableBody');
        if(!body) return;
        body.innerHTML = data.map(u => `
            <tr>
                <td>${u.kullaniciAd} ${u.kullaniciSoyad}</td>
                <td>${u.eposta}</td>
                <td><span class="badge ${u.rol === 'admin' ? 'badge-red' : 'badge-green'}">${u.rol}</span></td>
                <td>
                    ${u.kullaniciId !== currentUser.kullaniciId ? 
                    `<button class="btn-danger" onclick="deleteUser(${u.kullaniciId})">Sil</button>` : '<em>Siz</em>'}
                </td>
            </tr>
        `).join('');
    });
}

function deleteUser(id) {
    if(confirm("Bu kullanıcıyı silmek istediğinize emin misiniz?")) {
        fetch(`/kullanici/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + token } })
        .then(res => {
            if(res.ok) { alert("Kullanıcı silindi"); loadAllUsers(); }
            else { alert("Hata: Silinemedi. Kullanıcının aktif ödünçleri olabilir."); }
        });
    }
}

// 4. ADMIN İŞLEMLERİ
function saveKitap() {
    const p = {
        kitapAd: document.getElementById('k_ad').value,
        toplamStok: parseInt(document.getElementById('k_stok').value),
        mevcutStok: parseInt(document.getElementById('k_stok').value),
        yazar: { yazarId: parseInt(document.getElementById('sel_yazar').value) },
        kategori: { kategoriId: parseInt(document.getElementById('sel_kat').value) }
    };
    fetch('/kitap', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
        body: JSON.stringify(p)
    }).then(res => { if(res.ok) { alert("Kitap eklendi"); loadBooks(); } });
}

function deleteKitap(id) {
    if(confirm("Kitabı silmek istediğinize emin misiniz?")) {
        fetch(`/kitap/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + token } })
        .then(res => res.ok ? loadBooks() : alert("Silinemedi (Ödünçte olabilir)"));
    }
}

function saveYazar() {
    const p = { yazarAd: document.getElementById('y_ad').value, yazarSoyad: document.getElementById('y_soyad').value };
    fetch('/yazar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
        body: JSON.stringify(p)
    }).then(res => { if(res.ok) { alert("Yazar eklendi"); loadAdminData(); } });
}

function deleteYazar(id) {
    if(confirm("Yazarı sil?")) {
        fetch(`/yazar/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + token } })
        .then(res => res.ok ? loadAdminData() : alert("Hata: Bu yazara bağlı kitaplar var."));
    }
}

function saveKategori() {
    const p = { kategoriAd: document.getElementById('kat_ad').value };
    fetch('/kategori', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
        body: JSON.stringify(p)
    }).then(res => { if(res.ok) { alert("Kategori eklendi"); loadAdminData(); } });
}

function deleteKategori(id) {
    if(confirm("Kategoriyi sil?")) {
        fetch(`/kategori/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + token } })
        .then(res => res.ok ? loadAdminData() : alert("Hata: Bu kategoriye bağlı kitaplar var."));
    }
}

function loadAdminData() {
    fetch('/yazar', { headers: { 'Authorization': 'Bearer ' + token } }).then(res => res.ok ? res.json() : []).then(data => {
        const sel = document.getElementById('sel_yazar');
        const list = document.getElementById('yazarMgmtList');
        if(sel) sel.innerHTML = '<option value="">Yazar Seç</option>' + data.map(y => `<option value="${y.yazarId}">${y.yazarAd} ${y.yazarSoyad}</option>`).join('');
        if(list) list.innerHTML = '<strong>Yazarlar:</strong>' + data.map(y => `<div class="mgmt-item"><span>${y.yazarAd} ${y.yazarSoyad}</span><button class="btn-danger" style="padding:2px 5px; font-size:10px;" onclick="deleteYazar(${y.yazarId})">Sil</button></div>`).join('');
    });
    fetch('/kategori', { headers: { 'Authorization': 'Bearer ' + token } }).then(res => res.ok ? res.json() : []).then(data => {
        const sel = document.getElementById('sel_kat');
        const list = document.getElementById('katMgmtList');
        if(sel) sel.innerHTML = '<option value="">Kategori Seç</option>' + data.map(k => `<option value="${k.kategoriId}">${k.kategoriAd}</option>`).join('');
        if(list) list.innerHTML = '<strong>Kategoriler:</strong>' + data.map(k => `<div class="mgmt-item"><span>${k.kategoriAd}</span><button class="btn-danger" style="padding:2px 5px; font-size:10px;" onclick="deleteKategori(${k.kategoriId})">Sil</button></div>`).join('');
    });
}

// 5. ÖDÜNÇ VE İADE İŞLEMLERİ
async function oduncAl(bookId) {
    const bugun = new Date();
    const oduncTarihiStr = bugun.toISOString().split('T')[0];
    const iadeSuresi = new Date();
    iadeSuresi.setDate(bugun.getDate() + 1); 
    const planlananIadeTarihiStr = iadeSuresi.toISOString().split('T')[0];

    const p = {
        durum: 'oduncte',
        oduncTarihi: oduncTarihiStr,
        planlananIadeTarihi: planlananIadeTarihiStr,
        kullanici: { kullaniciId: currentUser.kullaniciId },
        kitap: { kitapId: bookId }
    };

    try {
        const res = await fetch('/odunc', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify(p)
        });

        if(res.ok) {
            alert("Kitap başarıyla ödünç alındı!");
            loadBooks(); 
            if(currentUser.rol === 'admin') loadAllOduncAdmin();
            else loadMyBorrows(); 
        } else if (res.status === 403) {
            alert("Hata: Bu işlemi yapmaya yetkiniz yok (403).");
        } else {
            alert("İşlem başarısız oldu. Durum: " + res.status);
        }
    } catch (error) {
        console.error("Ödünç alma hatası:", error);
        alert("Sunucuya bağlanılamadı.");
    }
}

function iadeEt(id) {
    if(!confirm("İade etmek istediğinize emin misiniz?")) return;
    fetch(`/odunc/${id}/iade`, { 
        method: 'POST', 
        headers: { 'Authorization': 'Bearer ' + token } 
    }).then(res => {
        if(res.ok) {
            alert("İade başarılı!");
            loadBooks();
            if(currentUser.rol === 'admin') { loadAllOduncAdmin(); loadAllFinesAdmin(); }
            else { loadMyBorrows(); }
        } else {
            alert("İade işlemi başarısız (403/500).");
        }
    });
}

// 6. LİSTELEME FONKSİYONLARI
function loadMyBorrows() {
    console.log("Yükleniyor: ID ", currentUser.kullaniciId);
    
    fetch(`/odunc/kullanici/${currentUser.kullaniciId}`, { 
        headers: { 'Authorization': 'Bearer ' + token } 
    })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        console.log("Frontend'e ulaşan veri listesi:", data);
        const alan = document.getElementById('studentDisplayArea');
        if (!alan) return;

        let html = `<h3>📚 Ödünç Aldığım Kitaplar</h3>`;
        
        if (!data || data.length === 0) {
            html += '<p>Üzerinizde ödünç kitap bulunmuyor.</p>';
            alan.innerHTML = html;
            return;
        }

        // Tabloyu başlat
        html += `<table class="table">
                    <thead>
                        <tr>
                            <th>Kitap Adı</th>
                            <th>Ödünç Tarihi</th>
                            <th>Durum</th>
                            <th>İşlem</th>
                        </tr>
                    </thead>
                    <tbody>`;

        data.forEach(o => {
            // VERİ KONTROLLERİ (Hata almamak için)
            const kitapAd = (o.kitap && o.kitap.kitapAd) ? o.kitap.kitapAd : "Kitap Bilgisi Yok";
            const durumRaw = (o.durum) ? o.durum.toString().toLowerCase().trim() : "";
            const oduncId = o.oduncId;

            html += `<tr>
                <td><strong>${kitapAd}</strong></td>
                <td>${o.oduncTarihi || '-'}</td>
                <td>
                    <span class="badge ${durumRaw === 'oduncte' ? 'badge-red' : 'badge-green'}">
                        ${o.durum}
                    </span>
                </td>
                <td>
                    ${durumRaw === 'oduncte' ? 
                    `<button class="btn-success" onclick="iadeEt(${oduncId})">İade Et</button>` : 
                    '<span class="text-muted">İade Edildi</span>'}
                </td>
            </tr>`;
        });

        html += `</tbody></table>`;
        alan.innerHTML = html; // HTML'i tek seferde bas

        if(typeof loadMyFines === "function") loadMyFines();
    })
    .catch(err => {
        console.error("Liste basılırken hata oluştu:", err);
        document.getElementById('studentDisplayArea').innerHTML = "Veriler gösterilemedi.";
    });
}

function loadMyFines() {
    fetch(`/ceza/kullanici/${currentUser.kullaniciId}`, { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        const alan = document.getElementById('studentDisplayArea');
        let html = `<br><h3>💰 Cezalarım</h3>`;
        if(!data || data.length === 0) {
            html += '<p>Borcunuz bulunmamaktadır.</p>';
        } else {
            html += `<table><thead><tr><th>Kitap</th><th>Tutar</th><th>Durum</th></tr></thead><tbody>`;
            data.forEach(f => {
                html += `<tr>
                    <td>${f.oduncAlma && f.oduncAlma.kitap ? f.oduncAlma.kitap.kitapAd : 'Bilinmiyor'}</td>
                    <td>${f.cezaMiktari} TL</td>
                    <td>${f.odemeDurumu}</td>
                </tr>`;
            });
            html += "</tbody></table>";
        }
        alan.innerHTML += html;
    });
}

function loadAllOduncAdmin() {
    fetch('/odunc/admin/hepsi', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        const body = document.getElementById('adminOduncTable');
        if(body) body.innerHTML = data.map(o => 
            `<tr><td>${o.kullanici ? o.kullanici.kullaniciAd : '-'}</td><td>${o.kitap ? o.kitap.kitapAd : '-'}</td><td>${formatDate(o.oduncTarihi)}</td><td>${o.durum}</td></tr>`).join('');
    });
}

function loadAllFinesAdmin() {
    fetch('/ceza', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.ok ? res.json() : [])
    .then(data => {
        const body = document.getElementById('adminCezaTable');
        if(body) body.innerHTML = data.map(c => 
            `<tr><td>${c.oduncAlma && c.oduncAlma.kullanici ? c.oduncAlma.kullanici.kullaniciAd : '-'}</td><td>${c.cezaMiktari} TL</td><td>${c.cezaMiktari > 0 ? '🔴 Gecikmiş' : '✅ Zamanında'}</td><td>${c.odemeDurumu}</td></tr>`).join('');
    });
}

// 7. YARDIMCI ARAÇLAR
function toggleArea(id) { document.getElementById(id).classList.toggle('hidden'); }
function formatDate(iso) { return iso ? new Date(iso).toLocaleDateString('tr-TR') : "-"; }
function logout() { localStorage.removeItem('jwtToken'); window.location.href = 'login.html'; }

init();