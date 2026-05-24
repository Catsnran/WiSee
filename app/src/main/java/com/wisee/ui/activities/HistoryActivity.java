package com.wisee.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wisee.R;
import com.wisee.model.WordHistory;
import com.wisee.service.DatabaseService;
import com.wisee.service.SessionService;
import com.wisee.ui.adapters.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView  rvHistory;
    private TextView      tvEmpty, tvTotalWords, tvTotalSessions;
    private HistoryAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        userId = SessionService.getInstance().getCurrentUser().id;

        rvHistory      = findViewById(R.id.rvHistory);
        tvEmpty        = findViewById(R.id.tvEmpty);
        tvTotalWords   = findViewById(R.id.tvTotalWords);
        tvTotalSessions= findViewById(R.id.tvTotalSessions);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(new ArrayList<>(), (item, pos) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Riwayat?")
                    .setMessage("Hapus kata \"" + item.word + "\" dari riwayat?")
                    .setPositiveButton("Hapus", (d, w) ->
                            DatabaseService.deleteHistory(item.id, userId, ok -> runOnUiThread(() -> {
                                if (ok) {
                                    adapter.remove(pos);
                                    if (adapter.getItemCount() == 0) showEmpty();
                                    loadStats();
                                }
                            })))
                    .setNegativeButton("Batal", null)
                    .show();
        });
        rvHistory.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnClearAll).setOnClickListener(v -> confirmClearAll());

        loadHistory();
    }

    private void loadHistory() {
        DatabaseService.getHistory(userId, list -> runOnUiThread(() -> {
            if (list == null || list.isEmpty()) {
                showEmpty();
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvHistory.setVisibility(View.VISIBLE);
                adapter.setData(list);
            }
            loadStats();
        }));
    }

    private void loadStats() {
        DatabaseService.getUniqueWordCount(userId, count ->
                runOnUiThread(() -> tvTotalWords.setText(String.valueOf(count))));

        DatabaseService.getHistory(userId, list -> runOnUiThread(() ->
                tvTotalSessions.setText(list == null ? "0" : String.valueOf(list.size()))));
    }

    private void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
        rvHistory.setVisibility(View.GONE);
    }

    private void confirmClearAll() {
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Hapus Semua Riwayat?")
                .setMessage("Semua riwayat belajar akan dihapus permanen.")
                .setPositiveButton("Hapus Semua", (d, w) ->
                        DatabaseService.clearHistory(userId, ok -> runOnUiThread(() -> {
                            adapter.setData(new ArrayList<>());
                            showEmpty();
                            loadStats();
                        })))
                .setNegativeButton("Batal", null)
                .show();
    }
}
