# ✨ WiSee Android — Fun Learning for Kids

Aplikasi **Android** edukasi interaktif untuk anak-anak yang mengubah tulisan tangan menjadi gambar ilustrasi AI dan suara edukatif bahasa Indonesia.

---

## 🚀 Cara Buka dan Build di Android Studio

### 1. Persyaratan
- **Android Studio Hedgehog** (2023.1.1) atau lebih baru
- **JDK 17** (sudah include di Android Studio)
- **Android SDK 34**
- Device/Emulator Android (Android 11.0+)

### 2. Cara Build & Run
```
1. Buka Android Studio
2. File → Open → pilih folder WiSeeAndroid/
3. Tunggu Gradle sync selesai (~2-5 menit, memerlukan internet saat pertama kali)
4. Klik ▶ Run atau Shift+F10
```

---

## 📦 Dataset KNN (Built-in, Tidak Perlu Upload)

Dataset sudah **ter-bundle** di dalam APK:
```
app/src/main/assets/dataset/az_emnist_combined.bin
```
- **62.400 sampel**
- Diekstrak dari dataset A-Z Handwritten Kaggle
- Format: `label(0-25), pixel1, ..., pixel784` (28×28 grayscale)
- Ukuran terkompresi: **~1.8 MB**
- Dimuat otomatis saat aplikasi pertama dibuka dengan progress bar loading screen yang interaktif.

---

## 🏗️ Struktur Project (Terupdate)

```
WiSeeAndroid/
├── app/src/main/
│   ├── java/com/wisee/
│   │   ├── WiSeeApplication.java          ← Application class
│   │   ├── model/
│   │   │   ├── User.java                  ← Room entity
│   │   │   ├── WordHistory.java           ← Room entity
│   │   │   └── KnowledgeFrame.java        ← Frame pengetahuan
│   │   ├── service/
│   │   │   ├── AppDatabase.java           ← Room database
│   │   │   ├── UserDao.java               ← DAO user
│   │   │   ├── WordHistoryDao.java        ← DAO riwayat
│   │   │   ├── DatabaseService.java       ← Facade DB operations
│   │   │   ├── SessionService.java        ← Singleton sesi login
│   │   │   ├── KnnService.java            ← KNN classifier
│   │   │   └── KnowledgeFrameService.java ← Basis pengetahuan (180 kata)
│   │   ├── ui/
│   │   │   ├── activities/
│   │   │   │   ├── SplashActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── RegisterActivity.java  ← Redirect ke Login setelah daftar
│   │   │   │   ├── MainActivity.java      ← OCR + Kosakata utama + Dialog kamera
│   │   │   │   ├── ResultActivity.java    ← Detail hasil + AI illustration + Safe handler
│   │   │   │   ├── HistoryActivity.java   ← Riwayat belajar
│   │   │   │   └── VocabActivity.java     ← Browser kosakata
│   │   │   ├── adapters/
│   │   │   │   ├── VocabAdapter.java
│   │   │   │   └── HistoryAdapter.java
│   │   │   └── views/
│   │   │       └── DrawingView.java       ← Custom canvas menggambar
│   │   └── util/
│   │       ├── HashUtil.java              ← SHA-256
│   │       ├── ImageProcessor.java        ← Grayscale→Threshold→Segmentasi
│   │       ├── NegativeWordFilter.java    ← Filter kata buruk + pesan ramah [TERUPDATE]
│   │       └── TtsUtil.java              ← Android TextToSpeech (offline)
│   │
│   ├── assets/dataset/
│   │   └── az_emnist_combined.bin         ← Dataset KNN built-in
│   │
│   └── res/
│       ├── layout/                        ← 9 layout XML (Tampilan terupdate & premium)
│       ├── drawable/                      ← Shape & asset logo (ic_wisee_logo)
│       ├── xml/
│       │   └── file_paths.xml             ← Provider file untuk kamera [BARU]
│       └── values/                        ← colors, strings, themes, dimens
│
├── build.gradle                           ← Dependencies
└── settings.gradle
```

---

## 🤖 Cara Kerja Sistem

### Alur Pengenalan Tulisan & Gambar
```
User menggambar di DrawingView / Ambil Foto (Kamera / Galeri)
                        ↓
                 Preprocessing
      (Grayscale → Otsu Threshold → Segmentasi)
                        ↓
            KnnService.predict() (K=5)
                        ↓
            Pengecekan Kata Negatif
          (NegativeWordFilter.isBlocked)
          ↙                          ↘
  [YA: Diblokir]              [TIDAK: Diizinkan]
         ↓                           ↓
Tampilkan Pesan Ramah       KnowledgeFrameService
(Blokir Gambar & Audio)              ↓
                       Ilustrasi AI (Pollinations FLUX)
                                     +
                       TtsUtil.speak (Suara Offline)
```

### 1. K-Nearest Neighbors (KNN)
- **K = 5** tetangga terdekat dengan Euclidean distance 784 dimensi.
- Dimuat otomatis dari file bin di thread latar belakang demi menjaga performa aplikasi tetap mulus.

