package com.example.kursach.DB.Sights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SightsDBH extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sights.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_SIGHTS = "sights";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    public SightsDBH(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSightsTable = "CREATE TABLE " + TABLE_SIGHTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DATA + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createSightsTable);

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DATA + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IS_FAVORITE + " INTEGER" + ")";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean addSight(Sight sight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, sight.getTitle());
        values.put(COLUMN_DATA, sight.getData());
        values.put(COLUMN_DESCRIPTION, sight.getDescription());

        long result = db.insert(TABLE_SIGHTS, null, values);
        db.close();
        return result != -1;
    }

    public List<Sight> getAllSights() {
        List<Sight> sightsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SIGHTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Sight sight = new Sight(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                sightsList.add(sight);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sightsList;
    }

    public boolean addSightToFavorites(Sight sight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, sight.getTitle());
        values.put(COLUMN_DATA, sight.getData());
        values.put(COLUMN_DESCRIPTION, sight.getDescription());
        values.put(COLUMN_IS_FAVORITE, 1);

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public List<Sight> getFavoriteSights() {
        List<Sight> favoriteSights = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Sight sight = new Sight(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                favoriteSights.add(sight);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoriteSights;
    }

    public boolean removeFavoriteSight(int sightId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FAVORITES, COLUMN_ID + " = ?", new String[]{String.valueOf(sightId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<Sight> searchSightsByTitle(String title) {
        List<Sight> sights = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SIGHTS, null, COLUMN_TITLE + " LIKE ?", new String[]{"%" + title + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Sight sight = new Sight(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                sights.add(sight);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sights;
    }

    public boolean deleteSight(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SIGHTS, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
