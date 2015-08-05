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
 * Created by CMGT400 on 7/10/2015.
 */
public class DatabaseHandlerSentence extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sentencesManager";

    private static final String TABLE_SENTENCE = "sentences";

    public static final String KEY_ID = "_id";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NAME = "sentence";
    public static final String KEY_INDEX = "stt";
    private static DatabaseHandlerSentence sInstance;

    public static synchronized DatabaseHandlerSentence getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHandlerSentence(context.getApplicationContext());
        }
        return sInstance;
    }


    public DatabaseHandlerSentence(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENTENCES_TABLE = "CREATE TABLE " + TABLE_SENTENCE + "("
                + KEY_ID + " TEXT," + KEY_NAME + " TEXT," + KEY_STATUS + " INTEGER," + KEY_INDEX + " INTEGER" + ")" ;
        db.execSQL(CREATE_SENTENCES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENTENCE);

        // Create tables again
        onCreate(db);
    }

    public void addSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sentenceModel.getID());
        values.put(KEY_NAME, sentenceModel.getSentence());
        values.put(KEY_STATUS, sentenceModel.getStatus());
        values.put(KEY_INDEX, sentenceModel.getIndex());
        // Inserting Row
        db.insert(TABLE_SENTENCE, null, values);
        db.close();
    }

    public SentenceModel getSentence(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SENTENCE, new String[] { KEY_ID,
                        KEY_NAME, KEY_STATUS, KEY_INDEX}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SentenceModel sentenceModel = new SentenceModel(cursor.getString(0),cursor.getString(1),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));
        return sentenceModel;
    }

    public List<SentenceModel> getAllSentence() {
        List<SentenceModel> sentenceModelsList = new ArrayList<SentenceModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_SENTENCE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SentenceModel sentenceModel = new SentenceModel();
                sentenceModel.setID(cursor.getString(0));
                sentenceModel.setSentence(cursor.getString(1));
                sentenceModel.setStatus(Integer.parseInt(cursor.getString(2)));
                sentenceModel.setIndex(Integer.parseInt(cursor.getString(3)));
                sentenceModelsList.add(sentenceModel);
            } while (cursor.moveToNext());
        }

        return sentenceModelsList;
    }


    public int updateSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sentenceModel.getID());
        values.put(KEY_NAME, sentenceModel.getSentence());
        values.put(KEY_STATUS, sentenceModel.getStatus());
        values.put(KEY_INDEX, sentenceModel.getIndex());


        return db.update(TABLE_SENTENCE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sentenceModel.getID()) });
    }

    public void deleteSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENTENCE, KEY_ID + " = ?",
                new String[]{String.valueOf(sentenceModel.getID())});
        db.close();
    }

    public void deleteStatus(int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENTENCE, KEY_STATUS + " != ?",
                new String[]{String.valueOf(status)});
        db.close();
    }

    public void deleteAllSentence() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENTENCE, null, null);
        db.close();
    }

    public int getSentenceCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SENTENCE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public Cursor getAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_SENTENCE, new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_STATUS,
                        KEY_INDEX},
                null,
                null,
                null,
                null,
                null);
    }
    public Cursor search(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_SENTENCE, new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_STATUS,
                        KEY_INDEX},
                "sentence LIKE ?",
                new String[] {s + "%"},
                null,
                null,
                null);
    }
    public DatabaseHandlerSentence open() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return this;
    }

}
