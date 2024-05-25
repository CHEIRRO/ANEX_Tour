package com.example.kursach.DB.Entertainments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentsDBH extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "entertainmentDB";

    // Таблица развлечений
    private static final String TABLE_ENTERTAINMENTS = "entertainments";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    // Таблица избранного
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";  // Признак избранного

    public EntertainmentsDBH(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createEntertainmentsTable = "CREATE TABLE " + TABLE_ENTERTAINMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createEntertainmentsTable);

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IS_FAVORITE + " INTEGER" + ")";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTERTAINMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean addEntertainment(Entertainment entertainment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entertainment.getTitle());
        values.put(COLUMN_DESCRIPTION, entertainment.getDescription());

        long result = db.insert(TABLE_ENTERTAINMENTS, null, values);
        db.close();
        return result != -1;
    }

    public List<Entertainment> getAllEntertainments() {
        List<Entertainment> entertainments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ENTERTAINMENTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Entertainment entertainment = new Entertainment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                entertainments.add(entertainment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entertainments;
    }

    public boolean addEntertainmentToFavorites(Entertainment entertainment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, entertainment.getTitle());
        values.put(COLUMN_DESCRIPTION, entertainment.getDescription());
        values.put(COLUMN_IS_FAVORITE, 1); // Добавляем признак избранного

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public List<Entertainment> getFavoriteEntertainments() {
        List<Entertainment> entertainments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Entertainment entertainment = new Entertainment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                entertainments.add(entertainment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entertainments;
    }

    public boolean removeFavorite1(long entertainmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FAVORITES, COLUMN_ID + " = ?", new String[]{String.valueOf(entertainmentId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<Entertainment> searchEntertainmentsByTitle(String title) {
        List<Entertainment> entertainments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ENTERTAINMENTS, null, COLUMN_TITLE + " LIKE ?", new String[]{"%" + title + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Entertainment entertainment = new Entertainment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                entertainments.add(entertainment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return entertainments;
    }

    public boolean deleteEntertainment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ENTERTAINMENTS, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}

