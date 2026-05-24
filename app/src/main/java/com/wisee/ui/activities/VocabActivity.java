package com.wisee.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wisee.R;
import com.wisee.model.KnowledgeFrame;
import com.wisee.service.KnowledgeFrameService;
import com.wisee.service.SessionService;
import com.wisee.ui.adapters.VocabAdapter;
import com.wisee.util.TtsUtil;

import java.util.*;

public class VocabActivity extends AppCompatActivity {

    private RecyclerView rvVocab;
    private ChipGroup    cgCat;
    private SearchView   svSearch;
    private TextView     tvCount;
    private VocabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);

        rvVocab  = findViewById(R.id.rvVocab);
        cgCat    = findViewById(R.id.cgCategories);
        svSearch = findViewById(R.id.svSearch);
        tvCount  = findViewById(R.id.tvCount);

        KnowledgeFrameService kfs = KnowledgeFrameService.getInstance();

        adapter = new VocabAdapter(new ArrayList<>(kfs.getAll()), frame -> {
            // Klik kata → langsung putar TTS
            TtsUtil.getInstance().speak(frame.audioText);
        });
        rvVocab.setLayoutManager(new GridLayoutManager(this, 2));
        rvVocab.setAdapter(adapter);

        updateCount(kfs.getAll().size());

        // Category chips
        Chip chipAll = makeChip("Semua");
        chipAll.setChecked(true);
        cgCat.addView(chipAll);
        chipAll.setOnClickListener(v -> refresh("Semua", svSearch.getQuery().toString()));

        for (String cat : kfs.getCategories()) {
            Chip chip = makeChip(cat);
            chip.setOnClickListener(vv -> refresh(chip.getText().toString(),
                    svSearch.getQuery().toString()));
            cgCat.addView(chip);
        }

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                refresh(getSelected(), q);
                return true;
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void refresh(String cat, String query) {
        KnowledgeFrameService kfs = KnowledgeFrameService.getInstance();
        List<KnowledgeFrame> list;
        if ("Semua".equals(cat)) list = new ArrayList<>(kfs.getAll());
        else                      list = kfs.getByCategory(cat);

        if (query != null && !query.isBlank()) {
            String q = query.toUpperCase().trim();
            list.removeIf(f -> !f.word.contains(q) && !f.category.toUpperCase().contains(q));
        }
        adapter.update(list);
        updateCount(list.size());
    }

    private void updateCount(int n) {
        tvCount.setText(n + " kata");
    }

    private String getSelected() {
        int id = cgCat.getCheckedChipId();
        if (id == View.NO_ID) return "Semua";
        Chip c = cgCat.findViewById(id);
        return c != null ? c.getText().toString() : "Semua";
    }

    private Chip makeChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(false);
        chip.setChipBackgroundColorResource(R.color.chip_selector);
        chip.setTextColor(getResources().getColorStateList(R.color.chip_text_selector, null));
        return chip;
    }
}
