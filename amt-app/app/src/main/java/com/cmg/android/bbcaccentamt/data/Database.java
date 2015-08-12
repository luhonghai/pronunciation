package com.cmg.android.bbcaccentamt.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMGT400 on 8/12/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "idsentencesManager";

    private static final String TABLE_IDSENTENCE = "idsentence";

    public static final String KEY_ID = "_id";
    public static final String KEY_IDSENTENCE = "idsentence";
    private static DatabaseHandlerSentence sInstance;

    public static synchronized DatabaseHandlerSentence getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHandlerSentence(context.getApplicationContext());
        }
        return sInstance;
    }


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENTENCES_TABLE = "CREATE TABLE " + TABLE_IDSENTENCE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IDSENTENCE + " TEXT" + ")" ;
        db.execSQL(CREATE_SENTENCES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IDSENTENCE);

        // Create tables again
        onCreate(db);
    }

    public void addSentence(SentenceUpload sentenceUpload) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sentenceUpload.getID());
        values.put(KEY_IDSENTENCE, sentenceUpload.getSentence());
        // Inserting Row
        db.insert(TABLE_IDSENTENCE, null, values);
        db.close();
    }

    public SentenceUpload getSentence(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IDSENTENCE, new String[]{KEY_ID,
                         KEY_IDSENTENCE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SentenceUpload sentenceUpload = new SentenceUpload(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        return sentenceUpload;
    }

    public List<SentenceUpload> getAllIdSentence() {
        List<SentenceUpload> sentenceUploads = new ArrayList<SentenceUpload>();

        String selectQuery = "SELECT  * FROM " + TABLE_IDSENTENCE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SentenceUpload sentenceUpload = new SentenceUpload();
                sentenceUpload.setID(Integer.parseInt(cursor.getString(0)));
                sentenceUpload.setSentence(cursor.getString(1));
                sentenceUploads.add(sentenceUpload);
            } while (cursor.moveToNext());
        }

        return sentenceUploads;
    }
    public void deleteAllIdSentence() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IDSENTENCE, null, null);
        db.close();
    }



}

