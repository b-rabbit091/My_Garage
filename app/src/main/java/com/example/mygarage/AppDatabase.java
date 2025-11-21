package com.example.mygarage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                UserEntity.class,
                VehicleEntity.class,
                ExpenseEntity.class,
                ReminderEntity.class
        },
        version = 4,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract VehicleDao vehicleDao();

    public abstract ExpenseDao expenseDao();

    public abstract ReminderDao reminderDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "mygarage.db"
                            )
                            .fallbackToDestructiveMigration()
                          .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
