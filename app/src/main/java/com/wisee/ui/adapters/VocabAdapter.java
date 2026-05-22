package com.wisee.ui.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wisee.R;
import com.wisee.model.KnowledgeFrame;

import java.util.List;

public class VocabAdapter extends RecyclerView.Adapter<VocabAdapter.VH> {

    public interface OnItemClick { void onClick(KnowledgeFrame frame); }

    private List<KnowledgeFrame> data;
    private final OnItemClick listener;

    public VocabAdapter(List<KnowledgeFrame> data, OnItemClick listener) {
        this.data = data;
        this.listener = listener;
    }

    public void update(List<KnowledgeFrame> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_vocab, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        KnowledgeFrame frame = data.get(position);
        holder.tvEmoji.setText(frame.emoji);
        holder.tvWord.setText(frame.word);
        holder.tvCat.setText(frame.category);
        holder.itemView.setOnClickListener(v -> listener.onClick(frame));

        // Warna berbeda untuk kata negatif
        int bgColor = frame.safe
            ? holder.itemView.getContext().getColor(R.color.chip_bg)
            : holder.itemView.getContext().getColor(R.color.danger_light);
        holder.itemView.setBackgroundColor(bgColor);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvWord, tvCat;
        VH(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tvEmoji);
            tvWord  = v.findViewById(R.id.tvWord);
            tvCat   = v.findViewById(R.id.tvCat);
        }
    }
}
