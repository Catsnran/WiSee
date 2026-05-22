package com.wisee.service;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wisee.model.WordHistory;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WordHistoryDao_Impl implements WordHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WordHistory> __insertionAdapterOfWordHistory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WordHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWordHistory = new EntityInsertionAdapter<WordHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `word_history` (`id`,`userId`,`word`,`category`,`emoji`,`description`,`imagePath`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final WordHistory entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.userId);
        if (entity.word == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.word);
        }
        if (entity.category == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.category);
        }
        if (entity.emoji == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.emoji);
        }
        if (entity.description == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.description);
        }
        if (entity.imagePath == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.imagePath);
        }
        if (entity.createdAt == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.createdAt);
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM word_history WHERE id = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM word_history WHERE userId = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final WordHistory history) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfWordHistory.insertAndReturnId(history);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteById(final int id, final int userId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, userId);
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public int deleteAll(final int userId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, userId);
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<WordHistory> getByUser(final int userId) {
    final String _sql = "SELECT * FROM word_history WHERE userId = ? ORDER BY id DESC LIMIT 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final List<WordHistory> _result = new ArrayList<WordHistory>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final WordHistory _item;
        _item = new WordHistory();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _item.userId = _cursor.getInt(_cursorIndexOfUserId);
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _item.word = null;
        } else {
          _item.word = _cursor.getString(_cursorIndexOfWord);
        }
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _item.category = null;
        } else {
          _item.category = _cursor.getString(_cursorIndexOfCategory);
        }
        if (_cursor.isNull(_cursorIndexOfEmoji)) {
          _item.emoji = null;
        } else {
          _item.emoji = _cursor.getString(_cursorIndexOfEmoji);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _item.imagePath = null;
        } else {
          _item.imagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _item.createdAt = null;
        } else {
          _item.createdAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countUniqueWords(final int userId) {
    final String _sql = "SELECT COUNT(DISTINCT word) FROM word_history WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getFavoriteCategory(final int userId) {
    final String _sql = "SELECT category FROM word_history WHERE userId = ? GROUP BY category ORDER BY COUNT(*) DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if (_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
