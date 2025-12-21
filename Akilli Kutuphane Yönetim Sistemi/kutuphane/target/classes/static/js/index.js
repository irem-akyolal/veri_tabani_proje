const token = localStorage.getItem('jwtToken');
let currentUser = null;

// 1. UYGULAMA BAŞLATMA
async function init() {
    if (!token) { 
        window.location.href = 'login.html'; 
        return; 
    }

    try {
        // JWT Token içindeki payload'u çözüp email'i alıyoruz
        const payload = JSON.parse(atob(token.split('.')[1]));
        const email = payload.sub;

        // Kullanıcı bilgilerini backend'den çekiyoruz
        const response = await fetch(`/kullanici/eposta?eposta=${email}`, {
            headers: { 'Authorization': 'Bearer ' + token }
        });
        
        if (!response.ok) throw new Error("Kullanıcı oturumu geçersiz.");
        
        currentUser = await response.json();
        
        // Hoşgeldin mesajı ve Rol kontrolü
        document.getElementById('welcomeMsg').innerText = `👤 ${currentUser.kullaniciAd} ${currentUser.kullaniciSoyad} (${currentUser.rol})`;
        
        if(currentUser.rol === 'admin') {
            document.getElementById('adminPanel').classList.remove('hidden');
            loadAdminData();      // Dropdown'ları doldur (Yazar/Kategori)
            loadAllOduncAdmin();  // Tüm ödünçleri listele
            loadAllFinesAdmin();  // Tüm cezaları listele
        } else {
            document.getElementById('studentPanel').classList.remove('hidden');
        }
        
        loadBooks(); // Ortak kitap listesini yükle

    } catch (error) {
        console.error("Başlatma hatası:", error);
        logout();
    }
}

// 2. KİTAPLARI LİSTELE
function loadBooks() {
    fetch('/kitap', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.json())
    .then(data => {
        const body = document.getElementById('booksTableBody');
        body.innerHTML = '';
        data.forEach(book => {
            body.innerHTML += `
                <tr>
                    <td><strong>${book.kitapAd}</strong></td>
                    <td>${book.yazar.yazarAd} ${book.yazar.yazarSoyad}</td>
                    <td>${book.kategori.kategoriAd}</td>
                    <td>${book.mevcutStok} / ${book.toplamStok}</td>
                    <td>
                        <button class="btn-primary" onclick="oduncAl(${book.kitapId})" 
                            ${book.mevcutStok <= 0 ? 'disabled style="background:gray"' : ''}>
                            ${book.mevcutStok <= 0 ? 'Tükendi' : 'Ödünç Al'}
                        </button>
                    </td>
                </tr>`;
        });
    });
}

// 3. ADMIN: TÜM ÖDÜNÇLERİ VE CEZALARI TAKİP ET
async function loadAllOduncAdmin() {
    const res = await fetch('/odunc/admin/hepsi', { headers: { 'Authorization': 'Bearer ' + token } });
    if(res.ok) {
        const data = await res.json();
        const body = document.getElementById('adminOduncTable');
        body.innerHTML = data.map(o => `
            <tr>
                <td>${o.kullanici.kullaniciAd} ${o.kullanici.kullaniciSoyad}</td>
                <td>${o.kitap.kitapAd}</td>
                <td>${formatDate(o.oduncTarihi)}</td>
                <td>${formatDate(o.planlananIadeTarihi)}</td>
                <td><span class="badge ${o.durum === 'oduncte' ? 'badge-red' : 'badge-green'}">${o.durum}</span></td>
            </tr>
        `).join('');
    }
}

async function loadAllFinesAdmin() {
    const res = await fetch('/ceza', { headers: { 'Authorization': 'Bearer ' + token } });
    if(res.ok) {
        const data = await res.json();
        const body = document.getElementById('adminCezaTable');
        body.innerHTML = data.map(c => `
            <tr>
                <td>${c.oduncAlma.kullanici.kullaniciAd}</td>
                <td><b style="color:red">${c.cezaMiktari} TL</b></td>
                <td>${c.cezaMiktari > 0 ? '🔴 Gecikme' : '✅ Temiz'}</td>
                <td><span class="badge">${c.odemeDurumu}</span></td>
            </tr>
        `).join('');
    }
}

// 4. ADMIN: KULLANICI YÖNETİMİ
function listAllUsers() {
    fetch('/kullanici', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.json())
    .then(data => {
        let html = `<h3>👥 Sistemdeki Kullanıcılar</h3><table><thead><tr><th>Ad Soyad</th><th>E-posta</th><th>Rol</th><th>İşlem</th></tr></thead><tbody>`;
        data.forEach(u => {
            html += `<tr>
                <td>${u.kullaniciAd} ${u.kullaniciSoyad}</td>
                <td>${u.kullaniciEposta}</td>
                <td>${u.rol}</td>
                <td><button class="btn-danger" onclick="deleteUser(${u.kullaniciId})">Kullanıcıyı Sil</button></td>
            </tr>`;
        });
        document.getElementById('userListArea').innerHTML = html + "</tbody></table>";
    });
}

