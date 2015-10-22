package com.cmg.android.bbcaccent.data.sqlite;

import com.luhonghai.litedb.LiteBaseDao;
import com.luhonghai.litedb.LiteDatabaseHelper;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class BaseDatabaseAdapter<T> extends LiteBaseDao<T> {

    /**
     * Constructor
     *
     * @param databaseHelper
     * @param tableClass
     */
    public BaseDatabaseAdapter(LiteDatabaseHelper databaseHelper, Class<T> tableClass) {
        super(databaseHelper, tableClass);
    }

    @Override
    @Deprecated
    public void open() {
        // Skip
    }

    @Override
    @Deprecated
    public void close() {
        // Skip
    }
}
