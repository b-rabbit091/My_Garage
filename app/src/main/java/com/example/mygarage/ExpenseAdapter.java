package com.example.mygarage;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final List<ExpenseEntity> items;
    private final Context context;

    public ExpenseAdapter(List<ExpenseEntity> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseEntity e = items.get(position);
        holder.category.setText(e.category);
        holder.amount.setText("$" + String.format("%.2f", e.amount));

        String dateStr = DateFormat.getDateFormat(context).format(new Date(e.dateMillis));
        holder.date.setText(dateStr);

        if (e.note != null && !e.note.isEmpty()) {
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setText(e.note);
        } else {
            holder.note.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView category, amount, date, note;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.expCategory);
            amount = itemView.findViewById(R.id.expAmount);
            date = itemView.findViewById(R.id.expDate);
            note = itemView.findViewById(R.id.expNote);
        }
    }
}
