package com.cmg.android.bbcaccent.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.PhoneScoreAdapter;
import com.cmg.android.bbcaccent.adapter.viewholder.QuestionViewHolder;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.question.Question;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.cmg.android.bbcaccent.dictionary.DictionaryListener;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalkerFactory;
import com.cmg.android.bbcaccent.extra.BreakDownAction;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.lesson.LessonCollectionFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonFragment;
import com.cmg.android.bbcaccent.fragment.tab.FragmentTab;
import com.cmg.android.bbcaccent.fragment.tab.GraphFragmentParent;
import com.cmg.android.bbcaccent.fragment.tab.HistoryFragment;
import com.cmg.android.bbcaccent.fragment.tab.TipFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.helper.PopupShowcaseHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.view.ShowcaseHelper;
import com.cmg.android.bbcaccent.view.SlidingUpPanelLayout;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.cmg.android.bbcaccent.view.dialog.CenterFullPaddingDialog;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by luhonghai on 12/22/14.
 */
public class DetailFragment extends BaseFragment implements RecordingView.OnAnimationListener {

    private DisplayingState displayingState;

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    @Bind(R.id.btnAudio)
    CircleCardView btnAudio;

    @Bind(R.id.cvNext)
    CircleCardView btnNext;

    @Bind(R.id.cvRefresh)
    CircleCardView btnRedo;

    @Bind(R.id.txtWord)
    AlwaysMarqueeTextView txtWord;

    @Bind(R.id.listViewScore)
    HListView hListView;

    @Bind(R.id.panelSlider)
    SlidingUpPanelLayout panelSlider;

    @Bind(R.id.rlBottomAction)
    RelativeLayout rlBottomAction;

    @Bind(R.id.rlActionContainer)
    RelativeLayout rlActionContainer;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserVoiceModel model;

    private PlayerHelper player;

    private ButtonState lastState;

    private boolean isPlaying = false;

    private int receiverListenerId;

    private ShowcaseHelper showcaseHelper;

    private void initDetailView(View root) {
        recordingView.setAnimationListener(this);
    }

    private LessonFragment.ViewState viewState;

