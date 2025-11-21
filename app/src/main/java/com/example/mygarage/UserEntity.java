package com.example.mygarage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String firstName;
    public String lastName;
    public String email;   // unique logically
    public int age;
    public String password;
}
