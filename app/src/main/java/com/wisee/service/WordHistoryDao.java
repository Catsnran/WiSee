package com.wisee.service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.wisee.model.WordHistory;
import java.util.List;

@Dao
public interface WordHistoryDao {

    @Insert
    long insert(WordHistory history);

    @Query("SELECT * FROM word_history WHERE userId = :userId ORDER BY id DESC LIMIT 100")
    List<WordHistory> getByUser(int userId);

    @Query("SELECT COUNT(DISTINCT word) FROM word_history WHERE userId = :userId")
    int countUniqueWords(int userId);

    @Query("DELETE FROM word_history WHERE id = :id AND userId = :userId")
    int deleteById(int id, int userId);

    @Query("DELETE FROM word_history WHERE userId = :userId")
    int deleteAll(int userId);

    // Returns only category column for favorite
    @Query("SELECT category FROM word_history WHERE userId = :userId " +
            "GROUP BY category ORDER BY COUNT(*) DESC LIMIT 1")
    String getFavoriteCategory(int userId);
}
