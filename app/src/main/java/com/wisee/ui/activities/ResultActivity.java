package com.wisee.ui.activities;

import android.graphics.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.bumptech.glide.Glide;
import com.wisee.R;
import com.wisee.model.KnowledgeFrame;
import com.wisee.model.WordHistory;
import com.wisee.service.DatabaseService;
import com.wisee.service.KnowledgeFrameService;
import com.wisee.service.SessionService;
import com.wisee.util.TtsUtil;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "WiSee";
    public static final String EXTRA_WORD = "word";

    // ── Views ─────────────────────────────────────────────────
    private TextView         tvWord, tvEmoji, tvCategory, tvDesc;
    private ImageView        ivIllustration;
    private ProgressBar      pbImage;
    private TextView         tvImageStatus;
    private Button           btnSpeak, btnSave, btnBack, btnRegenerate;
    private MaterialCardView cardNegative;
    private TextView         tvNegativeMsg;

    // ── State ─────────────────────────────────────────────────
    private KnowledgeFrame   frame;
    private String           word;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        bindViews();

        word  = getIntent().getStringExtra(EXTRA_WORD);
        frame = KnowledgeFrameService.getInstance().getFrame(word);

        if (frame == null) { finish(); return; }

        populateResult();
        TtsUtil.getInstance().speak(frame.audioText);
        generateIllustration();
    }

    // ─────────────────────────────────────────────────────────
    private void bindViews() {
        tvWord          = findViewById(R.id.tvWord);
        tvEmoji         = findViewById(R.id.tvEmoji);
        tvCategory      = findViewById(R.id.tvCategory);
        tvDesc          = findViewById(R.id.tvDesc);
        ivIllustration  = findViewById(R.id.ivIllustration);
        pbImage         = findViewById(R.id.pbImage);
        tvImageStatus   = findViewById(R.id.tvImageStatus);
        btnSpeak        = findViewById(R.id.btnSpeak);
        btnSave         = findViewById(R.id.btnSave);
        btnBack         = findViewById(R.id.btnBack);
        btnRegenerate   = findViewById(R.id.btnRegenerate);
        cardNegative    = findViewById(R.id.cardNegative);
        tvNegativeMsg   = findViewById(R.id.tvNegativeMsg);

        btnBack.setOnClickListener(v -> finish());
        btnSpeak.setOnClickListener(v -> {
            if (frame != null) TtsUtil.getInstance().speak(frame.audioText);
        });
        btnSave.setOnClickListener(v -> saveHistory());
        btnRegenerate.setOnClickListener(v -> {
            clearImageCache();
            generateIllustration();
        });
    }

    private void populateResult() {
        tvWord.setText(frame.word);
        tvEmoji.setText(frame.emoji);
        tvCategory.setText("📁 " + frame.category.toUpperCase());
        tvDesc.setText(frame.audioText);

        if (!frame.safe) {
            cardNegative.setVisibility(View.VISIBLE);
            tvNegativeMsg.setText("💝 Yuk selalu gunakan kata-kata yang baik dan positif kepada semua orang!");
        }
    }

    // ─────────────────────────────────────────────────────────
    //  IMAGE GENERATION — 3-layer strategy:
    //  1. Cache lokal (instant)
    //  2. Pollination
    //  3. Fallback lokal: render emoji besar dari Canvas (offline, instant)
    // ─────────────────────────────────────────────────────────
    private void generateIllustration() {
        File cacheFile = new File(getCachePath(word));

        setImageLoading("🎨 Membuat ilustrasi...");

        // ── Layer 1: Cache hit ────────────────────────────────
        if (cacheFile.exists() && cacheFile.length() > 500) {
            Log.d(TAG, "Image cache hit: " + cacheFile.getName());
            loadImageFromFile(cacheFile);
            return;
        }

        // ── Layer 2: Pollinations API ─────────────────────────
        exec.execute(() -> {
            String prompt = "cartoon illustration, cute cartoon, colorful, "
                    + "simple, white background, safe for kids, " + frame.imagePrompt;

            byte[] imgBytes = null;

            // Retry 3x
            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    Log.d(TAG, "Pollinations attempt " + attempt + "/3");
                    int finalAttempt = attempt;
                    runOnUiThread(() -> tvImageStatus.setText(
                            "🎨 Generating... (percobaan " + finalAttempt + "/3)"
                    ));

                    imgBytes = callPollination(prompt);
                    if (imgBytes != null) break;

                    if (attempt < 3) Thread.sleep(3000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    Log.e(TAG, "Pollinations attempt " + attempt + " error: " + e.getMessage());
                }
            }

            if (imgBytes != null && imgBytes.length > 500) {
                // Tambah batas maksimal ukuran (5MB)
                if (imgBytes.length > 5 * 1024 * 1024) {
                    Log.w(TAG, "Image terlalu besar: " + imgBytes.length + " bytes, skip");
                    runOnUiThread(() -> showFallbackImage());
                    return;
                }
                if (isValidImageBytes(imgBytes)) {
                    try {
                        new File(cacheFile.getParent()).mkdirs();
                        FileOutputStream fos = new FileOutputStream(cacheFile);
                        fos.write(imgBytes);
                        fos.close();
                        Log.d(TAG, "Image saved: " + cacheFile.length() + " bytes");
                        runOnUiThread(() -> loadImageFromFile(cacheFile));
                        return;
                    } catch (Exception e) {
                        Log.e(TAG, "Save image error: " + e.getMessage());
                    }
                } else {
                    Log.w(TAG, "Response bukan image");
                    Log.w(TAG, "Response: " + new String(imgBytes, 0, Math.min(200, imgBytes.length)));
                }
            }

            // ── Layer 3: Fallback gambar lokal ────────────────
            Log.d(TAG, "Fallback ke generated local image");
            runOnUiThread(() -> showFallbackImage());
        });
    }

    /**
     * Panggil Pollinations.
     * Header x-wait-for-model: true → HF tunggu model load (tidak langsung 503).
     */
    private byte[] callPollination(String prompt) throws Exception {
        // Pollinations.ai — gratis, tanpa API key, tanpa signup
        String encodedPrompt = java.net.URLEncoder.encode(prompt, "UTF-8");
        String endpoint = "https://image.pollinations.ai/prompt/" + encodedPrompt
                + "?width=512&height=512&model=flux&nologo=true";

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(30_000);
        conn.setReadTimeout(120_000);

        int code = conn.getResponseCode();
        Log.d(TAG, "Pollinations response: " + code);

        if (code == 200) {
            try (InputStream is = conn.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buf = new byte[8192]; int n;
                while ((n = is.read(buf)) != -1) baos.write(buf, 0, n);
                return baos.toByteArray();
            }
        }
        return null;
    }

    /**
     * Verifikasi bytes adalah image valid (cek magic bytes header).
     */
    private boolean isValidImageBytes(byte[] data) {
        if (data == null || data.length < 4) return false;
        // JPEG: FF D8 FF
        if ((data[0] & 0xFF) == 0xFF && (data[1] & 0xFF) == 0xD8) return true;
        // PNG:  89 50 4E 47
        if ((data[0] & 0xFF) == 0x89 && data[1] == 'P' && data[2] == 'N' && data[3] == 'G') return true;
        // WEBP: RIFF....WEBP
        if (data[0] == 'R' && data[1] == 'I' && data[2] == 'F' && data[3] == 'F') return true;
        return false;
    }

    // ─────────────────────────────────────────────────────────
    //  FALLBACK: Generate gambar lokal dari emoji + gradient Canvas
    //  Offline, instant, selalu berhasil
    // ─────────────────────────────────────────────────────────
    private void showFallbackImage() {
        Log.d(TAG, "Showing fallback canvas image for: " + frame.emoji);

        try {
            Bitmap bmp = generateLocalIllustration(frame.emoji, frame.word, frame.category);
            ivIllustration.setImageBitmap(bmp);
            ivIllustration.setVisibility(View.VISIBLE);
            pbImage.setVisibility(View.GONE);
            tvImageStatus.setText("🖼️ Ilustrasi lokal (offline)");
            btnRegenerate.setEnabled(true);

            // Cache gambar lokal agar tidak perlu generate ulang
            try {
                File f = new File(getCachePath(word));
                new File(f.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            } catch (Exception ignored) {}

        } catch (Exception e) {
            Log.e(TAG, "Fallback image error: " + e.getMessage());
            pbImage.setVisibility(View.GONE);
            tvImageStatus.setText("💡 Tidak ada ilustrasi");
            btnRegenerate.setEnabled(true);
        }
    }

    /**
     * Generate bitmap cantik dari emoji + teks dengan gradient background.
     * Warna background sesuai kategori kata.
     */
    private Bitmap generateLocalIllustration(String emoji, String word, String category) {
        int size = 512;
        Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        // Pilih warna background berdasarkan kategori
        int[] colors = getCategoryColors(category);

        // Gradient background
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RadialGradient gradient = new RadialGradient(
                size / 2f, size / 2f, size / 1.5f,
                colors[0], colors[1], Shader.TileMode.CLAMP
        );
        bgPaint.setShader(gradient);
        canvas.drawRoundRect(0, 0, size, size, 40, 40, bgPaint);

        // Lingkaran putih di tengah
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.argb(180, 255, 255, 255));
        canvas.drawCircle(size / 2f, size / 2.2f, size / 2.6f, circlePaint);

        // Emoji besar di tengah
        Paint emojiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        emojiPaint.setTextSize(size / 2.8f);
        emojiPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(emoji, size / 2f, size / 2f + emojiPaint.getTextSize() / 3, emojiPaint);

        // Nama kata di bawah
        Paint wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wordPaint.setColor(Color.WHITE);
        wordPaint.setTextSize(size / 8f);
        wordPaint.setTextAlign(Paint.Align.CENTER);
        wordPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        // Shadow untuk readability
        wordPaint.setShadowLayer(4f, 2f, 2f, Color.argb(100, 0, 0, 0));
        canvas.drawText(word, size / 2f, size * 0.82f, wordPaint);

        // Label kategori kecil
        Paint catPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        catPaint.setColor(Color.argb(200, 255, 255, 255));
        catPaint.setTextSize(size / 14f);
        catPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(category.toUpperCase(), size / 2f, size * 0.93f, catPaint);

        // Dekorasi bintang kecil di sudut
        Paint starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setColor(Color.argb(180, 255, 255, 255));
        starPaint.setTextSize(size / 10f);
        canvas.drawText("✨", size * 0.12f, size * 0.18f, starPaint);
        canvas.drawText("⭐", size * 0.82f, size * 0.16f, starPaint);

        return bmp;
    }

    /** Warna gradient berdasarkan kategori */
    private int[] getCategoryColors(String cat) {
        switch (cat.toLowerCase()) {
            case "hewan":     return new int[]{0xFFFF9A3C, 0xFFFF6B00};
            case "buah":      return new int[]{0xFFFF6B8A, 0xFFE91E63};
            case "sayuran":   return new int[]{0xFF66BB6A, 0xFF2E7D32};
            case "benda":     return new int[]{0xFF5C9EF0, 0xFF1565C0};
            case "kendaraan": return new int[]{0xFF78909C, 0xFF37474F};
            case "alam":      return new int[]{0xFF4FC3F7, 0xFF0288D1};
            case "tubuh":     return new int[]{0xFFFFB74D, 0xFFF57C00};
            case "warna":     return new int[]{0xFFCE93D8, 0xFF7B1FA2};
            case "makanan":   return new int[]{0xFFFF8A65, 0xFFBF360C};
            case "tempat":    return new int[]{0xFF80CBC4, 0xFF00695C};
            case "profesi":   return new int[]{0xFF9FA8DA, 0xFF283593};
            case "keluarga":  return new int[]{0xFFF48FB1, 0xFFC2185B};
            default:          return new int[]{0xFFEF7722, 0xFFD4661A};
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────
    private void setImageLoading(String msg) {
        pbImage.setVisibility(View.VISIBLE);
        tvImageStatus.setText(msg);
        ivIllustration.setVisibility(View.GONE);
        btnRegenerate.setEnabled(false);
    }

    private void loadImageFromFile(File f) {
        runOnUiThread(() -> {
            pbImage.setVisibility(View.GONE);
            ivIllustration.setVisibility(View.VISIBLE);
            tvImageStatus.setText("✅ Ilustrasi AI");
            btnRegenerate.setEnabled(true);
            Glide.with(this)
                    .load(f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .into(ivIllustration);
        });
    }

    private void clearImageCache() {
        File f = new File(getCachePath(word));
        if (f.exists()) {
            f.delete();
            Log.d(TAG, "Cache cleared: " + f.getName());
        }
    }

    private String getCachePath(String w) {
        File dir = getExternalFilesDir("wisee_images");
        if (dir == null) dir = new File(getFilesDir(), "wisee_images");
        return dir.getAbsolutePath() + "/" + w.toLowerCase() + ".png";
    }

    // ─────────────────────────────────────────────────────────
    //  Save History
    // ─────────────────────────────────────────────────────────
    private void saveHistory() {
        int userId = SessionService.getInstance().getCurrentUser().id;
        WordHistory h = new WordHistory(
                userId, word, frame.category, frame.emoji,
                frame.audioText, LocalDateTime.now().toString()
        );
        DatabaseService.saveHistory(h, ok -> runOnUiThread(() -> {
            if (ok) {
                btnSave.setText("✅ Tersimpan!");
                btnSave.setEnabled(false);
                Toast.makeText(this, "Kata '" + word + "' disimpan! 🎉", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menyimpan.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exec.shutdownNow();
    }
}
