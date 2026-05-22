package com.wisee.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.wisee.R;
import com.wisee.service.DatabaseService;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etUsername, etEmail, etPassword, etConfirm;
    private TextView tvError, tvSuccess;
    private Button   btnRegister;
    private TextView tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName      = findViewById(R.id.etName);
        etUsername  = findViewById(R.id.etUsername);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        etConfirm   = findViewById(R.id.etConfirm);
        tvError     = findViewById(R.id.tvError);
        tvSuccess   = findViewById(R.id.tvSuccess);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoLogin   = findViewById(R.id.tvGoLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvGoLogin.setOnClickListener(v -> finish()); // kembali ke Login
    }

    private void handleRegister() {
        String name     = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirm  = etConfirm.getText().toString();

        tvError.setVisibility(View.GONE);
        tvSuccess.setVisibility(View.GONE);

        // Validasi
        if (name.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirm.isEmpty()) {
            showError("❌ Semua kolom harus diisi ya!");
            return;
        }
        if (name.length() < 2) {
            showError("❌ Nama minimal 2 karakter.");
            return;
        }
        if (username.length() < 3 || username.length() > 20) {
            showError("❌ Username harus 3–20 karakter.");
            return;
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            showError("❌ Username hanya huruf, angka, dan underscore.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showError("❌ Format email tidak valid.");
            return;
        }
        if (password.length() < 6) {
            showError("❌ Password minimal 6 karakter.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("❌ Password dan konfirmasi tidak sama.");
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("⏳ Mendaftar...");

        DatabaseService.register(username, name, email, password, result -> runOnUiThread(() -> {
            btnRegister.setEnabled(true);
            btnRegister.setText("🌟 Daftar Sekarang!");

            switch (result) {
                case SUCCESS:
                    // Tampilkan pesan sukses, lalu redirect ke Login setelah 2 detik
                    showSuccess("🎉 Berhasil daftar! Masuk sekarang...");
                    tvGoLogin.postDelayed(() -> {
                        Intent intent = new Intent(this, LoginActivity.class);
                        // Pre-fill username agar user tinggal input password
                        intent.putExtra("prefill_username", username);
                        startActivity(intent);
                        finish();
                    }, 1800);
                    break;
                case USERNAME_TAKEN:
                    showError("❌ Username '" + username + "' sudah dipakai. Pilih lain.");
                    break;
                case EMAIL_TAKEN:
                    showError("❌ Email sudah terdaftar. Coba login.");
                    break;
                default:
                    showError("❌ Gagal mendaftar. Coba lagi.");
            }
        }));
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }

    private void showSuccess(String msg) {
        tvSuccess.setText(msg);
        tvSuccess.setVisibility(View.VISIBLE);
    }
}
