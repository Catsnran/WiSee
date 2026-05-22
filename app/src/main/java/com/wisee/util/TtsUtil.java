package com.wisee.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

/**
 * Text-to-Speech menggunakan Android TTS engine bawaan.
 * Mendukung Bahasa Indonesia (id_ID).
 * Tidak perlu internet — pakai TTS engine yang sudah ada di device.
 */
public class TtsUtil implements TextToSpeech.OnInitListener {

    private static final String TAG = "TtsUtil";
    private static TtsUtil instance;

    private TextToSpeech tts;
    private boolean ready = false;
    private String pendingText = null;

    private TtsUtil() {}

    public static TtsUtil getInstance() {
        if (instance == null) instance = new TtsUtil();
        return instance;
    }

    /** Inisialisasi TTS — panggil dari Activity/Application */
    public void init(Context ctx) {
        if (tts == null) {
            tts = new TextToSpeech(ctx.getApplicationContext(), this);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Coba set Bahasa Indonesia
            int result = tts.setLanguage(new Locale("id", "ID"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback ke English
                tts.setLanguage(Locale.ENGLISH);
                Log.w(TAG, "Bahasa Indonesia tidak tersedia, fallback ke English");
            }

            // Set speech rate sedikit lebih lambat untuk anak-anak
            tts.setSpeechRate(0.85f);
            tts.setPitch(1.1f);  // Sedikit lebih tinggi, ramah anak

            ready = true;
            Log.i(TAG, "TTS siap");

            // Putar teks yang tertunda
            if (pendingText != null) {
                speak(pendingText);
                pendingText = null;
            }
        } else {
            Log.e(TAG, "TTS inisialisasi gagal");
        }
    }

    /** Putar teks dengan TTS */
    public void speak(String text) {
        if (!ready) {
            pendingText = text;
            return;
        }
        if (text == null || text.isBlank()) return;

        // Stop yang sedang berjalan
        tts.stop();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "wisee_" + System.currentTimeMillis());
    }

    /** Stop TTS */
    public void stop() {
        if (tts != null && tts.isSpeaking()) tts.stop();
    }

    /** Cek apakah TTS siap */
    public boolean isReady() { return ready; }

    /** Panggil saat Activity/Application selesai */
    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
            ready = false;
        }
    }
}