function deleteUser(id) {
    if(confirm("Bu kullanıcıyı kalıcı olarak silmek istiyor musunuz?")) {
        fetch(`/kullanici/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + token } })
        .then(() => listAllUsers());
    }
}

// 5. ÖĞRENCİ İŞLEMLERİ (Ödünç, İade, Cezalarım)
function oduncAl(bookId) {
    const oduncData = {
        durum: 'oduncte',
        oduncTarihi: new Date().toISOString().split('T')[0],
        planlananIadeTarihi: new Date(Date.now() + 14*24*60*60*1000).toISOString().split('T')[0], // 14 Günlük süre
        kullanici: { kullaniciId: currentUser.kullaniciId },
        kitap: { kitapId: bookId }
    };

    fetch('/odunc', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
        body: JSON.stringify(oduncData)
    }).then(res => {
        if(res.ok) { 
            alert("Kitap başarıyla ödünç alındı!"); 
            loadBooks(); 
            if(currentUser.rol === 'admin') loadAllOduncAdmin(); 
        } else {
            alert("Hata: Kitap ödünç alınamadı.");
        }
    });
}

function loadMyBorrows() {
    fetch(`/odunc/kullanici/${currentUser.kullaniciId}`, { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.json())
    .then(data => {
        let html = `<h3>Ödünç Geçmişim</h3><table><thead><tr><th>Kitap</th><th>Durum</th><th>İade Tarihi</th><th>İşlem</th></tr></thead><tbody>`;
        data.forEach(o => {
            html += `<tr>
                <td>${o.kitap.kitapAd}</td>
                <td><span class="badge ${o.durum === 'oduncte' ? 'badge-red' : 'badge-green'}">${o.durum}</span></td>
                <td>${formatDate(o.planlananIadeTarihi)}</td>
                <td>${o.durum === 'oduncte' ? `<button class="btn-success" onclick="iadeEt(${o.oduncId})">İade Et</button>` : 'İade Edildi'}</td>
            </tr>`;
        });
        document.getElementById('studentDisplayArea').innerHTML = html + "</tbody></table>";
    });
}

function iadeEt(id) {
    fetch(`/odunc/${id}/iade`, { method: 'POST', headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => { 
        if(res.ok) { 
            alert("Kitap iade edildi."); 
            loadMyBorrows(); 
            loadBooks(); 
        } 
    });
}

// 6. YÖNETİCİ: KAYIT FONKSİYONLARI (Kitap, Yazar, Kategori)
function saveKitap() {
    const payload = {
        kitapAd: document.getElementById('k_ad').value,
        toplamStok: parseInt(document.getElementById('k_stok').value),
        mevcutStok: parseInt(document.getElementById('k_stok').value),
        yazar: { yazarId: parseInt(document.getElementById('sel_yazar').value) },
        kategori: { kategoriId: parseInt(document.getElementById('sel_kat').value) }
    };
    fetch('/kitap', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
        body: JSON.stringify(payload)
    }).then(() => location.reload());
}

function saveYazar() {
    const payload = { 
        yazarAd: document.getElementById('y_ad').value, 
        yazarSoyad: document.getElementById('y_soyad').value 
    };
    fetch('/yazar', { 
        method: 'POST', 
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token }, 
        body: JSON.stringify(payload) 
    }).then(() => { 
        alert("Yazar eklendi"); 
        loadAdminData(); 
    });
}

function saveKategori() {
    const payload = { kategoriAd: document.getElementById('kat_ad').value };
    fetch('/kategori', { 
        method: 'POST', 
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token }, 
        body: JSON.stringify(payload) 
    }).then(() => { 
        alert("Kategori eklendi"); 
        loadAdminData(); 
    });
}

// 7. YARDIMCI ARAÇLAR
function loadAdminData() {
    // Yazar dropdown
    fetch('/yazar', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.json()).then(data => {
        const s = document.getElementById('sel_yazar');
        s.innerHTML = '<option value="">Yazar Seçiniz</option>';
        data.forEach(y => s.innerHTML += `<option value="${y.yazarId}">${y.yazarAd} ${y.yazarSoyad}</option>`);
    });
    // Kategori dropdown
    fetch('/kategori', { headers: { 'Authorization': 'Bearer ' + token } })
    .then(res => res.json()).then(data => {
        const s = document.getElementById('sel_kat');
        s.innerHTML = '<option value="">Kategori Seçiniz</option>';
        data.forEach(k => s.innerHTML += `<option value="${k.kategoriId}">${k.kategoriAd}</option>`);
    });
}

function formatDate(isoDate) {
    if(!isoDate) return "-";
    const date = new Date(isoDate);
    return date.toLocaleDateString('tr-TR');
}

function toggleArea(id) { document.getElementById(id).classList.toggle('hidden'); }

function logout() { 
    localStorage.removeItem('jwtToken'); 
    window.location.href = 'login.html'; 
}

// Uygulamayı başlat
init();