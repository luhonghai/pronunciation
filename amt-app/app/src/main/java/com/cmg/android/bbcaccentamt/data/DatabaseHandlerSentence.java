package com.cmg.android.bbcaccentamt.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cmg.android.bbcaccentamt.common.Common;

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
    public static final String TABLE_RECORDERSENTENCE = "recordersentence";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "sentence";
    public static final String KEY_ISDELETED = "isdelete";
    public static final String KEY_FILENAME = "filename";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_STATUS = "status";
    public static final String KEY_VERSION = "version";
    public static final String KEY_ID_SENTENCE = "idSentence";

    private static final String CREATE_SENTENCES_TABLE = "CREATE TABLE " + TABLE_SENTENCE + "("
            + KEY_ID + " TEXT, " + KEY_NAME + " TEXT, " + KEY_STATUS + " INTEGER, " + KEY_VERSION + " INTEGER, " +  KEY_ISDELETED + " INTEGER " +  ")" ;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_RECORDERSENTENCE + "("
            + KEY_ID + " TEXT, " +KEY_ID_SENTENCE+ " TEXT," + KEY_FILENAME + " TEXT, " + KEY_ACCOUNT + " TEXT, " + KEY_STATUS + " INTEGER, " + KEY_VERSION + " INTEGER, " + KEY_ISDELETED + " INTEGER" + ")" ;


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
        db.execSQL(CREATE_SENTENCES_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENTENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDERSENTENCE);

        // Create tables again
        onCreate(db);
    }

    public void addSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, sentenceModel.getID());
        values.put(KEY_NAME, sentenceModel.getSentence());
        values.put(KEY_STATUS,0);
        values.put(KEY_VERSION, sentenceModel.getVersion());
        values.put(KEY_ISDELETED, sentenceModel.isDeleted());
        // Inserting Row
        db.insert(TABLE_SENTENCE, null, values);
        db.close();
    }

    public SentenceModel getSentence(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SENTENCE, new String[]{KEY_ID,
                        KEY_NAME, KEY_STATUS, KEY_VERSION, KEY_ISDELETED}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            SentenceModel sentenceModel = new SentenceModel(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
            cursor.close();
            return sentenceModel;
        }
        return null;
    }
    //not use
    public List<SentenceModel> getAllSentence() {
        List<SentenceModel> sentenceModelsList = new ArrayList<SentenceModel>();

        String selectQuery = "SELECT "+ KEY_ID +"," + KEY_NAME +"," + KEY_STATUS +"," + KEY_VERSION +","+KEY_ISDELETED+ " FROM " + TABLE_SENTENCE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SentenceModel sentenceModel = new SentenceModel();
                sentenceModel.setID(cursor.getString(0));
                sentenceModel.setSentence(cursor.getString(1));
                sentenceModel.setStatus(Integer.parseInt(cursor.getString(2)));
                sentenceModel.setVersion(Integer.parseInt(cursor.getString(3)));
                sentenceModel.setIsDeleted(Integer.parseInt(cursor.getString(4)));
                sentenceModelsList.add(sentenceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sentenceModelsList;
    }


    public List<SentenceModel> getAllSentenceWithStatusandAccount(String account) {
        List<SentenceModel> sentenceModelsList = new ArrayList<SentenceModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_SENTENCE + " WHERE " + KEY_ID + " NOT IN " + "(" + " SELECT " + KEY_ID_SENTENCE + " FROM " + TABLE_RECORDERSENTENCE + " WHERE " + KEY_ACCOUNT +"='"+account+"' and " + KEY_STATUS +"!='"+Common.NOT_RECORD+"'"+") and " +KEY_ISDELETED +"="+Common.ISDELETED_FALSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SentenceModel sentenceModel = new SentenceModel();
                sentenceModel.setID(cursor.getString(0));
                sentenceModel.setSentence(cursor.getString(1));
                sentenceModel.setStatus(Common.NOT_RECORD);
                sentenceModel.setVersion(Integer.parseInt(cursor.getString(3)));
                sentenceModel.setIsDeleted(Integer.parseInt(cursor.getString(4)));
                sentenceModelsList.add(sentenceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sentenceModelsList;
    }

    public List<SentenceModel> getSentenceWithAccountandStatus(String account, int status) {
        List<SentenceModel> sentenceModelList = new ArrayList<SentenceModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SENTENCE + " WHERE " + KEY_ID + " IN " + "(" + " SELECT " + KEY_ID_SENTENCE + " FROM " + TABLE_RECORDERSENTENCE + " WHERE " + KEY_ACCOUNT +"='"+account+"' and " + KEY_STATUS +"="+status+") and " +KEY_ISDELETED +"="+Common.ISDELETED_FALSE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SentenceModel sentenceModel = new SentenceModel();
                sentenceModel.setID(cursor.getString(0));
                sentenceModel.setSentence(cursor.getString(1));
                sentenceModel.setStatus(status);
                sentenceModel.setVersion(Integer.parseInt(cursor.getString(3)));
                sentenceModel.setIsDeleted(Integer.parseInt(cursor.getString(4)));
                sentenceModelList.add(sentenceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sentenceModelList;
    }




    public int updateSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sentenceModel.getID());
        values.put(KEY_NAME, sentenceModel.getSentence());
        values.put(KEY_STATUS, sentenceModel.getStatus());
        values.put(KEY_VERSION, sentenceModel.getVersion());
        values.put(KEY_ISDELETED, sentenceModel.isDeleted());


        return db.update(TABLE_SENTENCE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sentenceModel.getID()) });
    }

    public void deleteSentence(SentenceModel sentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SENTENCE, KEY_ID + "=?",
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
        return db.query(TABLE_SENTENCE, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_STATUS,
                        KEY_VERSION,
                        KEY_ISDELETED},
                null,
                null,
                null,
                null,
                null);
    }
    public Cursor search(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_SENTENCE, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_STATUS,
                        KEY_VERSION,
                        KEY_ISDELETED},
                "sentence LIKE ?"+ " and " + KEY_ISDELETED + "=? ",
                new String[]{String.valueOf(s), String.valueOf(Common.ISDELETED_FALSE)}, null, null, null, null);
    }
    public DatabaseHandlerSentence open() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return this;
    }

    public int getLastedVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastest=0;
        Cursor cursor= db.rawQuery("SELECT MAX(version) FROM " + TABLE_SENTENCE, null);
        if (cursor != null)
            cursor.moveToFirst();
        try {
            lastest= Integer.parseInt(cursor.getString(0));
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return lastest;

    }

    //RecorderSentence
    public List<RecorderSentenceModel> getAllSentenceUpload(String account) {
        List<RecorderSentenceModel> recorderSentenceModels = new ArrayList<RecorderSentenceModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORDERSENTENCE, new String[]{KEY_ID,KEY_ID_SENTENCE,
                        KEY_FILENAME, KEY_ACCOUNT,KEY_STATUS, KEY_VERSION, KEY_ISDELETED}, KEY_STATUS + "=?" + " and " + KEY_ACCOUNT + "=? " + "and " +KEY_ISDELETED +"=?",
                new String[]{String.valueOf(Common.RECORDED_BUT_NOT_UPLOAD), String.valueOf(account), String.valueOf(Common.ISDELETED_FALSE)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecorderSentenceModel recorderSentenceModel = new RecorderSentenceModel();
                recorderSentenceModel.setID(cursor.getString(0));
                recorderSentenceModel.setIdSentence(cursor.getString(1));
                recorderSentenceModel.setFileName(cursor.getString(2));
                recorderSentenceModel.setAccount(cursor.getString(3));
                recorderSentenceModel.setStatus(Integer.parseInt(cursor.getString(4)));
                recorderSentenceModel.setVersion(Integer.parseInt(cursor.getString(5)));
                recorderSentenceModel.setIsDelete(Integer.parseInt(cursor.getString(6)));
                recorderSentenceModels.add(recorderSentenceModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return recorderSentenceModels;
    }

    public void addRecorderSentence(RecorderSentenceModel recorderSentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, recorderSentenceModel.getID());
        values.put(KEY_ID_SENTENCE, recorderSentenceModel.getIdSentence());
        values.put(KEY_FILENAME, recorderSentenceModel.getFileName());
        values.put(KEY_ACCOUNT, recorderSentenceModel.getAccount());
        values.put(KEY_STATUS, recorderSentenceModel.getStatus());
        values.put(KEY_VERSION, recorderSentenceModel.getVersion());
        values.put(KEY_ISDELETED, recorderSentenceModel.isDelete());


        // Inserting Row
        db.insert(TABLE_RECORDERSENTENCE, null, values);
        db.close();
    }

    public RecorderSentenceModel getRecorderSentence(String idSentence, String account) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECORDERSENTENCE, new String[]{KEY_ID, KEY_ID_SENTENCE
                        , KEY_ACCOUNT, KEY_FILENAME, KEY_STATUS, KEY_VERSION, KEY_ISDELETED}, KEY_ID_SENTENCE + "=?" + " and " + KEY_ACCOUNT + "=?",
                new String[]{String.valueOf(idSentence), String.valueOf(account)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RecorderSentenceModel recorderSentenceModel = new RecorderSentenceModel(
                cursor.getString(0),cursor.getString(1),
                cursor.getString(2),cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)));
        return recorderSentenceModel;
    }

    public int updateRecorder(int version, int status,int isdeleted, String idSentence, String account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VERSION,version);
        values.put(KEY_ISDELETED, isdeleted);
        values.put(KEY_STATUS, status);

       int i = db.update(TABLE_RECORDERSENTENCE, values, KEY_ID_SENTENCE + "=?" + " and " + KEY_ACCOUNT + "=?",
                new String[]{String.valueOf(idSentence), String.valueOf(account)});
        db.close();
        return i;


    }


    public void deleteAllIdSentence() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDERSENTENCE, null, null);
        db.close();
    }

    public int updateSentence(RecorderSentenceModel recorderSentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, recorderSentenceModel.getID());
        values.put(KEY_ID_SENTENCE, recorderSentenceModel.getIdSentence());
        values.put(KEY_FILENAME, recorderSentenceModel.getFileName());
        values.put(KEY_ACCOUNT, recorderSentenceModel.getAccount());
        values.put(KEY_STATUS, recorderSentenceModel.getStatus());
        values.put(KEY_VERSION, recorderSentenceModel.getVersion());
        values.put(KEY_ISDELETED, recorderSentenceModel.isDelete());


        return db.update(TABLE_RECORDERSENTENCE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(recorderSentenceModel.getID()) });
    }

    public int getLastedVersionRecorder() {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastest=0;
        Cursor cursor= db.rawQuery("SELECT MAX("+KEY_VERSION+") FROM " + TABLE_RECORDERSENTENCE,null);
        if (cursor != null)
            cursor.moveToFirst();
        try {
            lastest= Integer.parseInt(cursor.getString(0));
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return lastest;



    }
    public void deleteRecorderSentence(RecorderSentenceModel recorderSentenceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDERSENTENCE, KEY_ID_SENTENCE + " = ?" + " and " + KEY_ACCOUNT + "=?",
                new String[]{String.valueOf(recorderSentenceModel.getIdSentence()),String.valueOf(recorderSentenceModel.getAccount())});
        db.close();
    }

}
