package com.example.mygarage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VehicleDao {

    @Insert
    long insert(VehicleEntity vehicle);

    @Update
    int update(VehicleEntity vehicle);

    @Delete
    int delete(VehicleEntity vehicle);

    @Query("SELECT * FROM vehicles WHERE userId = :userId ORDER BY vehicleName ASC")
    List<VehicleEntity> getAllForUser(long userId);

    @Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
    VehicleEntity getById(long id);

    @Query("SELECT * FROM vehicles WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    VehicleEntity getLatestVehicleForUser(long userId);
}
