package com.wisee.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.wisee.R;
import com.wisee.service.SessionService;
import com.wisee.util.TtsUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inisialisasi TTS
        TtsUtil.getInstance().init(this);

        // Tunda 2 detik, lalu cek session
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (SessionService.getInstance().isLoggedIn()) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
