package com.wisee.service;

import android.content.Context;
import com.wisee.model.User;
import com.wisee.model.WordHistory;
import com.wisee.util.HashUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade untuk semua operasi database.
 * Semua operasi DB dijalankan di background thread (executor).
 */
public class DatabaseService {

    private static AppDatabase db;
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public static void init(Context ctx) {
        db = AppDatabase.getInstance(ctx);
    }

    // ── USER ──────────────────────────────────────────────────

    public interface Callback<T> { void onResult(T result); }

    /** Register user baru. Callback di background thread. */
    public static void register(String username, String displayName,
                                String email, String password,
                                Callback<RegisterResult> cb) {
        exec.execute(() -> {
            // Cek duplikat
            if (db.userDao().countByUsername(username.toLowerCase()) > 0) {
                cb.onResult(RegisterResult.USERNAME_TAKEN);
                return;
            }
            if (db.userDao().countByEmail(email.toLowerCase()) > 0) {
                cb.onResult(RegisterResult.EMAIL_TAKEN);
                return;
            }
            User user = new User(
                username.toLowerCase().trim(),
                displayName.trim(),
                email.toLowerCase().trim(),
                HashUtil.sha256(password),
                LocalDateTime.now().toString()
            );
            try {
                db.userDao().insert(user);
                cb.onResult(RegisterResult.SUCCESS);
            } catch (Exception e) {
                cb.onResult(RegisterResult.ERROR);
            }
        });
    }

    public enum RegisterResult { SUCCESS, USERNAME_TAKEN, EMAIL_TAKEN, ERROR }

    /** Login. Callback di background thread, null jika gagal. */
    public static void login(String username, String password, Callback<User> cb) {
        exec.execute(() -> {
            User user = db.userDao().findByUsername(username.toLowerCase().trim());
            if (user == null || !user.passwordHash.equals(HashUtil.sha256(password))) {
                cb.onResult(null);
            } else {
                cb.onResult(user);
            }
        });
    }

    // ── HISTORY ───────────────────────────────────────────────

    public static void saveHistory(WordHistory h, Callback<Boolean> cb) {
        exec.execute(() -> {
            try {
                db.wordHistoryDao().insert(h);
                cb.onResult(true);
            } catch (Exception e) { cb.onResult(false); }
        });
    }

    public static void getHistory(int userId, Callback<List<WordHistory>> cb) {
        exec.execute(() -> cb.onResult(db.wordHistoryDao().getByUser(userId)));
    }

    public static void deleteHistory(int id, int userId, Callback<Boolean> cb) {
        exec.execute(() -> cb.onResult(db.wordHistoryDao().deleteById(id, userId) > 0));
    }

    public static void clearHistory(int userId, Callback<Boolean> cb) {
        exec.execute(() -> {
            db.wordHistoryDao().deleteAll(userId);
            cb.onResult(true);
        });
    }

    public static void getUniqueWordCount(int userId, Callback<Integer> cb) {
        exec.execute(() -> cb.onResult(db.wordHistoryDao().countUniqueWords(userId)));
    }
}
