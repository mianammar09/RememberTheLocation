package com.imianammar.rememberthelocation.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.imianammar.rememberthelocation.Model.Note;
import com.imianammar.rememberthelocation.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "remember_the_location_db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_NOTES = "notes";

    // Common column names
    private static final String KEY_ID = "id";

    // Users table columns
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PASSWORD = "password";

    // Notes table columns
    private static final String KEY_LOCATION_NAME = "location_name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_PATH = "image_path";

    // Create tables queries
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT, " +
            KEY_ADDRESS + " TEXT, " +
            KEY_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_LOCATION_NAME + " TEXT, " +
            KEY_DESCRIPTION + " TEXT, " +
            KEY_IMAGE_PATH + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create tables again
        onCreate(db);
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "name=?", new String[]{username}, null, null, null);
        boolean taken = cursor.moveToFirst();
        cursor.close();
        return taken;
    }

    public long addUser(String name, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("password", password);

        long result = db.insert("users", null, values);
        db.close();

        return result;
    }

    public long addNote(String locationName, String description, String imagePath) {
        if (locationName.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_NAME, locationName);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_IMAGE_PATH, imagePath);

        long result = db.insert(TABLE_NOTES, null, values);
        db.close();

        return result;
    }


    @SuppressLint("Range")
    public List<Note> getAllNotes() {
        List<Note> notesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                note.setLocationName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                note.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));

                notesList.add(note);
            }
            cursor.close();
        } else {
            Log.e("DatabaseHelper", "Cursor is null");
        }

        db.close();

        return notesList;
    }


    @SuppressLint("Range")
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"name", "address", "password"},
                "name=?",
                new String[]{username},
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            cursor.close();
        }

        return user;
    }

    public boolean updateNote(int id, String locationName, String description, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("location_name", locationName);
        values.put("description", description);
        values.put("image_path", imagePath);

        int updatedRows = db.update("notes", values, "id=?", new String[]{String.valueOf(id)});

        return updatedRows > 0;
    }

    public boolean deleteNoteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deletedRows = db.delete(TABLE_NOTES, KEY_ID + "=?", new String[]{String.valueOf(id)});

        return deletedRows > 0;
    }

}
