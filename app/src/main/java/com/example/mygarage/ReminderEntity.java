package com.example.mygarage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class ReminderEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long vehicleId;
    public String title;
    public long dueMillis;
    public boolean enabled;
}
