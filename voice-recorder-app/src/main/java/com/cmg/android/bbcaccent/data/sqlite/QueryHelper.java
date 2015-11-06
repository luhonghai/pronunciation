package com.cmg.android.bbcaccent.data.sqlite;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Created by luhonghai on 23/10/2015.
 */
public enum QueryHelper {
    select_all_level_by_country("select_all_level_by_country"),
    select_all_objective_by_level("select_all_objective_by_level"),
    select_all_test_by_level("select_all_test_by_level"),
    select_all_lesson_collection_by_objective("select_all_lesson_collection_by_objective"),
    select_all_lesson_collection_by_test("select_all_lesson_collection_by_test"),
    select_all_question_by_lesson_collection("select_all_question_by_lesson_collection"),
    select_all_words_by_question("select_all_words_by_question"),
    search_word("search_word"),
    select_prev_level_of_level("select_prev_level_of_level")
    ;
    String name;

    QueryHelper(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        try {
            return IOUtils.toString(MainApplication.getContext().getAssets().open("database/queries/" + name + ".sql"), "UTF-8");
        } catch (IOException e) {
            SimpleAppLog.error("",e);
        }
        return "";
    }
}
