package com.cmg.android.bbcaccent.fragment.lesson;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.LessonCollectionAdapter;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LessonScore;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonHistoryDBAdapterService;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonCollectionFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.cvItemContainer)
    CardView cvItemContainer;

    private LessonLevel level;
    private Objective objective;

    private Handler handlerDialog = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_collection, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    private void initData() {
        final UserProfile userProfile = Preferences.getCurrentProfile();
        if (userProfile != null && userProfile.getSelectedCountry() != null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.containsKey(LessonLevel.class.getName())) {
                    level = MainApplication.fromJson(bundle.getString(LessonLevel.class.getName()), LessonLevel.class);
                }
                if (bundle.containsKey(Objective.class.getName())) {
                    objective = MainApplication.fromJson(bundle.getString(Objective.class.getName()), Objective.class);
                }
            }
            if (level != null && objective != null) {
                final String levelName = level.getName();
                try {
                    Cursor cursor = LessonDBAdapterService.getInstance().cursorLessonCollectionByObjective(
                            userProfile.getSelectedCountry().getId(),
                            level.getId(),
                            objective.getId());
                    SimpleAppLog.debug("Found "
                            + (cursor != null ? cursor.getCount() : 0)
                            + " lesson collection of objective id "
                            + objective.getId() + " and level id "
                            + level.getId() + ", level name " + levelName);
                    LessonCollectionAdapter adapter = new LessonCollectionAdapter(getContext(), cursor, new LessonCollectionAdapter.OnSelectLessonCollection() {
                        @Override
                        public void onSelectLessonCollection(LessonCollection lessonCollection) {
                            Bundle b = bundle;
                            if (b == null) b = new Bundle();
                            SwitchFragmentParameter parameter
                                    = new SwitchFragmentParameter(true, true, true);
                            b.putString(LessonCollection.class.getName(), MainApplication.toJson(lessonCollection));
                            b.putString(MainBroadcaster.Filler.Key.TYPE.toString(), Objective.class.getName());
                            MainApplication.getContext().setLessonViewState(null);
                            LessonHistoryDBAdapterService.getInstance().emptyHistory();
                            MainApplication.getContext().setSelectedWord(null);
                            MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                    LessonFragment.class,
                                    parameter,
                                    b);
                        }

                        @Override
                        public LessonCollection bindLessonCollectionData(LessonCollection lessonCollection) {
                            try {
                                LessonScore lessonScore = LessonHistoryDBAdapterService.getInstance().getLessonScore(userProfile.getUsername(),
                                                    userProfile.getSelectedCountry().getId(), level.getId(), objective.getId(), lessonCollection.getId());
                                if (lessonScore != null) {
                                    lessonCollection.setScore(lessonScore.getScore());
                                } else {
                                    lessonCollection.setScore(-1);
                                }
                            } catch (LiteDatabaseException e) {
                                SimpleAppLog.error("Could not get lesson score",e);
                            }
                            return lessonCollection;
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter.notifyDataSetChanged();
                } catch (LiteDatabaseException e) {
                    SimpleAppLog.error("Could not list lesson collection from database", e);
                }
                final Dialog dialog = new DefaultCenterDialog(getActivity(), R.layout.showcase_content);
                ((HtmlTextView)dialog.findViewById(R.id.tv_content)).setHtmlFromString(objective.getDescription(), true);
                cvItemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                        handlerDialog.removeCallbacksAndMessages(null);
                        handlerDialog.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing()) dialog.dismiss();
                            }
                        }, 15000);
                    }
                });
            } else {
                SimpleAppLog.error("Could not found objective or level");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        handlerDialog.removeCallbacksAndMessages(null);
    }
}
