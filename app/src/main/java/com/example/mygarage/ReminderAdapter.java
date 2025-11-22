package com.example.mygarage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private final List<ReminderDisplayItem> items = new ArrayList<>();
    private final Context context;
    private final AppDatabase db;
    private final DateFormat dateFormat;
    private final DateFormat timeFormat;

    public ReminderAdapter(List<ReminderDisplayItem> data, Context ctx) {
        context = ctx;
        db = AppDatabase.getInstance(ctx.getApplicationContext());
        dateFormat = android.text.format.DateFormat.getDateFormat(ctx);
        timeFormat = android.text.format.DateFormat.getTimeFormat(ctx);
        if (data != null) {
            items.addAll(data);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ReminderDisplayItem item = items.get(position);

        h.remVehicle.setText(item.vehicleName);
        h.remTitle.setText(item.title);

        Date d = new Date(item.dueMillis);
        String dateStr = dateFormat.format(d);
        String timeStr = timeFormat.format(d);
        h.remDateTime.setText(dateStr + " • " + timeStr);

        if (item.note != null && !item.note.trim().isEmpty()) {
            h.remNote.setVisibility(View.VISIBLE);
            h.remNote.setText("Note: " + item.note.trim());
        } else {
            h.remNote.setVisibility(View.GONE);
        }

        h.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, RemindersActivity.class);
            intent.putExtra("reminderId", item.id);
            intent.putExtra("vehicleId", item.vehicleId);

            String type = null;
            String lower = item.title != null ? item.title.toLowerCase() : "";
            if (lower.startsWith("fuel:")) {
                type = "fuel";
            } else if (lower.startsWith("washing:")) {
                type = "washing";
            } else if (lower.startsWith("service:")) {
                type = "service";
            }
            if (type != null) {
                intent.putExtra("reminderType", type);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        h.btnDelete.setOnClickListener(v -> {
            ReminderEntity entity = db.reminderDao().getById(item.id);
            if (entity != null) {
                db.reminderDao().delete(entity);
            }
            int pos = h.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
            }
            Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView remVehicle;
        TextView remTitle;
        TextView remDateTime;
        TextView remNote;
        Button btnEdit;
        Button btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            remVehicle = itemView.findViewById(R.id.remVehicle);
            remTitle = itemView.findViewById(R.id.remTitle);
            remDateTime = itemView.findViewById(R.id.remDateTime);
            remNote = itemView.findViewById(R.id.remNote);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
