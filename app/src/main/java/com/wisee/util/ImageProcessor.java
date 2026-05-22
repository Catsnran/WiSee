package com.wisee.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import com.wisee.service.KnnService;
import java.util.ArrayList;
import java.util.List;

/**
 * Preprocessing bitmap tulisan tangan → float[784] untuk KNN.
 * Pipeline: Grayscale → Otsu Threshold → Segmentasi → Resize 28×28
 */
public class ImageProcessor {

    private static final int SIZE = KnnService.IMG_SIZE;

    /** Bitmap → array grayscale 0-255 */
    public static int[][] toGray(Bitmap bmp) {
        int w = bmp.getWidth(), h = bmp.getHeight();
        int[][] gray = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int px = bmp.getPixel(x, y);
                int r = (px >> 16) & 0xFF;
                int g = (px >> 8)  & 0xFF;
                int b =  px        & 0xFF;
                gray[y][x] = (int)(0.299*r + 0.587*g + 0.114*b);
            }
        }
        return gray;
    }

    /** Otsu thresholding → true = foreground (tinta) */
    public static boolean[][] threshold(int[][] gray) {
        int h = gray.length, w = gray[0].length;
        int[] hist = new int[256];
        for (int[] row : gray) for (int v : row) hist[Math.min(255, v)]++;

        int total = h * w; float sum = 0;
        for (int i = 0; i < 256; i++) sum += i * hist[i];

        float sumB = 0; int wB = 0; float maxVar = 0; int thr = 128;
        for (int i = 0; i < 256; i++) {
            wB += hist[i]; if (wB == 0) continue;
            int wF = total - wB; if (wF == 0) break;
            sumB += i * hist[i];
            float mB = sumB / wB, mF = (sum - sumB) / wF;
            float v = (float) wB * wF * (mB - mF) * (mB - mF);
            if (v > maxVar) { maxVar = v; thr = i; }
        }

        boolean[][] bin = new boolean[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                bin[y][x] = gray[y][x] < thr;

        // Auto-invert jika background lebih gelap
        int ink = 0;
        for (boolean[] row : bin) for (boolean v : row) if (v) ink++;
        if (ink > (h * w / 2)) {
            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++)
                    bin[y][x] = !bin[y][x];
        }
        return bin;
    }

    /** Segmentasi karakter berdasarkan proyeksi kolom */
    public static List<int[]> segmentChars(boolean[][] bin) {
        int h = bin.length, w = bin[0].length;
        int[] colSum = new int[w];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                if (bin[y][x]) colSum[x]++;

        List<int[]> segs = new ArrayList<>();
        int minW = Math.max(4, w / 60);
        boolean in = false; int sx = 0;
        for (int x = 0; x <= w; x++) {
            boolean ink = x < w && colSum[x] > 0;
            if (!in && ink) { in = true; sx = x; }
            else if (in && !ink) {
                in = false;
                if (x - sx >= minW) {
                    int minY = h, maxY = 0;
                    for (int cx = sx; cx < x; cx++)
                        for (int cy = 0; cy < h; cy++)
                            if (bin[cy][cx]) { minY = Math.min(minY, cy); maxY = Math.max(maxY, cy); }
                    if (maxY > minY) segs.add(new int[]{sx, minY, x - sx, maxY - minY + 1});
                }
            }
        }
        return segs;
    }

    /** Ekstrak satu karakter dan resize ke SIZE×SIZE → float[784] */
    public static float[] extract(boolean[][] bin, int[] bbox) {
        int bx = bbox[0], by = bbox[1], bw = bbox[2], bh = bbox[3];
        int h = bin.length, w = bin[0].length;
        int pad = Math.max(bw, bh) / 7;
        int x1 = Math.max(0, bx - pad),     y1 = Math.max(0, by - pad);
        int x2 = Math.min(w-1, bx+bw+pad),  y2 = Math.min(h-1, by+bh+pad);
        int sw = x2-x1+1, sh = y2-y1+1;

        float[][] src = new float[sh][sw];
        for (int y = 0; y < sh; y++)
            for (int x = 0; x < sw; x++)
                src[y][x] = bin[y1+y][x1+x] ? 1f : 0f;

        float[] out = new float[SIZE * SIZE];
        for (int ty = 0; ty < SIZE; ty++)
            for (int tx = 0; tx < SIZE; tx++) {
                int iy = Math.min((int)((float)ty / SIZE * sh), sh-1);
                int ix = Math.min((int)((float)tx / SIZE * sw), sw-1);
                out[ty * SIZE + tx] = src[iy][ix];
            }
        return out;
    }

    /**
     * Konversi Bitmap canvas (tinta hitam di latar putih) langsung ke float[784].
     * Cocok untuk pengenalan satu kata / satu karakter sekaligus.
     */
    public static float[] bitmapToFeatures(Bitmap bmp) {
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, SIZE, SIZE, true);
        float[] out = new float[SIZE * SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int px = scaled.getPixel(x, y);
                int r = (px >> 16) & 0xFF;
                int g = (px >> 8)  & 0xFF;
                int b =  px        & 0xFF;
                float gray = (0.299f*r + 0.587f*g + 0.114f*b) / 255f;
                out[y * SIZE + x] = 1f - gray; // invert: tinta = 1
            }
        }
        if (scaled != bmp) scaled.recycle();
        return out;
    }

    /** Resize Bitmap ke ukuran baru */
    public static Bitmap resize(Bitmap bmp, int w, int h) {
        return Bitmap.createScaledBitmap(bmp, w, h, true);
    }
}
