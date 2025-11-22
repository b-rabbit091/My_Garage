package com.example.mygarage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long vehicleId;
    public String category;
    public double amount;
    public long dateMillis;
    public String note;
}
