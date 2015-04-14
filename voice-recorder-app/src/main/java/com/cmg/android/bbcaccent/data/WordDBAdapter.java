package com.cmg.android.bbcaccent.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cmg.android.bbcaccent.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by luhonghai on 12/23/14.
 */
public class WordDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_WORD = "word";
    public static final String KEY_PRONUNCIATION = "pronunciation";
    private static final String TAG = "WordDBAdapter";

    private static final String DATABASE_NAME = "word";
    private static final String DATABASE_TABLE = "pronunciation";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table pronunciation (_id integer primary key autoincrement, "
                    + " word text not null, "
                    + "pronunciation text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public WordDBAdapter(Context ctx)
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
            // Insert default word list
            String[] words = currentContext.getResources().getStringArray(R.array.words_list);
            for (String word : words) {
                String[] tmp = word.split("\\|");
                if (tmp.length == 2) {
                    String w = tmp[0].trim();
                    String p = tmp[1].trim();
              //      AppLog.logString("INSERT-> " + word + " | w: " + w + " p: " + p);
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(KEY_WORD, w);
                    initialValues.put(KEY_PRONUNCIATION, p);
                    db.insert(DATABASE_TABLE, null, initialValues);
                }
            }
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
    public WordDBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a title into the database---
    public long insert(String word, String pronunciation)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_WORD, word);
        initialValues.put(KEY_PRONUNCIATION, pronunciation);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean delete(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID +
                "=" + rowId, null) > 0;
    }


    public Cursor search(String s) {
        return db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_WORD,
                        KEY_PRONUNCIATION},
                "word LIKE ?",
                new String[] {s + "%"},
                null,
                null,
                null);
    }

    public Cursor getAll()
    {
        return db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_WORD,
                        KEY_PRONUNCIATION},
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor get(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_WORD,
                                KEY_PRONUNCIATION
                        },
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

    //---updates a title---
    public boolean update(long rowId, String word,
                               String pronunciation)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_WORD, word);
        args.put(KEY_PRONUNCIATION, pronunciation);

        return db.update(DATABASE_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Map<String,String> getPhonemeCMUvsIPA() {
        Map<String, String> maps = new Hashtable<String, String>();
        try {
            String source = IOUtils.toString(context.getResources().openRawResource(R.raw.phoneme_cmu_ipa), "UTF-8");
            String[] lines = source.split("\n");
            for (String line : lines) {
                if (line.trim().length() > 0) {
                    String[] data = line.split(" ");
                    if (data.length == 2) {
                        maps.put(data[0].trim().toUpperCase(), data[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }
}
