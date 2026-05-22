package com.wisee.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.wisee.R;
import com.wisee.service.DatabaseService;
import com.wisee.service.SessionService;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView tvError;
    private Button   btnLogin;
    private TextView tvGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername   = findViewById(R.id.etUsername);
        etPassword   = findViewById(R.id.etPassword);
        tvError      = findViewById(R.id.tvError);
        btnLogin     = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Enter di password → login
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            handleLogin();
            return true;
        });

        // Pre-fill username jika dikirim dari RegisterActivity
        String prefill = getIntent().getStringExtra("prefill_username");
        if (prefill != null && !prefill.isEmpty()) {
            etUsername.setText(prefill);
            etPassword.requestFocus();
        }
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        tvError.setVisibility(View.GONE);

        if (username.isEmpty() || password.isEmpty()) {
            showError("❌ Username dan password tidak boleh kosong!");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("⏳ Masuk...");

        DatabaseService.login(username, password, user -> runOnUiThread(() -> {
            btnLogin.setEnabled(true);
            btnLogin.setText("🚀 Masuk Sekarang!");

            if (user == null) {
                showError("❌ Username atau password salah. Coba lagi ya!");
            } else {
                SessionService.getInstance().login(user);
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity(); // tutup semua stack
            }
        }));
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
}
