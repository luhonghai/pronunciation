package com.cmg.android.bbcaccent.extra;

import com.cmg.android.bbcaccent.adapter.ListMenuAdapter;
import com.cmg.android.bbcaccent.fragment.DetailFragment;
import com.cmg.android.bbcaccent.fragment.FeedbackFragment;
import com.cmg.android.bbcaccent.fragment.FreeStyleFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonFragment;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.fragment.info.AboutFragment;
import com.cmg.android.bbcaccent.fragment.info.HelpFragment;
import com.cmg.android.bbcaccent.fragment.info.LicenceFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonCollectionFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonLevelFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonObjectiveFragment;

/**
 * Created by luhonghai on 23/10/2015.
 */
public enum FragmentState {
    NULL(null),
    FREE_STYLE(FreeStyleFragment.class, ListMenuAdapter.MenuItem.FREESTYLE, ""),
    LESSON_LEVEL(LessonLevelFragment.class, ListMenuAdapter.MenuItem.LESSON, ""),
    LESSON_MAIN(LessonFragment.class, null, ""),
    LESSON_OBJECTIVE(LessonObjectiveFragment.class, null, ""),
    LESSON_COLLECTION(LessonCollectionFragment.class, null, ""),
    HELP(HelpFragment.class, ListMenuAdapter.MenuItem.HELP),
    SETTINGS(Preferences.class, ListMenuAdapter.MenuItem.SETTING),
    ABOUT(AboutFragment.class, ListMenuAdapter.MenuItem.ABOUT),
    LICENCE(LicenceFragment.class, ListMenuAdapter.MenuItem.LICENCE),
    FEEDBACK(FeedbackFragment.class, ListMenuAdapter.MenuItem.FEEDBACK),
    FREE_STYLE_DETAIL(DetailFragment.class, "")
    ;
    public Class<?> clazz;

    public ListMenuAdapter.MenuItem menuItem;

    public String title;

    FragmentState(Class<?> clazz) {
        this.clazz = clazz;
    }

    FragmentState(Class<?> clazz, ListMenuAdapter.MenuItem menuItem) {
        this(clazz);
        this.menuItem = menuItem;
    }

    FragmentState(Class<?> clazz, ListMenuAdapter.MenuItem menuItem, String title) {
        this(clazz, menuItem);
        this.title = title;
    }

    FragmentState(Class<?> clazz, String title) {
        this.clazz = clazz;
        this.title = title;
    }

    public static FragmentState fromMenuItem(ListMenuAdapter.MenuItem menuItem) {
        for (FragmentState state : values()) {
            if (state.menuItem == menuItem) return state;
        }
        return NULL;
    }

    public static FragmentState fromFragmentClassName(String className) {
        for (FragmentState state : values()) {
            if (state.clazz != null && state.clazz.getName().equals(className)) return state;
        }
        return NULL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        if (title == null) return (menuItem == null ? "" : menuItem.toString());
        return title;
    }

    @Override
    public String toString() {
        return clazz == null ? "null" : clazz.getName();
    }
}
