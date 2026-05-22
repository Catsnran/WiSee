package com.wisee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_history")
public class WordHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String word;
    public String category;
    public String emoji;
    public String description;
    public String imagePath;   // cache path ilustrasi AI (nullable)
    public String createdAt;   // ISO timestamp

    public WordHistory() {}

    public WordHistory(int userId, String word, String category,
                       String emoji, String description, String createdAt) {
        this.userId = userId;
        this.word = word;
        this.category = category;
        this.emoji = emoji;
        this.description = description;
        this.createdAt = createdAt;
    }
}
