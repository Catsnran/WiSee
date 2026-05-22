package com.wisee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "users",
        indices = {@Index(value = "username", unique = true),
                   @Index(value = "email",    unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String displayName;
    public String email;
    public String passwordHash;   // SHA-256
    public String createdAt;      // ISO timestamp

    public User() {}

    public User(String username, String displayName, String email,
                String passwordHash, String createdAt) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }
}
