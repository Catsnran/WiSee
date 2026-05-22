package com.wisee;

import android.app.Application;
import com.wisee.service.DatabaseService;
import com.wisee.service.KnnService;

public class WiSeeApplication extends Application {

    private static WiSeeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Inisialisasi database Room
        DatabaseService.init(this);

        // Pre-load KNN di background (dari assets)
        KnnService.getInstance().preloadAsync(this);
    }

    public static WiSeeApplication getInstance() {
        return instance;
    }
}