### 2. Sistem Filter Kata Negatif
Aplikasi memisahkan sistem kata negatif dari basis data utama agar konten buruk tidak muncul di pencarian atau kategori belajar:
- **Independent Filtering**: Semua kata negatif dipusatkan di `NegativeWordFilter.java`.
- **Aman dari False Positive**: Menggunakan aturan minimal 3 karakter untuk mendeteksi kata negatif, sehingga input huruf tunggal (seperti "A") tidak akan terblokir secara keliru.
- **Smart Matching**: Mencocokkan input yang mengandung unsur kata negatif secara aman tanpa memblokir kata baik yang mirip.
- **Friendly Redirection**: Menampilkan dialog edukatif ramah yang memotivasi anak untuk menulis kata positif, serta mematikan fitur TTS dan AI generator untuk kata negatif.

### 3. Pilihan Input Kamera & Galeri
Tombol kamera di halaman utama kini mendukung dual-action dialog:
- **Ambil Foto Langsung**: Membuka kamera bawaan menggunakan `FileProvider` untuk keamanan penuh.
- **Pilih dari Galeri**: Memilih gambar tulisan tangan langsung dari media penyimpanan lokal.

---

## 📚 Knowledge Base — 180 Kata, 15 Kategori

Basis pengetahuan dirancang ramah anak, terbebas dari kata-kata negatif, dan memiliki cakupan kosakata yang luas:

| Kategori | Jumlah | Contoh Kata |
| :--- | :---: | :--- |
| 🐱 **hewan** | 26 | KUCING, ANJING, GAJAH, JERAPAH, LUMBA... |
| 🍎 **buah** | 18 | APEL, MANGGA, JERUK, SEMANGKA, STROBERI... |
| 🥕 **sayuran** | 12 | WORTEL, BAYAM, KANGKUNG, BROKOLI... |
| 📚 **benda** | 15 | BUKU, PENSIL, JAM, CERMIN, LAMPU... |
| 🚗 **kendaraan** | 10 | MOBIL, HELIKOPTER, AMBULANS, TRAKTOR... |
| ☀️ **alam** | 12 | MATAHARI, PELANGI, SAWAH, SUNGAI... |
| 👁️ **tubuh** | 10 | MATA, GIGI, JARI, PERUT... |
| 🔴 **warna** | 8 | MERAH, UNGU, ORANYE, PUTIH, HITAM... |
| 🍚 **makanan** | 12 | NASI, SATE, TEMPE, TAHU, PERMEN, ES... |
| 🏫 **tempat** | 10 | SEKOLAH, PANTAI, LAPANGAN, PERPUSTAKAAN... |
| 👨‍🏫 **profesi** | 10 | GURU, PILOT, KOKI, NELAYAN, ASTRONOT... |
| 👨 **keluarga** | 8 | AYAH, IBU, KAKEK, NENEK, PAMAN, BIBI... |
| 🏃 **olahraga** [BARU] | 10 | LARI, SENAM, VOLI, TENIS, GOLF, TINJU... |
| 1️⃣ **angka** [BARU] | 10 | SATU, DUA, TIGA, EMPAT, LIMA... |
| 🌤️ **cuaca** [BARU] | 8 | CERAH, MENDUNG, ANGIN, SALJU, PANAS... |

*Catatan: Seluruh konten kata negatif telah dimigrasi total ke engine filter internal.*

---

## 📱 Fitur Lengkap WiSee
- **Login & Register**: Sistem autentikasi aman berbasis Room DB dengan auto-redirect setelah pendaftaran sukses.
- **Session Persistent**: Login satu kali, tetap masuk di peluncuran berikutnya.
- **Dual Input OCR**: Canvas menggambar interaktif (DrawingView) dan pengunggahan gambar tulisan tangan (Galeri & Kamera).
- **Offline TTS**: Konversi teks-ke-suara edukatif bahasa Indonesia secara offline dengan pelafalan yang ramah anak.
- **AI-Powered Illustration**: Menghasilkan gambar ilustrasi berkualitas tinggi dari coretan anak menggunakan API HuggingFace FLUX.1-schnell.
- **Save History**: Menyimpan riwayat belajar kata dan gambar AI di database lokal Room SQLite secara dinamis.
- **Filter Konten Negatif**: Blokir kata kasar/buruk secara otomatis dengan petunjuk alternatif yang positif.
- **Dataset Built-in**: Tidak memerlukan unggah file manual, siap digunakan seketika.

---

## ⚠️ Catatan Penting
1. **Sync Gradle**: Membutuhkan internet pada sinkronisasi pertama kali di Android Studio.
2. **Text-To-Speech**: Pastikan paket bahasa Indonesia terpasang di perangkat (`Settings → Language → Text-to-speech → Install Indonesian`).
3. **Koneksi Internet**: Diperlukan untuk mengakses server Pollinations untuk membuat ilustrasi AI.
