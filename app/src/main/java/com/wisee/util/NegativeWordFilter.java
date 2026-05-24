package com.wisee.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter kata-kata negatif/tidak pantas untuk anak-anak.
 *
 * Semua kata negatif diblokir di sini dengan pesan ramah.
 * Tidak ada kata negatif di dalam Knowledge Base.
 */
public class NegativeWordFilter {

    private NegativeWordFilter() {}

    // ── Daftar kata yang perlu diblokir ──────────────────────────────
    private static final Set<String> BLOCKED = new HashSet<>(Arrays.asList(
            // Kata kasar / umpatan
            "GOBLOK", "TOLOL", "BEGO", "DUNGU", "IDIOT", "SINTING",
            "GILA", "EDAN", "BANGSAT", "BAJINGAN", "SIALAN", "BRENGSEK",
            "KEPARAT", "ANJIR", "ANJRIT", "NGENTOT", "KAMPRET",
            "CELAKA", "GEBLEK", "PEKOK", "CICING", "BERENGSEK",

            // Kata tidak pantas untuk anak
            "SEKS", "PORNO", "BUGIL", "TELANJANG", "MABUK", "NARKOBA",
            "ROKOK", "ALKOHOL", "JUDI", "BUNUH", "MATI", "DARAH",

            // Kata bullying
            "BULLY", "HINA", "CELA", "EJEK", "FITNAH", "HASUT",

            // Kata negatif dari Knowledge Base
            "BODOH", "JELEK", "NAKAL", "JAHAT", "MALAS", "BENCI", "KOTOR", "BOHONG"
    ));

    /**
     * Cek apakah kata perlu diblokir total (tidak ada frame edukatif-nya).
     * @param word kata yang diinput user (sudah di-trim & uppercase oleh caller)
     * @return true jika kata harus diblokir
     */
    public static boolean isBlocked(String word) {
        if (word == null || word.isBlank()) return false;
        String w = word.toUpperCase().trim();

        // Terlalu pendek → jangan blokir (huruf tunggal/2 huruf bukan kata negatif)
        if (w.length() < 3) return false;

        // Exact match
        if (BLOCKED.contains(w)) return true;

        // Partial match — cek apakah INPUT mengandung kata negatif
        // (misal: "GOBLOKAN" mengandung "GOBLOK")
        // TIDAK cek sebaliknya (bad.contains(w)) karena terlalu agresif
        for (String bad : BLOCKED) {
            if (w.contains(bad)) return true;
        }
        return false;
    }

    /**
     * Pesan ramah yang ditampilkan saat kata diblokir.
     */
    public static String getBlockMessage(String word) {
        return "💝 Hei! Kata \"" + word + "\" bukan kata yang baik.\n\n"
                + "Yuk coba tulis kata lain yang positif dan menyenangkan! "
                + "Misalnya nama hewan, buah, warna, atau tempat favoritmu. 🌟";
    }
}
