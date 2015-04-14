package com.cmg.android.bbcaccent.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by luhonghai on 12/23/14.
 */
public class PhonemeScoreDBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_PHONEME = "phoneme";
    public static final String KEY_SCORE = "score";

    public static final String KEY_TIMESTAMP = "timestamp";
    private static final String TAG = "PhonemeScoreDBAdapter";

    private static final String DATABASE_NAME = "phoneme";
    private static final String DATABASE_TABLE = "score";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table score (_id integer primary key autoincrement, "
                    + " phoneme text not null, "
                    + " timestamp date not null, "
                    + "score integer not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PhonemeScoreDBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        private Context currentContext;
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.currentContext = context;

        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    public PhonemeScoreDBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public long insert(SphinxResult.PhonemeScore score)
    {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONEME, score.getName());
        initialValues.put(KEY_SCORE, score.getTotalScore());
        initialValues.put(KEY_TIMESTAMP, dateFormat.format(new Date(System.currentTimeMillis())));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean delete(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID +
                "=" + rowId, null) > 0;
    }

    public Cursor getAll()
    {
        return db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_PHONEME,
                        KEY_SCORE,
                        KEY_TIMESTAMP},
                null,
                null,
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                "30");
    }

    public Cursor get(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_PHONEME,
                                KEY_SCORE,
                                KEY_TIMESTAMP},
                        KEY_ROWID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getByPhoneme(String phoneme) throws SQLException
    {
        return db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                            KEY_PHONEME,
                                KEY_SCORE,
                                KEY_TIMESTAMP},
                   KEY_PHONEME + "=?",
                        new String[]{phoneme},
                        null,
                        null,
                KEY_TIMESTAMP + " DESC",
                "30");
    }

    public Cursor getLatestByPhoneme(String phoneme) throws SQLException
    {
        return db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_PHONEME,
                        KEY_SCORE,
                        KEY_TIMESTAMP},
                KEY_PHONEME + "=?",
                new String[]{phoneme},
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                "1");
    }

    public Collection<SphinxResult.PhonemeScore> toCollection(final Cursor cursor) throws ParseException {
        Collection<SphinxResult.PhonemeScore> list = new ArrayList<SphinxResult.PhonemeScore>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SphinxResult.PhonemeScore score = new SphinxResult.PhonemeScore();
            score.setTotalScore(cursor.getFloat(cursor.getColumnIndex(KEY_SCORE)));
            score.setName(cursor.getString(cursor.getColumnIndex(KEY_PHONEME)));
            list.add(score);
            cursor.moveToNext();
        }
        return list;
    }
}
