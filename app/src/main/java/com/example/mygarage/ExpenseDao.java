package com.example.mygarage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    long insert(ExpenseEntity expense);

    @Query("SELECT * FROM expenses WHERE vehicleId = :vehicleId ORDER BY dateMillis DESC")
    List<ExpenseEntity> getByVehicle(long vehicleId);

    @Query("SELECT SUM(amount) FROM expenses WHERE vehicleId = :vehicleId")
    Double getTotalForVehicle(long vehicleId);
}
