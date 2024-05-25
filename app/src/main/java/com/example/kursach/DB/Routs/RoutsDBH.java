package com.example.kursach.DB.Routs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RoutsDBH extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "routsDB";

    // Таблица маршрутов
    private static final String TABLE_ROUTS = "routs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_DESCRIPTION = "description";

    // Таблица избранного
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";  // Признак избранного

    public RoutsDBH(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRoutsTable = "CREATE TABLE " + TABLE_ROUTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_LENGTH + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createRoutsTable);

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_LENGTH + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IS_FAVORITE + " INTEGER" + ")";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean addRout(Rout rout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, rout.getTitle());
        values.put(COLUMN_LENGTH, rout.getLength());
        values.put(COLUMN_DESCRIPTION, rout.getDescription());

        long result = db.insert(TABLE_ROUTS, null, values);
        db.close();
        return result != -1;
    }

    public List<Rout> getAllRouts() {
        List<Rout> routs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROUTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Rout rout = new Rout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                routs.add(rout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return routs;
    }

    public boolean addRoutToFavorites(Rout rout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, rout.getTitle());
        values.put(COLUMN_LENGTH, rout.getLength());
        values.put(COLUMN_DESCRIPTION, rout.getDescription());
        values.put(COLUMN_IS_FAVORITE, 1); // Добавляем признак избранного

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public List<Rout> getFavoriteRouts() {
        List<Rout> routs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Rout rout = new Rout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                routs.add(rout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return routs;
    }

    public boolean removeFavorite(long routId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FAVORITES, COLUMN_ID + " = ?", new String[]{String.valueOf(routId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<Rout> searchRoutsByTitle(String title) {
        List<Rout> routs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROUTS, null, COLUMN_TITLE + " LIKE ?", new String[]{"%" + title + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Rout rout = new Rout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                routs.add(rout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return routs;
    }
    public boolean deleteRout(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ROUTS, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