    private PopupShowcaseHelper popupShowcaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail, null);
        ButterKnife.bind(this, root);
        Bundle bundle = getArguments();
        Gson gson = new Gson();
        model = gson.fromJson(bundle.getString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString()), UserVoiceModel.class);
        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.RESET_TIMING_HELP) {
                    if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                }
            }

            @Override
            public void onDataUpdate(String data, int type) {
                switch (type) {
                    case FragmentTab.TYPE_SELECT_PHONEME_GRAPH:
                        mTabHost.setCurrentTab(0);
                        break;
                }
            }

            @Override
            public void onHistoryAction(final UserVoiceModel model, String word, int type) {
                if (word != null && word.length() > 0) {
                    if (model != null) {
                        switch (type) {
                            case HistoryFragment.CLICK_LIST_ITEM:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showData(model, true);
                                        } catch (Exception e) {
                                            SimpleAppLog.error("Could not show data", e);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                } else {
                    if (type == HistoryFragment.CLICK_RECORD_BUTTON) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeDetail();
                            }
                        });
                    }
                }
            }
        });
        initSlider(root);
        showcaseHelper = new ShowcaseHelper(getActivity());
        boolean isCompleted = true;
        if (bundle.containsKey(LessonFragment.ViewState.class.getName())) {
            viewState = MainApplication.fromJson(bundle.getString(LessonFragment.ViewState.class.getName()), LessonFragment.ViewState.class);
            recyclerView.setAdapter(new QuestionAdapter());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            for (Question question : viewState.questions) {
                if (!question.isRecorded()) {
                    isCompleted = false;
                    break;
                }
            }
            if (isCompleted) {
                if (viewState.isLesson) {
                    rlActionContainer.setVisibility(View.VISIBLE);
                }
                int totalScore = 0;
                int totalQuestions = 0;
                for (Question q : viewState.questions) {
                    if (q.isRecorded()) {
                        totalQuestions++;
                        int totalQuestionScore = 0;
                        for (Integer i : q.getScoreHistory()) {
                            totalQuestionScore += i;
                        }
                        totalScore += Math.round((float) totalQuestionScore / q.getScoreHistory().size());
                    } else {
                        break;
                    }
                }
                if (totalQuestions == viewState.questions.size()) {
                    final int avgScore = Math.round((float) totalScore / totalQuestions);
                    if (viewState.isLesson) {
                        final Dialog dialog = new DefaultCenterDialog(getActivity(), R.layout.dialog_overall_score);
                        TextView textView = ButterKnife.findById(dialog, R.id.tv_content);
                        textView.setText(String.format(Locale.getDefault(), "Your overall score for %s is", viewState.lessonCollection.getTitle()));
                        final RecordingView recordingView = ButterKnife.findById(dialog, R.id.main_recording_view);
                        recordingView.setAnimationListener(new RecordingView.OnAnimationListener() {
                            @Override
                            public void onAnimationMax() {
                                recordingView.stopPingAnimation();
                            }

                            @Override
                            public void onAnimationMin() {

                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                                recordingView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recordingView.startPingAnimation(getActivity(), 2000, avgScore, true, false);
                                    }
                                });
                            }
                        }, 1900);
                    } else {
                        boolean isPassed = avgScore >= viewState.lessonTest.getPercentPass();
                        int layoutId = isPassed ? R.layout.dialog_passed_test : R.layout.dialog_failed_test;
                        final Dialog dialog = new CenterFullPaddingDialog(getActivity(), layoutId);
                        final RecordingView recordingView = ButterKnife.findById(dialog, R.id.main_recording_view);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                MainBroadcaster.getInstance().getSender().sendPopBackStackFragment(3);
                                if (getActivity() != null && !Preferences.getCurrentProfile().isPro()) {
                                    ((MainActivity) getActivity()).showActiveFullVersionDialog();
                                }
                            }
                        });
                        recordingView.setAnimationListener(new RecordingView.OnAnimationListener() {
                            @Override
                            public void onAnimationMax() {
                                recordingView.stopPingAnimation();
                            }

                            @Override
                            public void onAnimationMin() {

                            }
                        });

                        if (isPassed) {
                            CircleCardView btnShare = (CircleCardView) dialog.findViewById(R.id.btnShare);
                            if (btnShare != null) {
                                btnShare.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((MainActivity)getActivity()).share("accenteasy - English pronunciation app", String.format(
                                                Locale.getDefault(), "I scored %d in my pronunciation test for speaking English with a good accent. " +
                                                        "How did you do?",
                                                avgScore), "https://play.google.com/store/apps/details?id=com.cmg.android.bbcaccent");
                                    }
                                });
                                AndroidHelper.updateShareButton(btnShare);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                    recordingView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recordingView.startPingAnimation(getActivity(), 2000, avgScore, true, true);
                                        }
                                    });
                                }
                            }, 1900);
                        } else {
                            final RecordingView recordingViewPassed = ButterKnife.findById(dialog, R.id.main_recording_view_passed);
                            recordingViewPassed.setAnimationListener(new RecordingView.OnAnimationListener() {
                                @Override
                                public void onAnimationMax() {
                                    recordingViewPassed.stopPingAnimation();
                                }

                                @Override
                                public void onAnimationMin() {

                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                    recordingView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recordingView.startPingAnimation(getActivity(), 2000, avgScore, true, true);
                                        }
                                    });
                                    recordingViewPassed.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recordingViewPassed.startPingAnimation(getActivity(), 2000, Math.round(viewState.lessonTest.getPercentPass()), true, true);
                                        }
                                    });
                                }
                            }, 1900);
                        }


                    }
                }
            }
        }
        createPopupShowcaseHelper(viewState == null, viewState != null && viewState.isLesson, isCompleted);
        initTabHost(root);
        initDetailView(root);
        try {
            showData(model, false);
        } catch (Exception e) {
            SimpleAppLog.error("Could not open database",e);
        }

        rlBottomAction.setVisibility(viewState != null ? View.VISIBLE : View.INVISIBLE);
        registerGestureSwipe(root);
        return root;
    }

    private void createPopupShowcaseHelper(boolean isFreeStyle, boolean isLesson, boolean isCompleted) {
        PopupShowcaseHelper.HelpItem[] helpItems = null;
        if (!isFreeStyle) {
            if (isLesson) {
                if (isCompleted) {
                    helpItems = new PopupShowcaseHelper.HelpItem[]{
                            PopupShowcaseHelper.HelpItem.PHONEME_HELP,
                            PopupShowcaseHelper.HelpItem.LISTEN_AGAIN,
                            PopupShowcaseHelper.HelpItem.TRY_AGAIN,
                            PopupShowcaseHelper.HelpItem.NEXT_LESSON,
                            PopupShowcaseHelper.HelpItem.REDO_LESSON
                    };
                } else {
                    helpItems = new PopupShowcaseHelper.HelpItem[]{
                            PopupShowcaseHelper.HelpItem.PHONEME_HELP,
                            PopupShowcaseHelper.HelpItem.LISTEN_AGAIN,
                            PopupShowcaseHelper.HelpItem.TRY_AGAIN,
                            PopupShowcaseHelper.HelpItem.NEXT_QUESTION
                    };
                }
            } else {
                if (isCompleted) {
                } else {
                    helpItems = new PopupShowcaseHelper.HelpItem[]{
                            PopupShowcaseHelper.HelpItem.PHONEME_HELP,
                            PopupShowcaseHelper.HelpItem.LISTEN_AGAIN,
                            PopupShowcaseHelper.HelpItem.NEXT_QUESTION
                    };
                }
            }
        } else {
            helpItems = new PopupShowcaseHelper.HelpItem[]{
                    PopupShowcaseHelper.HelpItem.PHONEME_HELP,
                    PopupShowcaseHelper.HelpItem.LISTEN_AGAIN,
                    PopupShowcaseHelper.HelpItem.TRY_AGAIN
            };
        }
        if (helpItems != null) {
            if (popupShowcaseHelper != null) popupShowcaseHelper.recycle();
            popupShowcaseHelper = new PopupShowcaseHelper(getActivity(),
                    helpItems
                    ,
                    new PopupShowcaseHelper.OnSelectHelpItem() {
                        @Override
                        public void onSelectHelpItem(PopupShowcaseHelper.HelpItem helpItem) {
                            if (showcaseHelper == null) return;
                            switch (helpItem) {
                                case PHONEME_HELP:
                                    if (hListView != null && hListView.getChildCount() > 0) {
                                        showcaseHelper.showHelp(new ShowcaseHelper.HelpState(getViewByPosition(0, hListView), getString(R.string.help_phoneme_info)));
                                    }
                                    break;
                                case LISTEN_AGAIN:
                                    showcaseHelper.showHelp(new ShowcaseHelper.HelpState(btnAudio, getString(R.string.help_hear_last_attempt)));
                                    break;
                                case TRY_AGAIN:
                                    showcaseHelper.showHelp(new ShowcaseHelper.HelpState(recordingView, getString(R.string.help_reattempt_swipe_back)));
                                    break;
                                case NEXT_QUESTION:
                                    int index = 0;
                                    if (viewState != null && viewState.questions.size() > 0) {
                                        for (int i = 0; i < viewState.questions.size(); i++) {
                                            Question q = viewState.questions.get(i);
                                            if (q.isEnabled() && !q.isRecorded()) {
                                                index = i;
                                                break;
                                            }
                                        }
                                        showcaseHelper.showHelp(new ShowcaseHelper.HelpState(getViewByPosition(index, recyclerView),
                                                getString(R.string.help_next_question)));
                                    }
                                    break;
                                case NEXT_LESSON:
                                    showcaseHelper.showHelp(new ShowcaseHelper.HelpState(btnNext, getString(R.string.help_next_lesson)));
                                    break;
                                case REDO_LESSON:
                                    showcaseHelper.showHelp(new ShowcaseHelper.HelpState(btnRedo, getString(R.string.help_redo_lesson)));
                                    break;
                            }
                        }
                    });
            popupShowcaseHelper.resetTiming();
        }
    }

    @OnClick(R.id.main_recording_view)
    public void onClickScoreView() {
        closeDetail();
    }

    private void initSlider(final View root) {
        LinearLayout rlSliderContent = (LinearLayout) root.findViewById(R.id.rlSliderContent);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        SlidingUpPanelLayout.LayoutParams layoutParams = (SlidingUpPanelLayout.LayoutParams) rlSliderContent.getLayoutParams();
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        final int halfHeight = (displayMetrics.heightPixels - actionBarHeight) / 2;
        layoutParams.height = halfHeight;
        rlSliderContent.setLayoutParams(layoutParams);
        panelSlider.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float test = (halfHeight - root.findViewById(R.id.btnSlider).getHeight()) / (float) halfHeight;
                if (slideOffset > test) {
                    //txtPhonemes.setVisibility(View.INVISIBLE);
                } else {
                    //txtPhonemes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (panelSlider != null)
                    panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }, 1000);
    }

    private void showData(UserVoiceModel userVoiceModel, boolean showScore) throws LiteDatabaseException {
        model = userVoiceModel;
        recordingView.setScore(model.getScore());
        txtWord.setText(model.getWord());
        AndroidHelper.updateMarqueeTextView(txtWord, !AndroidHelper.isCorrectWidth(txtWord, model.getWord()));
        if (model.getScore() >= 80.0) {
            lastState = ButtonState.GREEN;
        } else if (model.getScore() >= 45.0) {
            lastState = ButtonState.ORANGE;
        } else {
            lastState = ButtonState.RED;
        }
        switchButtonState();
        //showPhonemes();
        showPhonemesListView();
        if (showScore) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(getActivity(), 2000, model.getScore(), true, true);
        }
    }

    private void closeDetail() {
        MainBroadcaster.getInstance().getSender().sendPopBackStackFragment();
    }

    private void showPhonemesListView() {
        if (model == null || model.getResult() == null) return;
        List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
        SphinxResult.PhonemeScore[] scores;
        if (phonemeScores == null || phonemeScores.size() == 0) {
            //TODO validate if not contain any scores
        } else {
            scores = new SphinxResult.PhonemeScore[phonemeScores.size()];
            phonemeScores.toArray(scores);
            for (final SphinxResult.PhonemeScore score : scores) {
                String ipa = score.getIpa();
                if (ipa == null || ipa.length() == 0) {
                    IPAMapArpabet ipaMapArpabet = getIpaByArpabet(score.getName().toUpperCase());
                    if (ipaMapArpabet != null) ipa = ipaMapArpabet.getIpa();
                }
                score.setIpa(ipa);
            }
            PhoneScoreAdapter scoreAdapter = new PhoneScoreAdapter(MainApplication.getContext(), scores, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(view);
                }
            });
            hListView.setAdapter(scoreAdapter);
            scoreAdapter.notifyDataSetChanged();
        }
    }

    private IPAMapArpabet getIpaByArpabet(String arpabet) {
        try {
            return LessonDBAdapterService.getInstance().findObject("arpabet = ?", new String[]{arpabet.toUpperCase()}, IPAMapArpabet.class);
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("could not load ipa from database",e);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(getActivity(), 1000, model.getScore(), true, true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (model != null) {
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handlerDialog.removeCallbacksAndMessages(null);
        if (popupShowcaseHelper != null) {
            popupShowcaseHelper.recycle();
            popupShowcaseHelper = null;
        }
        MainBroadcaster.getInstance().unregister(receiverListenerId);
        try {
            recordingView.stopPingAnimation();
        } catch (Exception ex) {

        }
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }
        ButterKnife.unbind(this);
    }

    private void initTabHost(View root) {
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }
        });
        Bundle bundle = new Bundle();
        if (viewState != null) {
            bundle.putString(MainBroadcaster.Filler.LESSON.toString(), MainBroadcaster.Filler.LESSON.toString());
        }
        bundle.putString(MainBroadcaster.Filler.Key.WORD.toString(), model.getWord());
        Gson gson = new Gson();
        bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(model));

        addTabImage(R.drawable.tab_graph,
                GraphFragmentParent.class, getString(R.string.tab_graph), bundle);
        addTabImage(R.drawable.tab_history,
                HistoryFragment.class, getString(R.string.tab_history), bundle);
        if (viewState == null) {
            addTabImage(R.drawable.tab_tip,
                    TipFragment.class, getString(R.string.tab_tip), bundle);
        }
    }

    private void addTabImage(int drawableId, Class<?> c, String labelId, Bundle bundle)
    {
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId).setIndicator(null, getResources().getDrawable(drawableId));
        mTabHost.addTab(spec, c, bundle);

    }

    @OnClick({R.id.rlVoiceExample, R.id.txtWord, R.id.btnAudio})
    public void onClick(View v) {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        switch (v.getId()) {
            case R.id.rlVoiceExample:
            case R.id.txtPhoneme:
            case R.id.txtWord:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        DictionaryWalker walker = DictionaryWalkerFactory.getInstance();
                        walker.setListener(new DictionaryListener() {
                            @Override
                            public void onDetectWord(final DictionaryItem dItem) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        play(dItem.getAudioFile());
                                    }
                                });
                            }

                            @Override
                            public void onWordNotFound(DictionaryItem dItem, final FileNotFoundException ex) {

                            }

                            @Override
                            public void onError(DictionaryItem dItem, final Exception ex) {

                            }
                        });
                        walker.execute(model.getWord());
                        return null;
                    }
                }.execute();
                break;
            case R.id.btnAudio:
                if (isPlaying) {
                    try {
                        player.stop();
                        isPlaying = false;
                        switchButtonState();
                    } catch (Exception ex) {

                    }
                } else {
                    playAudio();
                }
                break;
        }
    }

    private void showDialog(View v) {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        final SphinxResult.PhonemeScore score = (SphinxResult.PhonemeScore) v.getTag();
        final float totalScore = score.getTotalScore();
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_WhiteDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.phone_info_dialog);
        int color;
        if (totalScore >= 80.0f) {
            color = R.color.app_green;
        } else if (totalScore >= 45.0f) {
            color = R.color.app_orange;
        } else {
            color = R.color.app_red;
        }
        ((ImageView)((CircleCardView) dialog.findViewById(R.id.btnGraph)).getChildAt(0)).setColorFilter(ColorHelper.getColor(color));
        ((ImageView)((CircleCardView) dialog.findViewById(R.id.btnAudioPhoneme)).getChildAt(0)).setColorFilter(ColorHelper.getColor(color));
        ((CardView) dialog.findViewById(R.id.content)).setCardBackgroundColor(ColorHelper.getColor(color));
        dialog.findViewById(R.id.btnGraph).setTag(score.getName());
        dialog.findViewById(R.id.btnGraph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainBroadcaster.getInstance().getSender().sendUpdateData(v.getTag().toString(), FragmentTab.TYPE_SELECT_PHONEME_GRAPH);
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.txtPhonemeScore)).setText(String.format(Locale.getDefault(), "%d%%", Math.round(totalScore)));
        String ipa = score.getIpa();
        final IPAMapArpabet ipaMapArpabet = getIpaByArpabet(score.getName().toUpperCase());
        if (ipa == null || ipa.length() == 0) {
            if (ipaMapArpabet != null) ipa = ipaMapArpabet.getIpa();
        }
        ((TextView) dialog.findViewById(R.id.txtPhoneme)).setText(ipa);
        dialog.findViewById(R.id.btnAudioPhoneme).setTag(score.getName());
        dialog.findViewById(R.id.btnAudioPhoneme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipaMapArpabet == null || ipaMapArpabet.getMp3Url() == null || ipaMapArpabet.getMp3Url().length() == 0 ) return;
                File ipaAudio = new File(FileHelper.getCachedFilePath(ipaMapArpabet.getMp3Url()));
                playFile(ipaAudio);
            }
        });
        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }
        });
        handlerDialog.removeCallbacksAndMessages(null);
        handlerDialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && dialog.isShowing())
                    dialog.dismiss();
            }
        }, 10000);
    }

    private Handler handlerDialog = new Handler();

    private void play(String file) {
        if (file == null || file.length() == 0) return;
        play(new File(file));
    }

    private void play(File file) {
        if (!file.exists()) return;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    try {
                        player.stop();
                    } catch (Exception ex) {

                    }
                    isPlaying = false;
                    switchButtonState();
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchButtonState() {
        switchButtonState(lastState);
    }

    private void switchButtonState(ButtonState state) {
        switch (state) {
            case PLAYING:
                btnAudio.setEnabled(true);
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_close);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                break;
            case RED:
                btnAudio.setEnabled(true);
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                txtWord.setTextColor(ColorHelper.getColor(R.color.app_red));
                break;
            case ORANGE:
                btnAudio.setEnabled(true);
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_orange));
                txtWord.setTextColor(ColorHelper.getColor(R.color.app_orange));
                break;
            case GREEN:
                btnAudio.setEnabled(true);
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                txtWord.setTextColor(ColorHelper.getColor(R.color.app_green));
                break;
            default:
                break;
        }
        if (model != null) {
            File audio = new File(model.getAudioFile());
            if (!audio.exists()) {
                btnAudio.setEnabled(false);
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
            }
        }
    }

    private void playAudio() {
        switchButtonState(ButtonState.PLAYING);
        playFile(new File(model.getAudioFile()));
    }

    private void playFile(File file) {
        if (!file.exists()) return;
        isPlaying = true;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    isPlaying = false;
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchButtonState();
                            }
                        });
                    }

                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
            isPlaying = false;
        }
    }

    @Override
    public void onAnimationMax() {
        if (displayingState == DisplayingState.WAIT_FOR_ANIMATION_MAX) {
            AppLog.logString("On animation max");
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
//            showcaseHelper.showHelp(ShowcaseHelper.HelpKey.DETAIL_SELECT_PHONEME,
//                    new ShowcaseHelper.HelpState(getViewByPosition(0, hListView), "<b>Press</b> a phoneme for even more detail"),
//                    new ShowcaseHelper.HelpState(recordingView, "<b>Press</> the score to return to previous screen"));
        }
    }

    private View getViewByPosition(int pos, HListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private View getViewByPosition(int pos, RecyclerView recyclerView) {
        return recyclerView.getChildAt(pos);
    }

    @Override
    public void onAnimationMin() {

    }

    enum DisplayingState {
        DEFAULT,
        WAIT_FOR_ANIMATION_MAX
    }

    enum ButtonState {
        RED,
        ORANGE,
        GREEN,
        PLAYING
    }

    class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

        @Override
        public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.question_item, parent, false);
            return new QuestionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final QuestionViewHolder holder, final int position) {
            if (viewState == null || viewState.questions.size() == 0) return;
            final Question question = viewState.questions.get(position);
            int bgColor = R.color.app_gray;
            String prefix = viewState.isLesson ? "Q" : "T";
            String text = String.format(Locale.getDefault(), prefix + "%d", position + 1);
            if (question.isEnabled()) {
                holder.cardView.setTag(position);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (question.isEnabled()) {
                            MainApplication.getContext().setBreakDownAction(new BreakDownAction<Question>(BreakDownAction.Type.SELECT_QUESTION, question));
                            closeDetail();
                        }
                    }
                });
                if (question.isRecorded()) {
                    int totalScore = 0;
                    for (Integer score : question.getScoreHistory()) {
                        totalScore += score;
                    }
                    int avgScore = Math.round((float) totalScore / question.getScoreHistory().size());
                    text = String.format(Locale.getDefault(), "%d", avgScore);
                    if (avgScore >= 80) {
                        bgColor = R.color.app_green;
                    } else if (avgScore >= 45) {
                        bgColor = R.color.app_orange;
                    } else {
                        bgColor = R.color.app_red;
                    }
                } else {
                    bgColor = R.color.app_purple;
                }
            }
            holder.txtScore.setText(text);
            holder.cardView.setCardBackgroundColor(ColorHelper.getColor(bgColor));
        }

        @Override
        public int getItemCount() {
            return viewState == null ? 0 : viewState.questions.size();
        }
    }

    @OnClick(R.id.cvMenu)
    public void clickMenuAction() {
//        MainApplication.setBreakDownAction(new BreakDownAction<Object>(BreakDownAction.Type.SELECT_MENU));
//        closeDetail();
        MainBroadcaster.getInstance().getSender().sendPopBackStackFragment(2);
    }

    @OnClick(R.id.cvRefresh)
    public void clickRedoAction() {
        MainApplication.getContext().setBreakDownAction(new BreakDownAction<Object>(BreakDownAction.Type.SELECT_REDO));
        closeDetail();
    }

    @OnClick(R.id.cvNext)
    public void clickNextAction() {
        SimpleAppLog.debug("Detect break down action type SELECT_NEXT. Current lesson ID " + viewState.lessonCollection.getId());
        try {
            LessonCollection nextLesson = LessonDBAdapterService.getInstance().getNextLessonOnCurrentObjective(
                    Preferences.getCurrentProfile().getSelectedCountry().getId(),
                    viewState.lessonLevel.getId(),
                    viewState.objective.getId(),
                    viewState.lessonCollection.getId()
            );
            if (nextLesson != null) {
                MainApplication.getContext().setBreakDownAction(new BreakDownAction<Object>(BreakDownAction.Type.SELECT_NEXT));
                closeDetail();
            } else {
                SimpleAppLog.debug("No next lesson found for current objective. Move to next objective lesson");
                final Objective objective = LessonDBAdapterService.getInstance().getNextObjectiveOnCurrentLevel(
                        Preferences.getCurrentProfile().getSelectedCountry().getId(),
                        viewState.lessonLevel.getId(),
                        viewState.objective.getId()
                );
                final SwitchFragmentParameter parameter
                        = new SwitchFragmentParameter(true, true, true);
                final Bundle b = new Bundle();
                if (objective != null) {
                    SimpleAppLog.debug("Found next objective " + objective.getId() + ". name: " + objective.getName());
                    parameter.setTitle(viewState.lessonLevel.getName() + " - " + objective.getName());
                    b.putString(Objective.class.getName(), MainApplication.toJson(objective));
                    b.putString(LessonLevel.class.getName(), MainApplication.toJson(viewState.lessonLevel));
                } else {
                    SimpleAppLog.debug("No next objective found");
                }

                final MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.executeAction(new MainActivity.MainAction() {
                        @Override
                        public void execute(MainActivity mainActivity) {
                            MainApplication.enablePopbackFragmentAnimation = false;
                            mainActivity.popBackStackFragment();
                            mainActivity.popBackStackFragment();
                            mainActivity.popBackStackFragment();
                            MainApplication.enablePopbackFragmentAnimation = true;
                            if (objective != null) {
                                MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                        LessonCollectionFragment.class,
                                        parameter,
                                        b);
                                final Dialog dialog = new DefaultCenterDialog(activity, R.layout.showcase_content);
                                ((TextView) dialog.findViewById(R.id.tv_content)).setText(objective.getDescription());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                }, getResources().getInteger(android.R.integer.config_mediumAnimTime));
                            } else {
                                // Will show dialog for testing
                                //TODO check if lite version
                                final Dialog dialog = new DefaultCenterDialog(activity, R.layout.showcase_content);
                                ((TextView) dialog.findViewById(R.id.tv_content)).setText(R.string.help_to_test_last_object);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                }, 100);
                            }
                        }
                    });
                }
            }
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("could not get next lesson",e);
        }
    }

    @Override
    protected void onSwipeLeftToRight() {
        closeDetail();
    }
}
