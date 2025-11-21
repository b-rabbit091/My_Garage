package com.example.mygarage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vehicles")
public class VehicleEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long userId;

    public String vehicleName;
    public String model;
    public String make;
    public String purchaseDate;
    public int mileage;
}
