package com.wisee.ui.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wisee.R;
import com.wisee.model.WordHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    public interface OnDelete { void onDelete(WordHistory item, int position); }

    private List<WordHistory> data;
    private final OnDelete listener;

    public HistoryAdapter(List<WordHistory> data, OnDelete listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<WordHistory> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        WordHistory h = data.get(position);
        holder.tvEmoji.setText(h.emoji != null ? h.emoji : "📝");
        holder.tvWord.setText(h.word);
        holder.tvCat.setText(h.category != null ? "📁 " + h.category : "");
        holder.tvDate.setText(formatDate(h.createdAt));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(h, position));
    }

    private String formatDate(String ts) {
        if (ts == null || ts.length() < 16) return "";
        try {
            String[] months = {"", "Jan","Feb","Mar","Apr","Mei","Jun",
                               "Jul","Agt","Sep","Okt","Nov","Des"};
            String[] parts = ts.split("T");
            String[] d = parts[0].split("-");
            String time = parts[1].substring(0, 5);
            int m = Integer.parseInt(d[1]);
            return d[2] + " " + months[m] + " " + d[0] + ", " + time;
        } catch (Exception e) { return ts; }
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvWord, tvCat, tvDate;
        Button   btnDelete;
        VH(View v) {
            super(v);
            tvEmoji   = v.findViewById(R.id.tvEmoji);
            tvWord    = v.findViewById(R.id.tvWord);
            tvCat     = v.findViewById(R.id.tvCat);
            tvDate    = v.findViewById(R.id.tvDate);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
