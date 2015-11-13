package com.cmg.android.bbcaccent.fragment.lesson;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.LessonObjectiveAdapter;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LevelScore;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.test.LessonTest;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonHistoryDBAdapterService;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonObjectiveFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.cvItemContainer)
    CardView cvItemContainer;

    @Bind(R.id.cvScoreContainer)
    CircleCardView cvScoreContainer;

    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @Bind(R.id.txtScore)
    TextView txtScore;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    private LessonTest lessonTest;

    private LessonLevel level;

    private Country selectedCountry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_objective, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }
    @OnClick(R.id.cvItemContainer)
    public void clickTestLesson() {
        if (lessonTest != null) {
            SwitchFragmentParameter parameter
                    = new SwitchFragmentParameter(true, true, true);
            Bundle bundle = getArguments();
            if (bundle == null) bundle = new Bundle();
            bundle.putString(LessonTest.class.getName(), MainApplication.toJson(lessonTest));
            try {
                Cursor cursor =  LessonDBAdapterService.getInstance().cursorLessonCollectionByTest(selectedCountry.getId(),
                        level.getId(), lessonTest.getId());
                if (cursor != null && cursor.moveToFirst()) {
                    LessonCollection lessonCollection = LessonDBAdapterService.getInstance().toObject(
                            cursor,
                            LessonCollection.class
                    );
                    bundle.putString(LessonCollection.class.getName(), MainApplication.toJson(lessonCollection));
                    bundle.putString(MainBroadcaster.Filler.Key.TYPE.toString(), LessonTest.class.getName());
                    LessonHistoryDBAdapterService.getInstance().emptyHistory();
                    MainApplication.getContext().setLessonViewState(null);
                    MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                            LessonFragment.class,
                            parameter,
                            bundle);
                } {
                    SimpleAppLog.debug("No lesson collection found for test id " + lessonTest.getId());
                }
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not get lesson collection from test id " + lessonTest.getId()
                        + ". country id " + selectedCountry.getId() + ". Level id " + level.getId(),e);
            }

        }
    }

    private void initData() {
        final UserProfile userProfile = Preferences.getCurrentProfile();
        if (userProfile != null && userProfile.getSelectedCountry() != null) {
            selectedCountry = userProfile.getSelectedCountry();
            final Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey(LessonLevel.class.getName())) {
                level = MainApplication.fromJson(bundle.getString(LessonLevel.class.getName()), LessonLevel.class);
            }
            if (level != null) {
                final String levelName = level.getName();
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
                int testScore = level.getScore();
                try {
                    Cursor cursor = LessonDBAdapterService.getInstance().cursorAllObjective(userProfile.getSelectedCountry().getId(), level.getId());
                    SimpleAppLog.debug("Found "
                            + (cursor != null ? cursor.getCount() : 0)
                            + " objective of level id " + level.getId() + ", level name " + levelName);
                    LessonObjectiveAdapter adapter = new LessonObjectiveAdapter(getContext(), cursor, new LessonObjectiveAdapter.OnSelectObjective() {
                        @Override
                        public void onSelectObjective(Objective objective) {
                            Bundle b = bundle;
                            if (b == null) {
                                b = new Bundle();
                            }
                            SwitchFragmentParameter parameter
                                    = new SwitchFragmentParameter(true, true, true);
                            parameter.setTitle(levelName + " - " + objective.getName());
                            b.putString(Objective.class.getName(), MainApplication.toJson(objective));
                            MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                    LessonCollectionFragment.class,
                                    parameter,
                                    b);
                        }

                        @Override
                        public Objective bindObjectiveData(Objective objective) {
                            try {
                                int score =LessonHistoryDBAdapterService.getInstance().getObjectiveScore(userProfile.getUsername(),
                                                        userProfile.getSelectedCountry().getId(),
                                                        level.getId(),
                                                        objective.getId());
                                objective.setScore(score);
                            } catch (LiteDatabaseException e) {
                                SimpleAppLog.error("Could not get objective score",e);
                            }
                            return objective;
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter.notifyDataSetChanged();
                    showTestScore(testScore);

                    Cursor mCursor = LessonDBAdapterService.getInstance().cursorAllTest(userProfile.getSelectedCountry().getId(), level.getId());
                    if (mCursor != null && mCursor.moveToFirst()) {
                        lessonTest = LessonDBAdapterService.getInstance().toObject(
                                mCursor,
                                LessonTest.class);
                    }
                } catch (LiteDatabaseException e) {
                    SimpleAppLog.error("Could not list objective from database", e);
                }
            }
        }
    }

    private void showTestScore(final int score) {
        int scoreBgColor;
        if (score >= 80) {
            scoreBgColor = R.color.app_green;
        } else if (score >= 45) {
            scoreBgColor = R.color.app_orange;
        } else if (score >= 0) {
            scoreBgColor = R.color.app_red;
        } else {
            scoreBgColor = R.color.app_gray;
        }
        cvScoreContainer.setCardBackgroundColor(
                MainApplication.getContext().getResources().getColor(scoreBgColor));
        cvItemContainer.setCardBackgroundColor(
                MainApplication.getContext().getResources().getColor(R.color.app_light_purple));
        txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(R.color.app_purple));
        txtTitle.setText("test");
        if (score >= 0) {
            txtScore.setText(String.format(Locale.getDefault(), "%d", score));
        } else {
            txtScore.setText("");
        }

        recordingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recordingView != null) {
                    recordingView.recycleView();
                    recordingView.startPingAnimation(getActivity(), 0, score, true, false);
                }
            }
        }, 100);
        recordingView.setAnimationListener(new RecordingView.OnAnimationListener() {
            @Override
            public void onAnimationMax() {
                recordingView.stopPingAnimation();
            }

            @Override
            public void onAnimationMin() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
