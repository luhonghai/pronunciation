package com.cmg.android.bbcaccentamt.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cmg.android.bbcaccentamt.utils.FileHelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by luhonghai on 12/23/14.
 */
public class ScoreDBAdapter {

    public static class PronunciationScore {
        private int id;
        private String word;
        private float score;
        private String dataId;
        private Date timestamp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }

        public Date getTimestamp() {
            if (timestamp == null) return new Date(System.currentTimeMillis());
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getUserVoiceModel(Context context) throws IOException {
            if (dataId == null || dataId.length() == 0) return null;
            File modelSource = new File(FileHelper.getPronunciationScoreDir(context), dataId + FileHelper.JSON_EXTENSION);
            if (modelSource.exists()) {
                return FileUtils.readFileToString(modelSource, "UTF-8");
            }
            return null;
        }
    }

    public static final String KEY_ROWID = "_id";
    public static final String KEY_WORD = "word";
    public static final String KEY_SCORE = "score";
    public static final String KEY_DATA_ID = "data_id";
    public static final String KEY_TIMESTAMP = "timestamp";
    private static final String TAG = "ScoreDBAdapter";

    private static final String DATABASE_NAME = "score";
    private static final String DATABASE_TABLE = "pronunciation";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table pronunciation (_id integer primary key autoincrement, "
                    + " word text not null, "
                    + " data_id text not null, "
                    + " timestamp date not null, "
                    + "score integer not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ScoreDBAdapter(Context ctx)
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
    public ScoreDBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public long insert(PronunciationScore score)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_WORD, score.getWord());
        initialValues.put(KEY_DATA_ID, score.getDataId());
        initialValues.put(KEY_SCORE, score.getScore());
        initialValues.put(KEY_TIMESTAMP, dateFormat.format(score.getTimestamp()));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean delete(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID +
                "=" + rowId, null) > 0;
    }

    public Cursor getAllTime()
    {
        return db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_WORD,
                        KEY_DATA_ID,
                        KEY_SCORE,
                        KEY_TIMESTAMP},
                null,
                null,
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                null);
    }

    public Cursor getAll()
    {
        return db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_WORD,
                        KEY_DATA_ID,
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
                                KEY_WORD,
                                KEY_DATA_ID,
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

    public Cursor getByWord(String word) throws SQLException
    {
        return db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_WORD,
                                KEY_DATA_ID,
                                KEY_SCORE,
                                KEY_TIMESTAMP},
                        KEY_WORD + "=?",
                        new String[]{word},
                        null,
                        null,
                KEY_TIMESTAMP + " DESC",
                "30");
    }

    public Cursor getLatestByWord(String word) throws SQLException
    {
        return db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_WORD,
                        KEY_DATA_ID,
                        KEY_SCORE,
                        KEY_TIMESTAMP},
                KEY_WORD + "=?",
                new String[]{word},
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                "1");
    }

    public boolean update(PronunciationScore score)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_WORD, score.getWord());
        args.put(KEY_DATA_ID, score.getDataId());
        args.put(KEY_SCORE, score.getScore());
        args.put(KEY_TIMESTAMP, dateFormat.format(score.getTimestamp()));

        return db.update(DATABASE_TABLE, args,
                KEY_ROWID + "=" + score.getId(), null) > 0;
    }

    public Collection<PronunciationScore> toCollection(final Cursor cursor) throws ParseException {
        Collection<PronunciationScore> list = new ArrayList<PronunciationScore>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PronunciationScore score = new PronunciationScore();
            score.setDataId(cursor.getString(cursor.getColumnIndex(KEY_DATA_ID)));
            score.setScore(cursor.getFloat(cursor.getColumnIndex(KEY_SCORE)));
            score.setWord(cursor.getString(cursor.getColumnIndex(KEY_WORD)));
            score.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
            score.setTimestamp(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))));
            list.add(score);
            cursor.moveToNext();
        }
        return list;
    }
}