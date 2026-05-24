package com.wisee.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wisee.R;
import com.wisee.model.KnowledgeFrame;
import com.wisee.model.WordHistory;
import com.wisee.service.KnowledgeFrameService;
import com.wisee.service.KnnService;
import com.wisee.service.SessionService;
import com.wisee.service.DatabaseService;
import com.wisee.ui.adapters.VocabAdapter;
import com.wisee.ui.views.DrawingView;
import com.wisee.util.ImageProcessor;
import com.wisee.util.TtsUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // ── Views ─────────────────────────────────────────────────
    private TextView      tvGreeting, tvKnnStatus;
    private DrawingView   drawingView;
    private Button        btnClear, btnRecognize, btnCamera;
    private View          cardResult, cardUnknown;
    private TextView      tvResultWord, tvResultEmoji, tvResultCategory, tvResultDesc;
    private Button        btnSpeak, btnSave;
    private TextView      tvUnknownMsg;
    private ProgressBar   pbKnn;
    private TextView      tvLoadStatus;
    private View          layoutLoading;
    private RecyclerView  rvVocab;
    private ChipGroup     cgCategories;
    private SearchView    svSearch;
    private ImageView     ivUpload;

    // ── State ─────────────────────────────────────────────────
    private KnowledgeFrame  currentFrame;
    private String          currentWord;
    private Bitmap          uploadedBitmap;
    private VocabAdapter    vocabAdapter;
    private Uri             cameraImageUri;

    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    // ── Image picker launcher ──────────────────────────────────
    private final ActivityResultLauncher<Intent> pickImage =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                try {
                    Uri uri = result.getData().getData();
                    uploadedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ivUpload.setImageBitmap(uploadedBitmap);
                    ivUpload.setVisibility(View.VISIBLE);
                    drawingView.clear();
                } catch (Exception e) {
                    toast("Gagal memuat gambar");
                }
            }
        });

    // ── Camera launcher ──────────────────────────────────────────
    private final ActivityResultLauncher<Intent> takePicture =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && cameraImageUri != null) {
                    try {
                        uploadedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraImageUri);
                        ivUpload.setImageBitmap(uploadedBitmap);
                        ivUpload.setVisibility(View.VISIBLE);
                        drawingView.clear();
                    } catch (Exception e) {
                        toast("Gagal memuat foto");
                    }
                }
            });

    // ── Camera permission launcher ───────────────────────────────
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    toast("Izin kamera diperlukan untuk mengambil foto");
                }
            });

    // ─────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupGreeting();
        setupNavBar();
        setupVocabPanel();

        // Cek status KNN
        KnnService knn = KnnService.getInstance();
        if (knn.isLoaded()) {
            showMainUI();
        } else {
            showLoadingUI();
            knn.addProgressListener(p -> runOnUiThread(() ->
                pbKnn.setProgress((int)(p * 100))));
            knn.addDoneListener(ok -> runOnUiThread(() -> {
                if (ok) showMainUI();
                else tvLoadStatus.setText("❌ Gagal load KNN: " + knn.getLoadError());
            }));
        }

        TtsUtil.getInstance().init(this);
    }

    // ─────────────────────────────────────────────────────────
    //  View Binding
    // ─────────────────────────────────────────────────────────
    private void bindViews() {
        tvGreeting    = findViewById(R.id.tvGreeting);
        tvKnnStatus   = findViewById(R.id.tvKnnStatus);
        drawingView   = findViewById(R.id.drawingView);
        btnClear      = findViewById(R.id.btnClear);
        btnRecognize  = findViewById(R.id.btnRecognize);
        btnCamera     = findViewById(R.id.btnCamera);
        cardResult    = findViewById(R.id.cardResult);
        cardUnknown   = findViewById(R.id.cardUnknown);
        tvResultWord  = findViewById(R.id.tvResultWord);
        tvResultEmoji = findViewById(R.id.tvResultEmoji);
        tvResultCategory = findViewById(R.id.tvResultCategory);
        tvResultDesc  = findViewById(R.id.tvResultDesc);
        btnSpeak      = findViewById(R.id.btnSpeak);
        btnSave       = findViewById(R.id.btnSave);
        tvUnknownMsg  = findViewById(R.id.tvUnknownMsg);
        pbKnn         = findViewById(R.id.pbKnn);
        tvLoadStatus  = findViewById(R.id.tvLoadStatus);
        layoutLoading = findViewById(R.id.layoutLoading);
        rvVocab       = findViewById(R.id.rvVocab);
        cgCategories  = findViewById(R.id.cgCategories);
        svSearch      = findViewById(R.id.svSearch);
        ivUpload      = findViewById(R.id.ivUpload);

        btnClear.setOnClickListener(v -> {
            drawingView.clear();
            uploadedBitmap = null;
            ivUpload.setVisibility(View.GONE);
            hideResults();
        });

        btnRecognize.setOnClickListener(v -> runRecognition());

        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        btnSpeak.setOnClickListener(v -> {
            if (currentFrame != null) {
                TtsUtil.getInstance().speak(currentFrame.audioText);
            }
        });

        btnSave.setOnClickListener(v -> saveToHistory());
    }

    // ─────────────────────────────────────────────────────────
    //  Setup
    // ─────────────────────────────────────────────────────────
    private void setupGreeting() {
        String name = SessionService.getInstance().getCurrentUser().displayName;
        tvGreeting.setText("Halo, " + name + "! 👋");
    }

    private void setupNavBar() {
        findViewById(R.id.btnHistory).setOnClickListener(v ->
            startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.btnVocab).setOnClickListener(v ->
            startActivity(new Intent(this, VocabActivity.class)));

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("🚪 Keluar dari WiSee?")
                .setMessage("Apakah kamu yakin ingin keluar?")
                .setPositiveButton("Ya, Keluar", (d, w) -> {
                    TtsUtil.getInstance().stop();
                    SessionService.getInstance().logout();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                })
                .setNegativeButton("Batal", null)
                .show();
        });
    }

    private void setupVocabPanel() {
        KnowledgeFrameService kfs = KnowledgeFrameService.getInstance();

        // Adapter vocab grid
        vocabAdapter = new VocabAdapter(new ArrayList<>(kfs.getAll()), frame -> {
            // Klik kata dari kosakata → buka ResultActivity
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_WORD, frame.word);
            startActivity(intent);
        });
        rvVocab.setLayoutManager(new GridLayoutManager(this, 3));
        rvVocab.setAdapter(vocabAdapter);

        // Category chips
        Chip chipAll = buildChip("Semua");
        chipAll.setChecked(true);
        cgCategories.addView(chipAll);
        chipAll.setOnClickListener(v -> filterVocab("Semua", ""));

        for (String cat : kfs.getCategories()) {
            Chip chip = buildChip(cat);
            chip.setOnClickListener(v -> filterVocab(cat, svSearch.getQuery().toString()));
            cgCategories.addView(chip);
        }

        // Search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                String cat = getSelectedCategory();
                filterVocab(cat, q);
                return true;
            }
        });
    }

    private Chip buildChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(false);
        chip.setChipBackgroundColorResource(R.color.chip_selector);
        chip.setTextColor(getResources().getColorStateList(R.color.chip_text_selector, null));
        return chip;
    }

    private String getSelectedCategory() {
        int id = cgCategories.getCheckedChipId();
        if (id == View.NO_ID) return "Semua";
        Chip chip = cgCategories.findViewById(id);
        return chip != null ? chip.getText().toString() : "Semua";
    }

    private void filterVocab(String category, String query) {
        KnowledgeFrameService kfs = KnowledgeFrameService.getInstance();
        List<KnowledgeFrame> all;
        if ("Semua".equals(category)) {
            all = new ArrayList<>(kfs.getAll());
        } else {
            all = kfs.getByCategory(category);
        }
        if (query != null && !query.isBlank()) {
            String q = query.toUpperCase().trim();
            all.removeIf(f -> !f.word.contains(q));
        }
        vocabAdapter.update(all);
    }

    // ─────────────────────────────────────────────────────────
    //  Loading / Main UI state
    // ─────────────────────────────────────────────────────────
    private void showLoadingUI() {
        layoutLoading.setVisibility(View.VISIBLE);
        tvLoadStatus.setText("⏳ Memuat model KNN dari dataset...");
        pbKnn.setProgress(0);
        tvKnnStatus.setText("KNN: Memuat...");
    }

    private void showMainUI() {
        layoutLoading.setVisibility(View.GONE);
        tvKnnStatus.setText("KNN: " + KnnService.getInstance().getTrainingSize() + " sampel ✅");
    }

    // ─────────────────────────────────────────────────────────
    //  OCR Recognition
    // ─────────────────────────────────────────────────────────
    private void runRecognition() {
        if (!KnnService.getInstance().isLoaded()) {
            toast("KNN belum siap. Tunggu sebentar ya!");
            return;
        }

        hideResults();
        btnRecognize.setEnabled(false);
        btnRecognize.setText("⏳ Mengenali...");

        exec.execute(() -> {
            try {
                String word;
                Bitmap src = uploadedBitmap != null
                    ? uploadedBitmap
                    : drawingView.getBitmap();

                if (src == null || isBlankBitmap(src)) {
                    runOnUiThread(() -> {
                        resetRecognizeBtn();
                        toast("Canvas kosong! Tulis dulu ya.");
                    });
                    return;
                }

                word = recognizeWord(src);
                final String finalWord = word.toUpperCase().trim();

                runOnUiThread(() -> {
                    resetRecognizeBtn();
                    if (finalWord.isEmpty()) {
                        showUnknown("Tulisan tidak terdeteksi. Tulis lebih jelas dan tebal ya!");
                    } else {
                        processWord(finalWord);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    resetRecognizeBtn();
                    toast("Error: " + e.getMessage());
                });
            }
        });
    }

    private String recognizeWord(Bitmap bmp) {
        // Preprocessing
        int[][] gray = ImageProcessor.toGray(bmp);
        boolean[][] bin = ImageProcessor.threshold(gray);
        List<int[]> segs = ImageProcessor.segmentChars(bin);

        KnnService knn = KnnService.getInstance();
        StringBuilder word = new StringBuilder();

        if (segs.isEmpty()) {
            // Treat whole canvas as one character
            float[] feat = ImageProcessor.bitmapToFeatures(bmp);
            word.append(knn.predict(feat));
        } else {
            for (int[] seg : segs) {
                float[] feat = ImageProcessor.extract(bin, seg);
                word.append(knn.predict(feat));
            }
        }
        return word.toString();
    }

    private boolean isBlankBitmap(Bitmap bmp) {
        int sample = 0, white = 0;
        int w = bmp.getWidth(), h = bmp.getHeight();
        int step = Math.max(1, w * h / 400);
        for (int i = 0; i < w * h; i += step) {
            int px = bmp.getPixel(i % w, i / w);
            int r = (px >> 16) & 0xFF;
            sample++;
            if (r > 230) white++;
        }
        return (float) white / sample > 0.97f;
    }

    private void processWord(String word) {
        currentWord  = word;
        currentFrame = KnowledgeFrameService.getInstance().getFrame(word);

        if (currentFrame != null) {
            // Buka ResultActivity untuk detail lengkap
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_WORD, word);
            startActivity(intent);
            // Juga tampilkan result card di main
            showResult(currentFrame);
        } else {
            showUnknown("Tulisan terbaca: \"" + word + "\"\n\n" +
                "Kata ini belum ada di basis pengetahuan WiSee. " +
                "Coba tulis lebih jelas atau pilih kata dari kosakata di bawah!");
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Result / Unknown display
    // ─────────────────────────────────────────────────────────
    private void showResult(KnowledgeFrame frame) {
        cardResult.setVisibility(View.VISIBLE);
        cardUnknown.setVisibility(View.GONE);

        tvResultWord.setText(frame.word);
        tvResultEmoji.setText(frame.emoji);
        tvResultCategory.setText("📁 " + frame.category.toUpperCase());
        tvResultDesc.setText(frame.audioText);

        btnSave.setEnabled(true);
        btnSave.setText("💾 Simpan");
    }

    private void showUnknown(String msg) {
        cardUnknown.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
        tvUnknownMsg.setText(msg);
    }

    private void hideResults() {
        cardResult.setVisibility(View.GONE);
        cardUnknown.setVisibility(View.GONE);
        currentFrame = null;
        currentWord  = null;
    }

    private void resetRecognizeBtn() {
        btnRecognize.setEnabled(true);
        btnRecognize.setText("🔍 Kenali Tulisan!");
    }

    // ─────────────────────────────────────────────────────────
    //  Save to History
    // ─────────────────────────────────────────────────────────
    private void saveToHistory() {
        if (currentFrame == null || currentWord == null) return;
        int userId = SessionService.getInstance().getCurrentUser().id;
        WordHistory h = new WordHistory(
            userId, currentWord,
            currentFrame.category,
            currentFrame.emoji,
            currentFrame.audioText,
            LocalDateTime.now().toString()
        );
        DatabaseService.saveHistory(h, ok -> runOnUiThread(() -> {
            if (ok) {
                btnSave.setText("✅ Tersimpan!");
                btnSave.setEnabled(false);
                toast("Kata '" + currentWord + "' disimpan ke riwayat!");
            } else {
                toast("Gagal menyimpan.");
            }
        }));
    }

    // ─────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────
    private void openCamera() {
        try {
            File photoFile = createImageFile();
            cameraImageUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", photoFile);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            takePicture.launch(cameraIntent);
        } catch (IOException e) {
            toast("Gagal membuka kamera");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "WISEE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    private void toast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exec.shutdownNow();
    }
}
