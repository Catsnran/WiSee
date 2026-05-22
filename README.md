# ✨ WiSee Android — Fun Learning for Kids

Aplikasi **Android** edukasi interaktif untuk anak-anak yang mengubah tulisan tangan menjadi gambar ilustrasi AI dan suara edukatif bahasa Indonesia.

---

## 🚀 Cara Buka di Android Studio

### 1. Persyaratan
- **Android Studio Hedgehog** (2023.1.1) atau lebih baru
- **JDK 17** (sudah include di Android Studio)
- **Android SDK 34**
- Device/Emulator Android **min API 24** (Android 7.0+)

### 2. Buka Project
```
1. Buka Android Studio
2. File → Open → pilih folder WiSeeAndroid/
3. Tunggu Gradle sync selesai (~2-5 menit, perlu internet pertama kali)
4. Klik ▶ Run atau Shift+F10
```
## 📦 Dataset KNN (Built-in, Tidak Perlu Upload)

Dataset sudah **ter-bundle** di dalam APK:
```
app/src/main/assets/dataset/az_dataset.csv.gz
```
- **7.800 sampel** (300 per huruf × 26 huruf A-Z)
- Diekstrak dari dataset A-Z Handwritten Kaggle
- Format: `label(0-25), pixel1, ..., pixel784` (28×28 grayscale)
- Ukuran terkompresi: **~1.8 MB**
- Dimuat otomatis saat aplikasi pertama dibuka

---

## 🏗️ Struktur Project

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
│   │   │   └── KnowledgeFrameService.java ← Basis pengetahuan (130+ kata)
│   │   ├── ui/
│   │   │   ├── activities/
│   │   │   │   ├── SplashActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── RegisterActivity.java  ← Redirect ke Login setelah daftar
│   │   │   │   ├── MainActivity.java      ← OCR + Kosakata utama
│   │   │   │   ├── ResultActivity.java    ← Detail hasil + AI illustration
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
│   │       └── TtsUtil.java              ← Android TextToSpeech (offline)
│   │
│   ├── assets/dataset/
│   │   └── az_dataset.csv.gz              ← Dataset KNN built-in ✅
│   │
│   └── res/
│       ├── layout/                        ← 7 layout XML
│       ├── drawable/                      ← 11 shape/selector drawable
│       ├── values/                        ← colors, strings, themes, dimens
│       └── color/                         ← chip color selectors
│
├── build.gradle                           ← Dependencies
└── settings.gradle
```

---

## 🤖 Cara Kerja Sistem

### Alur Pengenalan Tulisan

```
User menggambar di DrawingView
        ↓
Bitmap (hitam di latar putih)
        ↓
toGray() → Grayscale 2D array
        ↓
threshold() → Otsu binarisasi
        ↓
segmentChars() → Proyeksi kolom → [x,y,w,h] per karakter
        ↓
extract() → Crop + padding + resize 28×28
        ↓
KnnService.predict() → K=5, Euclidean distance
        ↓
Karakter → Kata (A-Z concatenated)
        ↓
KnowledgeFrameService.getFrame(word)
        ↓
Frame: {emoji, audioText, imagePrompt, category}
        ↓
TtsUtil.speak(audioText)  +  ResultActivity
```

### KNN (K-Nearest Neighbors)
- **K = 5** tetangga terdekat, voting majority
- **Metrik**: Euclidean distance pada ruang 784 dimensi
- **Training data**: 7.800 sampel (300/huruf)
- **Loaded from**: `assets/dataset/az_dataset.csv.gz` (background thread)

### Frame-Based Knowledge System
Setiap kata dipetakan ke `KnowledgeFrame`:
```java
new KnowledgeFrame(
  "KUCING",      // word (key)
  "hewan",       // category
  "🐱",          // emoji
  "Kucing! ...", // audioText (TTS)
  "cute cat...", // imagePrompt (AI)
  true           // safe (false = konten negatif)
)
```

### Text-to-Speech
- **Engine**: Android built-in `TextToSpeech`
- **Bahasa**: `id_ID` (Bahasa Indonesia), fallback English
- **Tidak perlu internet** ✅
- Speech rate: 0.85x (pelan, ramah anak)

---

## 📚 Knowledge Base — 130+ Kata, 13 Kategori

| Kategori | Jumlah | Contoh |
|---|---|---|
| 🐱 hewan | 26 | KUCING, ANJING, GAJAH, JERAPAH, LUMBA... |
| 🍎 buah | 18 | APEL, MANGGA, DURIAN, RAMBUTAN, SALAK... |
| 🥕 sayuran | 12 | WORTEL, BAYAM, KANGKUNG, BROKOLI... |
| 📚 benda | 15 | BUKU, PENSIL, JAM, CERMIN, LAMPU... |
| 🚗 kendaraan | 10 | MOBIL, HELIKOPTER, AMBULANS, TRAKTOR... |
| ☀️ alam | 12 | MATAHARI, PELANGI, SAWAH, SUNGAI... |
| 👁️ tubuh | 10 | MATA, GIGI, JARI, PERUT... |
| 🔴 warna | 8 | MERAH, UNGU, ORANYE, PUTIH, HITAM... |
| 🍚 makanan | 12 | NASI, SATE, TEMPE, TAHU, PERMEN, ES... |
| 🏫 tempat | 10 | SEKOLAH, PANTAI, LAPANGAN, PERPUSTAKAAN... |
| 👨‍🏫 profesi | 10 | GURU, PILOT, KOKI, NELAYAN, ASTRONOT... |
| 👨 keluarga | 8 | AYAH, IBU, KAKEK, NENEK, PAMAN, BIBI... |
| ⛔ kata negatif | 8 | Filter + pesan positif |

---

## 🎨 Color Palette

| Nama | Hex | Penggunaan |
|---|---|---|
| Orange | `#EF7722` | Utama, header, aksen |
| Yellow | `#FAA533` | Tombol audio, aksen kedua |
| Light | `#EBEBEB` | Background sekunder |
| Blue | `#0BA6DF` | Tombol sekunder, kategori |
| BG | `#FFF8F0` | Background halaman |

---

## 📱 Fitur Lengkap

- ✅ **Login & Register** → Register langsung redirect ke Login
- ✅ **Session persisten** → Tidak perlu login ulang
- ✅ **OCR tulisan tangan** → DrawingView custom + KNN
- ✅ **Upload foto** → Dari galeri
- ✅ **TTS offline** → Android TextToSpeech bahasa Indonesia
- ✅ **Ilustrasi AI** → HuggingFace FLUX.1-schnell (perlu internet + token)
- ✅ **Riwayat belajar** → Room SQLite per user
- ✅ **Browser kosakata** → 130+ kata, filter kategori + search
- ✅ **Filter konten negatif** → Kata buruk → pesan positif
- ✅ **Dataset built-in** → Tidak perlu upload CSV

---

## ⚠️ Catatan

1. **Gradle sync** butuh internet pertama kali untuk download dependencies
2. **TTS bahasa Indonesia** butuh language pack di-install di device
   - Settings → General Management → Language → Text-to-speech → Install Indonesian
3. **Akurasi KNN** ~70-85% tergantung kejelasan tulisan (7.800 sampel)
   - Tips: tulis besar, tebal, satu kata, huruf kapital terpisah
4. **Ilustrasi AI** butuh internet(opsional)
