package com.example.mygarage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    long insert(ReminderEntity reminder);

    @Update
    int update(ReminderEntity reminder);

    @Delete
    int delete(ReminderEntity reminder);

    @Query("SELECT * FROM reminders WHERE vehicleId = :vehicleId ORDER BY dueMillis ASC")
    List<ReminderEntity> getByVehicle(long vehicleId);

    @Query("SELECT * FROM reminders WHERE id = :id LIMIT 1")
    ReminderEntity getById(long id);
}
