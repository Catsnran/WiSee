package com.wisee.service;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * KNN Classifier — memuat dataset dari assets/dataset/az_dataset.bin
 * Pure Java, tanpa library eksternal.
 * K=5, Euclidean distance, 784 fitur (28×28 pixel).
 */
public class KnnService {

    private static final String TAG = "KnnService";
    private static KnnService instance;

    public static final int IMG_SIZE = 28;
    public static final int FEATURES = IMG_SIZE * IMG_SIZE;
    private static final int K = 5;

    private float[][] trainData;
    private byte[]   trainLabels;
    private int      trainSize = 0;

    private volatile boolean loaded  = false;
    private volatile boolean loading = false;
    private String loadError = null;

    // Listeners
    private final List<Consumer<Float>>   progressListeners = new ArrayList<>();
    private final List<Consumer<Boolean>> doneListeners     = new ArrayList<>();

    private KnnService() {}

    public static KnnService getInstance() {
        if (instance == null) instance = new KnnService();
        return instance;
    }

    /** Panggil dari Application.onCreate() */
    public void preloadAsync(Context ctx) {
        if (loaded || loading) return;
        loading = true;
        new Thread(() -> {
            boolean ok = loadFromAssets(ctx);
            loaded = ok;
            loading = false;
            synchronized (doneListeners) {
                for (Consumer<Boolean> cb : doneListeners) cb.accept(ok);
            }
        }, "knn-loader").start();
    }

    public void addProgressListener(Consumer<Float> cb) {
        synchronized (progressListeners) { progressListeners.add(cb); }
    }
    public void addDoneListener(Consumer<Boolean> cb) {
        if (loaded) { cb.accept(true); return; }
        synchronized (doneListeners) { doneListeners.add(cb); }
    }
    public void clearListeners() {
        progressListeners.clear();
        doneListeners.clear();
    }

    private boolean loadFromAssets(Context ctx) {
        try {
            // Baca binary langsung — jauh lebih cepat dari CSV+GZIP
            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(ctx.getAssets().open("dataset/az_dataset.bin"), 1 << 16)
            );

            trainSize   = dis.readInt();
            trainData   = new float[trainSize][FEATURES];
            trainLabels = new byte[trainSize];

            for (int i = 0; i < trainSize; i++) {
                trainLabels[i] = dis.readByte();
                for (int j = 0; j < FEATURES; j++) {
                    trainData[i][j] = (dis.readUnsignedByte()) / 255.0f;
                }
                if (i % 500 == 0) {
                    float p = Math.min(0.99f, i / (float) trainSize);
                    synchronized (progressListeners) {
                        for (Consumer<Float> cb : progressListeners) cb.accept(p);
                    }
                }
            }
            dis.close();

            Log.i(TAG, "KNN loaded: " + trainSize + " samples");
            synchronized (progressListeners) {
                for (Consumer<Float> cb : progressListeners) cb.accept(1f);
            }
            return true;

        } catch (Exception e) {
            loadError = e.getMessage();
            Log.e(TAG, "KNN load error: " + e.getMessage());
            return false;
        }
    }

    /** Prediksi karakter (KNN penuh, lebih akurat) */
    public char predict(float[] pixels) {
        if (!loaded || trainSize == 0) return '?';

        int[]   kLabels = new int[K];
        float[] kDists  = new float[K];
        Arrays.fill(kDists, Float.MAX_VALUE);
        float maxH = Float.MAX_VALUE; int worstIdx = 0;

        for (int i = 0; i < trainSize; i++) {
            float d = euclidSq(pixels, trainData[i]);
            if (d < maxH) {
                kLabels[worstIdx] = trainLabels[i] & 0xFF;
                kDists[worstIdx]  = d;
                maxH = d; worstIdx = 0;
                for (int j = 1; j < K; j++) if (kDists[j] > maxH) { maxH = kDists[j]; worstIdx = j; }
            }
        }
        int[] votes = new int[36];
        for (int l : kLabels) votes[l]++;
        int best = 0;
        for (int j = 1; j < votes.length; j++) if (votes[j] > votes[best]) best = j;
        return labelToChar(best);
    }

    /** Prediksi cepat (subset acak) untuk real-time preview */
    public char predictFast(float[] pixels, int subset) {
        if (!loaded || trainSize == 0) return '?';
        int sz = Math.min(subset, trainSize);
        float minD = Float.MAX_VALUE; int best = 0;
        Random rng = new Random(42);
        for (int i = 0; i < sz; i++) {
            int idx = rng.nextInt(trainSize);
            float d = euclidSq(pixels, trainData[idx]);
            if (d < minD) { minD = d; best = trainLabels[idx] & 0xFF; }
        }
        return labelToChar(best);
    }

    private float euclidSq(float[] a, float[] b) {
        float s = 0;
        for (int i = 0; i < FEATURES; i++) { float d = a[i] - b[i]; s += d * d; }
        return s;
    }

    public static char labelToChar(int label) {
        if (label >= 0 && label < 26) return (char) ('A' + label);
        if (label >= 26 && label < 36) return (char) ('0' + label - 26);
        return '?';
    }

    public boolean isLoaded()      { return loaded; }
    public boolean isLoading()     { return loading; }
    public String  getLoadError()  { return loadError; }
    public int     getTrainingSize(){ return trainSize; }
}