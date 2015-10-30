package com.cmg.android.bbcaccent.fragment.lesson;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.LessonLevelAdapter;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LevelScore;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonHistoryDBAdapterService;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonLevelFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_level, null);
        ButterKnife.bind(this, root);
        final UserProfile userProfile = Preferences.getCurrentProfile();
        if (userProfile != null && userProfile.getSelectedCountry() != null) {
            try {
                Cursor cursor = LessonDBAdapterService.getInstance().cursorAllLevel(userProfile.getSelectedCountry().getId());
                recyclerView.setAdapter(new LessonLevelAdapter(getContext(), cursor, new LessonLevelAdapter.OnSelectLevel() {
                    @Override
                    public void onSelectLevel(LessonLevel level) {
                        SwitchFragmentParameter parameter
                                = new SwitchFragmentParameter(true, true, true);
                        parameter.setTitle(level.getName());
                        Bundle bundle = getArguments();
                        if (bundle == null) bundle = new Bundle();
                        bundle.putString(LessonLevel.class.getName(), MainApplication.toJson(level));
                        MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                LessonObjectiveFragment.class,
                                parameter,
                                bundle);
                    }

                    @Override
                    public LessonLevel bindLevelData(LessonLevel level) {
                        try {
                            LevelScore levelScore = LessonHistoryDBAdapterService.getInstance().getLevelScore(userProfile.getUsername(),
                                                userProfile.getSelectedCountry().getId(), level.getId());
                            if (levelScore != null) {
                                level.setScore(levelScore.getScore());
                                level.setActive(levelScore.isEnable());
                            } else {
                                level.setScore(-1);
                            }
                        } catch (LiteDatabaseException e) {
                            SimpleAppLog.error("could not get level score",e);
                        }
                        return level;
                    }
                }));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not get lesson level from database", e);
            }
        } else {
            SimpleAppLog.error("No user profile or country found");
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
