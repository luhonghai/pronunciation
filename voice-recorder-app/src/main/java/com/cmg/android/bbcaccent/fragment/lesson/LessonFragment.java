package com.cmg.android.bbcaccent.fragment.lesson;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cmg.android.bbcaccent.LoginActivity;
import com.cmg.android.bbcaccent.MainActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.viewholder.QuestionViewHolder;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.question.Question;
import com.cmg.android.bbcaccent.data.dto.lesson.test.LessonTest;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.WordDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonHistoryDBAdapterService;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.cmg.android.bbcaccent.dictionary.DictionaryListener;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalkerFactory;
import com.cmg.android.bbcaccent.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccent.extra.BreakDownAction;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.fragment.DetailFragment;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.fragment.tab.FragmentTab;
import com.cmg.android.bbcaccent.fragment.tab.GraphFragmentParent;
import com.cmg.android.bbcaccent.fragment.tab.HistoryFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.helper.PopupShowcaseHelper;
import com.cmg.android.bbcaccent.http.ResponseData;
import com.cmg.android.bbcaccent.http.UploaderAsync;
import com.cmg.android.bbcaccent.http.common.FileCommon;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.utils.UUIDGenerator;
import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.view.ShowcaseHelper;
import com.cmg.android.bbcaccent.view.SlidingUpPanelLayout;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nineoldandroids.animation.Animator;

import org.apache.commons.io.FileUtils;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class LessonFragment extends BaseFragment implements RecordingView.OnAnimationListener, LocationListener {

    /**
     * Recording
     */
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final long PITCH_TIMEOUT = 1000;

    private static final long START_TIMEOUT = 1000;

    private static final long RECORD_MAX_LENGTH = 6000;

    private AnalyzingState analyzingState = AnalyzingState.DEFAULT;

    private ButtonState lastState;

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    @Bind(R.id.btnAnalyzing)
    CircleCardView btnAnalyzing;

    @Bind(R.id.btnAudio)
    CircleCardView btnAudio;

    @Bind(R.id.txtWord)
    AlwaysMarqueeTextView txtWord;

    @Bind(R.id.txtPhoneme)
    AlwaysMarqueeTextView txtPhonemes;

    @Bind(R.id.rlVoiceExample)
    View rlVoiceExample;

    @Bind(R.id.imgHourGlass)
    ImageButton imgHourGlass;

    @Bind(R.id.imgHelpHand)
    ImageButton imgHelpHand;

    @Bind(R.id.panelSlider)
    SlidingUpPanelLayout panelSlider;

    @Bind(R.id.txtDefinition)
    TextView txtDefinition;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.cvTip)
    CircleCardView cvTip;

    @Bind(R.id.rlSliderContent)
    LinearLayout rlSliderContent;

    private AndroidAudioInputStream audioStream;
    private AudioDispatcher dispatcher;
    private Thread runner;
    private double pitch = 0;
    private long lastDetectedPitchTime = -1;

    private AudioRecord audioInputStream;
    private int chanel;
    private int sampleRate;
    private int bufferSize;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isAutoStop = false;

    private PlayerHelper player;

    private long start;

    private String selectedWord;

    /**
     * Animation
     */
    private Animation fadeIn;
    private Animation fadeOut;

    private UploaderAsync uploadTask;

    /**
     * Score
     */
    private ScoreDBAdapter scoreDBAdapter;

    private WordDBAdapter dbAdapter;

    private AccountManager accountManager;

    private int receiverListenerId;

    private ViewState viewState;

    private ShowcaseHelper showcaseHelper;

    private QuestionAdapter questionAdapter;

    private BreakDownAction breakDownAction;

    private PopupShowcaseHelper popupShowcaseHelper;

    private PopupShowcaseHelper.HelpItem currentHelpItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Gson gson = new Gson();
        View root = inflater.inflate(R.layout.fragment_lesson, null);
        ButterKnife.bind(this, root);
        accountManager = new AccountManager(getActivity());
        initTabHost();
        initRecordingView();
        initAnimation();
        switchButtonStage(ButtonState.DISABLED);
        viewState = MainApplication.getContext().getLessonViewState();
        Bundle bundle = getArguments();
        if (viewState == null) {
            viewState = new ViewState();
            if (bundle != null) {
                if (bundle.containsKey(LessonLevel.class.getName())) {
                    viewState.lessonLevel = MainApplication.fromJson(bundle.getString(LessonLevel.class.getName()), LessonLevel.class);
                }
                if (bundle.containsKey(Objective.class.getName())) {
                    viewState.objective = MainApplication.fromJson(bundle.getString(Objective.class.getName()), Objective.class);
                }
                if (bundle.containsKey(LessonTest.class.getName())) {
                    viewState.lessonTest = MainApplication.fromJson(bundle.getString(LessonTest.class.getName()), LessonTest.class);
                }
                if (bundle.containsKey(LessonCollection.class.getName())) {
                    viewState.lessonCollection = MainApplication.fromJson(bundle.getString(LessonCollection.class.getName()), LessonCollection.class);
                }
            }
        } else {
            breakDownAction = MainApplication.getContext().getBreakDownAction();
            MainApplication.getContext().setBreakDownAction(null);
        }

        boolean isLesson = false;
        if (bundle != null && bundle.containsKey(MainBroadcaster.Filler.Key.TYPE.toString())) {
            String typeClass = bundle.getString(MainBroadcaster.Filler.Key.TYPE.toString());
            if (typeClass != null && typeClass.equalsIgnoreCase(Objective.class.getName())) {
                isLesson = true;
            }
        }
        viewState.isLesson = isLesson;

        scoreDBAdapter = new ScoreDBAdapter(MainApplication.getContext().getLessonHistoryDatabaseHelper());
        dbAdapter = new WordDBAdapter();
        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.RESET_TIMING_HELP) {
                    if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                }
            }

            @Override
            public void onUserModelFetched(ResponseData<UserVoiceModel> model) {
                if (model != null && model.isStatus()) {
                    viewState.currentModel = model.getData();
                    try {
                        saveToDatabase();
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not save data to database", e);
                        viewState.currentModel = null;
                    }
                } else {
                    viewState.currentModel = null;
                    viewState.errorMessage = (model == null) ? "" : model.getMessage();
                }
                AppLog.logString("Start score animation");
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
            }

            @Override
            public void onHistoryAction(final UserVoiceModel model, String word, int type) {
                switch (type) {
                    case HistoryFragment.CLICK_LIST_ITEM:
                        if (word == null || word.length() == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(model));
                            bundle.putString(ViewState.class.getName(), MainApplication.toJson(viewState));
                            bundle.putString(MainBroadcaster.Filler.LESSON.toString(), MainBroadcaster.Filler.LESSON.toString());
                            //MainApplication.getContext().setSelectedWord(model.getWord());
                            MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                    DetailFragment.class,
                                    new SwitchFragmentParameter(true, true, true),
                                    bundle);
                        }
                        break;
                    case HistoryFragment.CLICK_PLAY_BUTTON:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                play(model.getAudioFile());
                            }
                        });
                        break;
                    case HistoryFragment.CLICK_RECORD_BUTTON:
                        String selectedWord = model.getWord();
                        for (int i = 0; i < viewState.questions.size(); i++) {
                            Question question = viewState.questions.get(i);
                            if (question.isRecorded() && question.getWord().equalsIgnoreCase(selectedWord)) {
                                selectQuestion(i);
                                break;
                            }
                        }
                        break;
                }
            }
        });
        initSlider(root);
        showcaseHelper = new ShowcaseHelper(getActivity());
        popupShowcaseHelper = new PopupShowcaseHelper(getActivity(),
                new PopupShowcaseHelper.HelpItem[] {
                        PopupShowcaseHelper.HelpItem.ANALYZE_A_WORD,
                        PopupShowcaseHelper.HelpItem.PROGRESS,
                        PopupShowcaseHelper.HelpItem.HISTORY
                },
                new PopupShowcaseHelper.OnSelectHelpItem() {
                    @Override
                    public void onSelectHelpItem(PopupShowcaseHelper.HelpItem helpItem) {
                        if (showcaseHelper == null) return;
                        switch (helpItem) {
                            case ANALYZE_A_WORD:
                                showcaseHelper.showHelp(new ShowcaseHelper.HelpState(txtWord, getString(R.string.help_press_the_word)),
                                        new ShowcaseHelper.HelpState(btnAnalyzing, getString(R.string.help_test_pronunciation)),
                                        new ShowcaseHelper.HelpState(btnAudio, getString(R.string.help_hear_last_attempt)));
                                break;
                            case PROGRESS:
                            case HISTORY:
                                currentHelpItem = helpItem;
                                if (panelSlider.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                                    panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                } else {
                                    showRemainHelpItem();
                                }
                                break;
                        }
                    }
                });
        popupShowcaseHelper.resetTiming();
        if (viewState.willShowHelpSearchWordAndSlider) {
            viewState.willShowHelpSearchWordAndSlider = false;
        }
        if (viewState.isLesson) {
            cvTip.setVisibility(View.VISIBLE);
            //txtDefinition.setVisibility(View.VISIBLE);
        } else {
            cvTip.setVisibility(View.INVISIBLE);
            //txtDefinition.setVisibility(View.INVISIBLE);
        }
        txtDefinition.setVisibility(View.VISIBLE);
        drawQuestionList();
        return root;
    }

    private void drawQuestionList() {
        if (breakDownAction != null && breakDownAction.getType() == BreakDownAction.Type.SELECT_NEXT) {
            SimpleAppLog.debug("Detect break down action type SELECT_NEXT. Current lesson ID " + viewState.lessonCollection.getId());
            try {
                LessonCollection nextLesson = LessonDBAdapterService.getInstance().getNextLessonOnCurrentObjective(
                        Preferences.getCurrentProfile().getSelectedCountry().getId(),
                        viewState.lessonLevel.getId(),
                        viewState.objective.getId(),
                        viewState.lessonCollection.getId()
                );
                if (nextLesson != null) {
                    SimpleAppLog.debug("Found next lesson " + nextLesson.getName() + ". ID: " + nextLesson.getId());
                    viewState.lessonCollection = nextLesson;
                    viewState.questions = null;
                } else {
                    SimpleAppLog.debug("No next lesson found for current objective. Move to next objective lesson");
                }
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("could not get next lesson",e);
            }
        }
        questionAdapter = new QuestionAdapter();
        if (viewState.questions == null || viewState.questions.size() == 0) {
            try {
                List<Question> questions = LessonDBAdapterService.getInstance().listAllQuestionByLessonCollection(viewState.lessonCollection.getId());
                if (questions != null && questions.size() > 0) {
                    SimpleAppLog.debug("Found " + questions.size() + " questions from lesson collection id " + viewState.lessonCollection.getId());
                    viewState.questions = questions;
                    viewState.selectedQuestionIndex = 0;
                    viewState.questions.get(viewState.selectedQuestionIndex).setEnabled(true);
                } else {
                    //TODO alert about no question found
                    SimpleAppLog.error("No question found for lesson collection " + viewState.lessonCollection.getId());
                }
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not list all question of lesson collection id " + viewState.lessonCollection.getId(), e);
            }
        }
        if (breakDownAction != null && breakDownAction.getType() == BreakDownAction.Type.SELECT_REDO) {
            viewState.sessionId = UUIDGenerator.generateUUID();
            viewState.selectedQuestionIndex = 0;
            for (int i = 0; i < viewState.questions.size(); i++) {
                final Question  question = viewState.questions.get(i);
                question.setEnabled(i == 0);
                question.setRecorded(false);
            }
        }
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        questionAdapter.notifyDataSetChanged();
        int selectedIndex = viewState.selectedQuestionIndex;
        if (breakDownAction != null && breakDownAction.getType() == BreakDownAction.Type.SELECT_QUESTION) {
            Question selectedQuestion = (Question) breakDownAction.getData();
            if (selectedQuestion != null) {
                for (int i = 0;  i < viewState.questions.size(); i++) {
                    if (viewState.questions.get(i).getId().equals(selectedQuestion.getId())) {
                        selectedIndex = i;
                        break;
                    }
                }
            }
        }
        selectQuestion(selectedIndex);

    }

    private void selectQuestion(int index) {
        if (viewState.questions == null || viewState.questions.size() <= 0) {
            //TODO alert about question
            SimpleAppLog.error("Not enough questions");
            return;
        }
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        final Question question = viewState.questions.get(index);
        if (question != null) {
            if (!viewState.isLesson) {
                txtDefinition.setText(question.getDescription());
            } else {
                txtDefinition.setText(viewState.lessonCollection.getName());
            }
            viewState.selectedQuestionIndex = index;
            if (question.isRecorded()) {
                viewState.dictionaryItem = question.getDictionaryItem();
                viewState.currentModel = question.getUserVoiceModel();
                displayDictionaryItem();
                swapButtonState(question.getScore());
                recordingView.recycleView();
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                recordingView.startPingAnimation(getActivity(), 0, question.getScore(), true, false);
                MainBroadcaster.getInstance().getSender().sendUpdateData(question.getDictionaryItem().getWord(), FragmentTab.TYPE_CHANGE_SELECTED_WORD);
            } else {
                viewState.currentModel = null;
                viewState.dictionaryItem = null;
                try {
                    List<WordCollection> wordCollections = LessonDBAdapterService.getInstance().getAllWordsOfQuestion(question.getId());
                    String selectedWord = "";
                    List<String> words = new ArrayList<>();
                    if (wordCollections != null && wordCollections.size() > 0) {
                        for (WordCollection wordCollection : wordCollections) {
                            String cWord = wordCollection.getWord();
                            if (cWord != null && cWord.length() > 0) {
                                boolean exist = false;
                                for (Question q : viewState.questions) {
                                    String word = q.getWord();
                                    if (word != null && cWord.equalsIgnoreCase(word)) {
                                        SimpleAppLog.debug("Word " + cWord + " is exist if question list. Skip by default. Question id " + question.getId());
                                        exist = true;
                                    }
                                }
                                if (!exist && !words.contains(cWord)) {
                                    words.add(cWord);
                                }
                            }
                        }
                    }
                    if (words.size() > 0)
                        selectedWord = words.get(RandomHelper.getRandomIndex(words.size()));
                    if (selectedWord.length() > 0) {
                        getWord(selectedWord);
                    } else {
                        getWord("not found");
                        SimpleAppLog.error("No word found for question id " + question.getId());
                    }
                } catch (LiteDatabaseException e) {
                    SimpleAppLog.error("Could not list", e);
                }
            }
        }
    }

    private void showRemainHelpItem() {
        if (currentHelpItem != null && showcaseHelper != null && rlSliderContent != null) {
            if (mTabHost != null) {
                int currentTab = mTabHost.getCurrentTab();
                if (currentHelpItem == PopupShowcaseHelper.HelpItem.PROGRESS && currentTab != 0) {
                    mTabHost.setCurrentTab(0);
                } else if (currentHelpItem == PopupShowcaseHelper.HelpItem.HISTORY && currentTab != 1) {
                    mTabHost.setCurrentTab(1);
                }
                ShowcaseConfig showcaseConfig = new ShowcaseConfig();
                showcaseConfig.setShape(new RectangleShape(new ViewTarget(rlSliderContent).getBounds(), false));
                switch (currentHelpItem) {
                    case PROGRESS:
                        showcaseHelper.showHelp(showcaseConfig, new ShowcaseHelper.HelpState(rlSliderContent,
                                getString(R.string.help_track_progress)));
                        break;
                    case HISTORY:
                        showcaseHelper.showHelp(showcaseConfig, new ShowcaseHelper.HelpState(rlSliderContent,
                                getString(R.string.help_view_past_words)));
                        break;
                }
            }
            currentHelpItem = null;
        }
    }

    private Handler handlerTip = new Handler();

    @OnClick(R.id.cvTip)
    public void clickTipIcon() {
        if (viewState.lessonCollection != null) {
            final Dialog dialog = new DefaultCenterDialog(getActivity(), R.layout.showcase_content);
            ((HtmlTextView) dialog.findViewById(R.id.tv_content)).setHtmlFromString(
                    viewState.lessonCollection.getDescription(), true);
            dialog.show();
            handlerTip.removeCallbacksAndMessages(null);
            handlerTip.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) dialog.dismiss();
                }
            }, 5000);
            if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        }
    }

    private void initRecordingView() {
        recordingView.setAnimationListener(this);
        txtPhonemes.setText("");
        txtWord.setText("");
    }

    private void initTabHost() {
        if (mTabHost == null) return;
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }
        });
        addTabImage(R.drawable.tab_graph,
                GraphFragmentParent.class, getString(R.string.tab_graph));
        addTabImage(R.drawable.tab_history,
                HistoryFragment.class, getString(R.string.tab_history));
    }

    private void displayDictionaryItem() {
        if (viewState.dictionaryItem != null) {
            txtPhonemes.setText(viewState.dictionaryItem.getPronunciation());
            txtWord.setText(viewState.dictionaryItem.getWord());
        }
    }

    private void initSlider(final View root) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        SlidingUpPanelLayout.LayoutParams layoutParams = (SlidingUpPanelLayout.LayoutParams) rlSliderContent.getLayoutParams();
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        final int halfHeight = (displayMetrics.heightPixels - actionBarHeight) / 2;
        layoutParams.height = halfHeight;
        rlSliderContent.setLayoutParams(layoutParams);
        panelSlider.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (txtPhonemes == null) return;
                float test = (halfHeight - root.findViewById(R.id.btnSlider).getHeight()) / (float) halfHeight;
                //SimpleAppLog.debug("Offset: " + slideOffset + ". Test: " + test);
                if (slideOffset > test) {
                    txtPhonemes.setVisibility(View.INVISIBLE);
                } else {
                    txtPhonemes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }

            @Override
            public void onPanelExpanded(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                showRemainHelpItem();
            }

            @Override
            public void onPanelAnchored(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }

            @Override
            public void onPanelHidden(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }
        });
        if (viewState.willCollapseSlider) {
            txtPhonemes.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (panelSlider != null) {
                        panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        viewState.willCollapseSlider = false;
                    }
                }
            }, 2000);
        }

    }

    private void initAnimation() {
        fadeIn = AnimationUtils.loadAnimation(MainApplication.getContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(MainApplication.getContext(), android.R.anim.fade_out);
    }

    private void addTabImage(int drawableId, Class<?> c, String labelId) {
        Bundle bundle = new Bundle();
        bundle.putString(MainBroadcaster.Filler.LESSON.toString(), MainBroadcaster.Filler.LESSON.toString());
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId).setIndicator(null, getResources().getDrawable(drawableId));
        mTabHost.addTab(spec, c, bundle);

    }

    private void completeGetWord(DictionaryItem item, ButtonState state) {
        viewState.dictionaryItem = item;
        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
        SimpleAppLog.debug("Complete get word. Wait for animation min");
        lastState = state;
        if (audioStream != null)
            audioStream.clearOldRecord();
    }

    private class GetWordAsync extends AsyncTask {
        private final String word;

        private GetWordAsync(String word) {
            this.word = word;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            selectedWord = word;
            DictionaryWalker walker = DictionaryWalkerFactory.getInstance();
            walker.setListener(new DictionaryListener() {
                @Override
                public void onDetectWord(final DictionaryItem dItem) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SimpleAppLog.logJson("Dictionary item", dItem);
                                completeGetWord(dItem, ButtonState.DEFAULT);
                            }
                        });
                }

                @Override
                public void onWordNotFound(DictionaryItem dItem, final FileNotFoundException ex) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SimpleAppLog.error("Word not found", ex);
                                completeGetWord(null, ButtonState.DISABLED);
                            }
                        });
                }

                @Override
                public void onError(DictionaryItem dItem, final Exception ex) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SimpleAppLog.error("Error when get word", ex);
                                completeGetWord(null, ButtonState.DISABLED);
                            }
                        });
                    }
                }
            });
            walker.execute(word);
            return null;
        }
    }

    private GetWordAsync getWordAsync;

    private void getWord(final String word) {
        if (isRecording) return;
        try {
            if (dbAdapter == null) dbAdapter = new WordDBAdapter();
//            if (!dbAdapter.isBeep(word)) {
//                AnalyticHelper.sendSelectWordNotInBeep(MainApplication.getContext(), word);
//                SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
//                d.setTitleText(getString(R.string.word_not_in_beep_title));
//                d.setContentText(getString(R.string.word_not_in_beep_message));
//                d.setConfirmText(getString(R.string.dialog_ok));
//                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                });
//                d.show();
//                return;
//            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not check word " + word + " is beep or not", e);
        }
        try {
            if (((MainActivity) getActivity()).checkNetwork(false)) {
                viewState.currentModel = null;
                recordingView.recycleView();
                analyzingState = AnalyzingState.DEFAULT;
                AnalyticHelper.sendSelectWord(MainApplication.getContext(), word);
                switchButtonStage(ButtonState.DISABLED);
                recordingView.startPingAnimation(getActivity());

                YoYo.with(Techniques.FadeIn).duration(700).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        imgHourGlass.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(imgHourGlass);

                txtWord.setText(getString(R.string.searching));
                txtPhonemes.setText(getString(R.string.please_wait));
                viewState.dictionaryItem = null;
                viewState.currentModel = null;
                if (getWordAsync != null) {
                    try {
                        while (!getWordAsync.isCancelled() && getWordAsync.cancel(true)) ;
                    } catch (Exception ex) {

                    }
                }
                getWordAsync = new GetWordAsync(word);
                getWordAsync.execute();
            }
        } catch (Exception e) {

        }
    }

    private void stopPlay() {
        switchButtonStage();
        isPlaying = false;
        player.stop();
    }

    private void play(String file) {
        if (file == null || file.length() == 0) return;
        play(new File(file));
    }

    private void play(File file) {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
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
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAudioExist() {
        String fileName = getCurrentRecordedFileName();
        if (fileName != null && fileName.length() > 0) {
            return new File(fileName).exists();
        }
        return false;
    }

    private String getCurrentRecordedFileName() {
        if (viewState != null && viewState.currentModel != null) {
            return viewState.currentModel.getAudioFile();
        }
        return "";
    }

    private void play() {
        if (!checkAudioExist()) return;
        try {
            String fileName = getCurrentRecordedFileName();
            File file = new File(fileName);
            if (!file.exists()) return;
            switchButtonStage(ButtonState.PLAYING);
            isPlaying = true;
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(new File(fileName), new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopPlay();
                        }
                    });

                }

            });
            start = System.currentTimeMillis();
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        stopRecording();
        if (uploadTask != null) {
            try {
                while (!uploadTask.isCancelled() && uploadTask.cancel(true)) ;
                uploadTask = null;
            } catch (Exception ex) {

            }
        }
    }

    private void analyze() {
        try {
            // Clear old data
            viewState.currentModel = null;
            analyzingState = AnalyzingState.RECORDING;
            switchButtonStage(ButtonState.RECORDING);
            isRecording = true;
            if (recordingView != null)
                recordingView.recycle();
            audioInputStream = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, chanel, RECORDER_AUDIO_ENCODING, bufferSize);

            if (audioInputStream.getState() == AudioRecord.STATE_UNINITIALIZED) {
                String configResume = "initRecorderParameters(sRates) has found recorder settings supported by the device:"
                        + "\nSource   = MICROPHONE"
                        + "\nsRate    = " + sampleRate + "Hz"
                        + "\nChannel  = " + ((chanel == AudioFormat.CHANNEL_IN_MONO) ? "MONO" : "STEREO")
                        + "\nEncoding = 16BIT";
                AppLog.logString(configResume);
                audioInputStream.release();
                audioInputStream = null;
                return;
            }
            //start recording ! Opens the stream
            audioInputStream.startRecording();

            TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, 16, (chanel == AudioFormat.CHANNEL_IN_MONO) ? 1 : 2, true, false);

            audioStream = new AndroidAudioInputStream(MainApplication.getContext(), audioInputStream, format, bufferSize);
            dispatcher = new AudioDispatcher(audioStream, bufferSize / 2, 0);
            dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, sampleRate, bufferSize / 2, new PitchDetectionHandler() {

                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                        AudioEvent audioEvent) {
                    pitch = pitchDetectionResult.getPitch();
                    if (pitch != -1) {
                        AppLog.logString("Detect pitch " + pitch);
                        lastDetectedPitchTime = System.currentTimeMillis();
                    }
                    long length = System.currentTimeMillis() - start;
                    if (length > RECORD_MAX_LENGTH || ((System.currentTimeMillis() - lastDetectedPitchTime) > PITCH_TIMEOUT)
                            && isAutoStop
                            && (length > (START_TIMEOUT + PITCH_TIMEOUT))) {
                        stopRecording(false);
                        uploadRecord();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recordingView.startPingAnimation(getActivity(), 2000, 100.0f, true, false);
                            }
                        });
                    }
                }
            }));
            dispatcher.addAudioProcessor(new Oscilloscope(new Oscilloscope.OscilloscopeEventHandler() {
                @Override
                public void handleEvent(final float[] floats, final AudioEvent audioEvent) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recordingView != null)
                                recordingView.setData(floats, pitch);
                        }
                    });

                }
            }));
            start = System.currentTimeMillis();
            runner = new Thread(dispatcher, "Audio Dispatcher");
            lastDetectedPitchTime = System.currentTimeMillis();
            runner.start();
        } catch (Exception ex) {
            SimpleAppLog.error("Could not start recording", ex);
        }
    }

    private void stopRecording() {
        stopRecording(true);
    }

    private void stopRecording(boolean changeStatus) {
        if (isRecording) {
            if (changeStatus) {
                isRecording = false;
            }
            AppLog.logString("Stop Recording");
            try {
                if (audioInputStream != null) {
                    audioInputStream.stop();
                    audioInputStream.release();
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            try {
                if (dispatcher != null)
                    dispatcher.stop();
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handlerStartDetail.removeCallbacks(runnableStartDetail);
        if (popupShowcaseHelper != null) {
            popupShowcaseHelper.recycle();
            popupShowcaseHelper = null;
        }
        stop();
        try {
            if (recordingView != null) {
                recordingView.stopPingAnimation();
                recordingView.recycle();
            }
        } catch (Exception ex) {

        }
        MainBroadcaster.getInstance().unregister(receiverListenerId);
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(false);
        }
        stopRequestLocation();
    }

    protected void stopRequestLocation() {
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) MainApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.removeUpdates(this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception ex) {
            SimpleAppLog.error("Could not stop request location", ex);
        }
    }

    protected void requestLocation() {
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request GPS provider location", e);
        }
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request Network provider location", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(true);
        }
        requestLocation();
        fetchSetting();
    }

    @OnClick({R.id.txtPhoneme, R.id.txtWord, R.id.rlVoiceExample})
    void playIPAVoice() {
        if (viewState.dictionaryItem != null) {
            play(viewState.dictionaryItem.getAudioFile());
        }
    }

    @OnClick(R.id.main_recording_view)
    void clickModelScore() {
        viewState.willShowHelpSearchWordAndSlider = true;
        handlerStartDetail.post(runnableStartDetail);
    }

    @OnClick(R.id.btnAudio)
    void playRecordedVoice() {
        if (isPlaying) {
            stopPlay();
        } else {
            play();
        }
    }

    @OnClick(R.id.btnAnalyzing)
    void clickAnalyzeVoice() {
        if (((MainActivity) getActivity()).checkNetwork(false)) {
            if (isRecording) {
                stop();
                switchButtonStage(ButtonState.DISABLED);
                if (viewState.currentModel != null) {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 2000, viewState.currentModel.getScore(), true, true);
                } else {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                    recordingView.startPingAnimation(getActivity(), 1000, 100.0f, false, false);
                }
            } else {
                analyze();
            }
        }
    }

    private Handler handlerStartDetail = new Handler();

    private Runnable runnableStartDetail = new Runnable() {
        @Override
        public void run() {
            if (viewState.currentModel != null && viewState.dictionaryItem != null) {
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(viewState.currentModel));
                bundle.putString(MainBroadcaster.Filler.LESSON.toString(), MainBroadcaster.Filler.LESSON.toString());
                MainApplication.getContext().setLessonViewState(viewState);
                bundle.putString(ViewState.class.getName(), MainApplication.toJson(viewState));
                //MainApplication.getContext().setSelectedWord(viewState.currentModel.getWord());
                MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                        DetailFragment.class,
                        new SwitchFragmentParameter(true, true, true),
                        bundle);
            }
        }
    };

    private void switchButtonStage() {
        if (lastState == null) lastState = ButtonState.DEFAULT;
        switchButtonStage(lastState);
    }

    private void switchButtonStage(ButtonState state) {
        try {
            boolean isProcess = true;
            imgHelpHand.setVisibility(View.GONE);
            switch (state) {
                case RECORDING:
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    btnAudio.setEnabled(false);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_close);
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case PLAYING:
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_close);
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    btnAnalyzing.setEnabled(false);
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case GREEN:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case ORANGE:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_orange));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_orange));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case RED:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case DISABLED:
                    btnAudio.setEnabled(false);
                    btnAnalyzing.setEnabled(false);
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    break;
                case DEFAULT:
                default:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
            }
            // Call other view update
            MainBroadcaster.getInstance().getSender().sendUpdateData(null, isProcess ? FragmentTab.TYPE_DISABLE_VIEW : FragmentTab.TYPE_ENABLE_VIEW);
            if (!checkAudioExist()) {
                btnAudio.setEnabled(false);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                ((ImageView) btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
            }

            boolean isEnabled = state != ButtonState.DISABLED && state != ButtonState.PLAYING && state != ButtonState.RECORDING;
            int count = recyclerView.getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    View item = recyclerView.getChildAt(i);
                    item.findViewById(R.id.cvItemContainer).setEnabled(isEnabled);
                }
            }
            cvTip.setEnabled(isEnabled);

            // Only record a question one time
            if (viewState != null) {
                Question currentQuestion = viewState.getCurrentQuestion();
                if (currentQuestion != null && ! viewState.isLesson && currentQuestion.isRecorded()) {
                    ((ImageView) btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    btnAnalyzing.setEnabled(false);
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not update screen state", e);
        }
    }

    private void fetchSetting() {
        try {
            String mChanel = Preferences.getString(Preferences.KEY_AUDIO_CHANEL, MainApplication.getContext(), "mono");
            if (mChanel.equalsIgnoreCase("mono")) {
                chanel = AudioFormat.CHANNEL_IN_MONO;
            } else {
                chanel = AudioFormat.CHANNEL_IN_STEREO;
            }
            sampleRate = Preferences.getInt(Preferences.KEY_AUDIO_SAMPLE_RATE, MainApplication.getContext(), 16000);
            isAutoStop = Preferences.getBoolean(Preferences.KEY_AUDIO_AUTO_STOP_RECORDING, MainApplication.getContext(), true);

            bufferSize = AudioRecord.getMinBufferSize(sampleRate, chanel, RECORDER_AUDIO_ENCODING);
        } catch (Exception e) {
            SimpleAppLog.error("Could not fetch setting", e);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        accountManager.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        accountManager.stop();
    }

    private void uploadRecord() {
        try {
            AppLog.logString("Start Uploading");
            analyzingState = AnalyzingState.ANALYZING;
            uploadTask = new UploaderAsync(getActivity(), getActivity().getResources().getString(R.string.upload_weight_url));
            Map<String, String> params = new HashMap<String, String>();
            String fileName = audioStream.getFilename();
            File tmp = new File(fileName);
            if (tmp.exists()) {
                UserProfile profile = Preferences.getCurrentProfile(MainApplication.getContext());
                if (profile != null) {
                    Gson gson = new Gson();
                    profile.setUuid(new DeviceUuidFactory(MainApplication.getContext()).getDeviceUuid().toString());
                    UserProfile.UserLocation lc = new UserProfile.UserLocation();
                    Location location = AndroidHelper.getLastBestLocation(MainApplication.getContext());
                    if (location != null) {
                        lc.setLongitude(location.getLongitude());
                        lc.setLatitude(location.getLatitude());
                        AppLog.logString("Lat: " + lc.getLatitude() + ". Lon: " + lc.getLongitude());
                        profile.setLocation(lc);
                    }
                    profile.setTime(System.currentTimeMillis());
                    params.put(FileCommon.PARA_FILE_NAME, tmp.getName());
                    params.put(FileCommon.PARA_FILE_PATH, tmp.getAbsolutePath());
                    params.put(FileCommon.PARA_FILE_TYPE, "audio/wav");
                    params.put("idWord", viewState.dictionaryItem.getWordId());
                    params.put("idQuestion", viewState.getCurrentQuestion().getId());
                    params.put("idCountry", Preferences.getCurrentProfile().getSelectedCountry().getId());
                    params.put("session", viewState.sessionId);
                    params.put("idLessonCollection", viewState.lessonCollection.getId());
                    if (viewState.isLesson) {
                        params.put("type", "Q");
                        params.put("itemId", viewState.objective.getId());
                    } else {
                        params.put("type", "T");
                        params.put("itemId", viewState.lessonTest.getId());
                    }
                    params.put("levelId", viewState.lessonLevel.getId());
                    params.put("profile", gson.toJson(profile));
                    params.put("word", selectedWord);

                    uploadTask.execute(params);
                } else {
                    AppLog.logString("Could not get user profile");
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not upload recording", e);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void swapButtonState(int score) {
        if (score >= 80.0) {
            lastState = ButtonState.GREEN;
        } else if (score >= 45.0) {
            lastState = ButtonState.ORANGE;
        } else {
            lastState = ButtonState.RED;
        }
        switchButtonStage();
    }

    @Override
    public void onAnimationMax() {
        try {
            if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MAX) {
                AppLog.logString("On animation max");
                if (recordingView == null) return;
                recordingView.stopPingAnimation();
                if (viewState.currentModel != null) {
                    if (isRecording) {
                        //viewState.currentModel.setAudioFile(audioStream.getFilename());
                        int score = Math.round(viewState.currentModel.getScore());

                        // Call other view update
                        Gson gson = new Gson();
                        MainBroadcaster.getInstance().getSender().sendUpdateData(gson.toJson(viewState.currentModel), FragmentTab.TYPE_RELOAD_DATA);
                        swapButtonState(score);

//                        showcaseHelper.showHelp(ShowcaseHelper.HelpKey.SELECT_SCORE,
//                                new ShowcaseHelper.HelpState(btnAudio, "<b>Press</b> to <b>hear</b> your last attempt"),
//                                new ShowcaseHelper.HelpState(recordingView, "<b>Press</b> for more detail"));

                        if (viewState.selectedQuestionIndex < viewState.questions.size() - 1) {
                            // Move to next question
                            final Question q = viewState.questions.get(viewState.selectedQuestionIndex + 1);
                            q.setEnabled(true);
                        } else if (viewState.selectedQuestionIndex == viewState.questions.size() - 1) {
                            // The last item
                            //TODO alert final score
                        }
                        questionAdapter.notifyDataSetChanged();
                        isRecording = false;
                        handlerStartDetail.postDelayed(runnableStartDetail, 1000);
                    }
                } else {
                    switchButtonStage(ButtonState.RED);
                    SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    d.setTitleText(getString(R.string.could_not_analyze_word_title));
                    d.setContentText(getString(R.string.could_not_analyze_word_message));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            popupShowcaseHelper.resetTiming();
                        }
                    });
                    d.show();

                }
                analyzingState = AnalyzingState.DEFAULT;
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
            isRecording = false;
        }
    }

    @Override
    public void onAnimationMin() {
        try {
            if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MIN) {
                if (recordingView == null) {
                    analyzingState = AnalyzingState.DEFAULT;
                    return;
                }
                AppLog.logString("On animation min");
                recordingView.setScore(0.0f);
                recordingView.stopPingAnimation();
                recordingView.recycle();
                recordingView.invalidate();
                if (viewState.currentModel != null) {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 2000, viewState.currentModel.getScore(), true, true);
                } else if (isRecording) {
                    SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    d.setTitleText(getString(R.string.could_not_analyze_word_title));
                    if (viewState.errorMessage != null && viewState.errorMessage.equalsIgnoreCase("invalid token")) {
                        d.setContentText("invalid login token. please logout and login again!");
                        d.setConfirmText(getString(R.string.logout));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                MainActivity activity = (MainActivity) getActivity();
                                if (activity != null) {
                                    UserProfile profile = Preferences.getCurrentProfile();
                                    if (profile != null) {
                                        AnalyticHelper.sendUserLogout(activity, profile.getUsername());
                                    }
                                    accountManager.logout();
                                    activity.finish();
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        d.setContentText(getString(R.string.could_not_analyze_word_message));
                        d.setConfirmText(getString(R.string.dialog_ok));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    }
                    d.show();
                    switchButtonStage(ButtonState.RED);
                    recordingView.drawEmptyCycle();
                    analyzingState = AnalyzingState.DEFAULT;
                    isRecording = false;
                    popupShowcaseHelper.resetTiming();
                } else {
                    YoYo.with(Techniques.FadeOut).duration(700).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (imgHourGlass != null)
                                imgHourGlass.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(imgHourGlass);

                    recordingView.drawEmptyCycle();
                    // Null response
                    analyzingState = AnalyzingState.DEFAULT;
                    switchButtonStage();

                    if (viewState.dictionaryItem != null) {
                        txtWord.setText(viewState.dictionaryItem.getWord());
                        txtPhonemes.setText(viewState.dictionaryItem.getPronunciation());
                        txtPhonemes.setVisibility(View.VISIBLE);
                        txtWord.setEnabled(true);
                        txtPhonemes.setEnabled(true);
                        rlVoiceExample.setEnabled(true);
                        AndroidHelper.updateMarqueeTextView(txtWord, !AndroidHelper.isCorrectWidth(txtWord, viewState.dictionaryItem.getWord()));
                        AndroidHelper.updateMarqueeTextView(txtPhonemes, !AndroidHelper.isCorrectWidth(txtPhonemes, viewState.dictionaryItem.getPronunciation()));
                        //txtPhonemes.setSelected(true);
                        //txtWord.setSelected(true);
                        showcaseHelper.showHelp(ShowcaseHelper.HelpKey.ANALYZING_WORD,
                                new ShowcaseHelper.HelpState(txtWord, getString(R.string.help_press_the_word)),
                                new ShowcaseHelper.HelpState(btnAnalyzing, getString(R.string.help_test_pronunciation)));

                        final Question question = viewState.questions.get(viewState.selectedQuestionIndex);
                        question.setDictionaryItem(MainApplication.fromJson(MainApplication.toJson(viewState.dictionaryItem), DictionaryItem.class));
                        question.setWord(viewState.dictionaryItem.getWord());
                        MainApplication.getContext().setSelectedWord(viewState.dictionaryItem.getWord());
                        MainBroadcaster.getInstance().getSender().sendUpdateData(viewState.dictionaryItem.getWord(), FragmentTab.TYPE_CHANGE_SELECTED_WORD);
                    } else {
                        txtWord.setText(getString(R.string.not_found));
                        txtPhonemes.setText(getString(R.string.please_try_again));
                        MainApplication.getContext().setSelectedWord(null);
                        MainBroadcaster.getInstance().getSender().sendUpdateData(null, FragmentTab.TYPE_CHANGE_SELECTED_WORD);
                    }
                    popupShowcaseHelper.resetTiming();
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
        }
    }


    private void saveToDatabase() throws Exception {
        if (audioStream == null) return;
        final String tmpFile = audioStream.getFilename();
        final File recordedFile = new File(tmpFile);
        if (recordedFile.exists() && viewState.currentModel != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {

                        AnalyticHelper.sendAnalyzingWord(getActivity(), viewState.currentModel.getWord(), Math.round(viewState.currentModel.getScore()));
                        File pronScoreDir = FileHelper.getPronunciationScoreDir(MainApplication.getContext());
                        PronunciationScore score = new PronunciationScore();
                        // Get ID from server
                        String dataId = viewState.currentModel.getId();
                        score.setDataId(dataId);
                        score.setScore(viewState.currentModel.getScore());
                        score.setWord(viewState.currentModel.getWord());
                        score.setTimestamp(new Date(System.currentTimeMillis()));
                        //DENP-238
                        score.setUsername(viewState.currentModel.getUsername());
                        score.setVersion(viewState.currentModel.getVersion());
                        // Save recorded file
                        File savedFile = new File(pronScoreDir, dataId + FileHelper.WAV_EXTENSION);
                        FileUtils.copyFile(recordedFile, savedFile);
                        // Save json data
                        Gson gson = new Gson();
                        viewState.currentModel.setAudioFile(savedFile.getAbsolutePath());
                        FileUtils.writeStringToFile(new File(pronScoreDir, dataId + FileHelper.JSON_EXTENSION), gson.toJson(viewState.currentModel), "UTF-8");
                        scoreDBAdapter.insert(score);
                        if (viewState.currentModel.getResult() != null) {
                            PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter(MainApplication.getContext().getLessonHistoryDatabaseHelper());
                            List<SphinxResult.PhonemeScore> phonemeScoreList = viewState.currentModel.getResult().getPhonemeScores();
                            if (phonemeScoreList != null && phonemeScoreList.size() > 0) {
                                for (SphinxResult.PhonemeScore phonemeScore : phonemeScoreList) {
                                    phonemeScore.setTime(System.currentTimeMillis());
                                    phonemeScore.setTimestamp(new Date(System.currentTimeMillis()));
                                    phonemeScore.setUserVoiceId(dataId);
                                    phonemeScoreDBAdapter.insert(phonemeScore, viewState.currentModel.getUsername(), viewState.currentModel.getVersionPhoneme());
                                }
                            }
                        }

                        int qScore = Math.round(viewState.currentModel.getScore());
                        final Question question = viewState.questions.get(viewState.selectedQuestionIndex);
                        question.setScore(qScore);
                        question.getScoreHistory().add(qScore);
                        question.setRecorded(true);
                        boolean isFinished = true;
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
                                if (!viewState.isLesson) {
                                    // Only lesson score will be tracked if not complete all questions
                                    isFinished = false;
                                    break;
                                }
                            }
                        }
                        if (isFinished) {
                            SimpleAppLog.debug("Number of complete question " + totalQuestions + ". Total score: " + totalScore);
                            int avgScore = Math.round((float) totalScore / totalQuestions);
                            UserProfile userProfile = Preferences.getCurrentProfile();
                            if (viewState.isLesson) {
                                // Is object lesson test
                                SimpleAppLog.debug("Save objective id "  + viewState.objective.getId() +" lesson id " +
                                        viewState.lessonCollection.getId() +
                                        " score " + avgScore);

                                LessonHistoryDBAdapterService.getInstance().addLessonScore(
                                        userProfile.getUsername(),
                                        userProfile.getSelectedCountry().getId(),
                                        viewState.lessonLevel.getId(),
                                        viewState.objective.getId(),
                                        viewState.lessonCollection.getId(),
                                        avgScore
                                );
                            } else {
                                SimpleAppLog.debug("Save test id " + viewState.lessonTest.getId() + " lesson id " +
                                        viewState.lessonCollection.getId() +
                                        " score " + totalScore);
                                double percentPass = viewState.lessonTest.getPercentPass();
                                LessonHistoryDBAdapterService.getInstance().addLevelScore(
                                        userProfile.getUsername(),
                                        userProfile.getSelectedCountry().getId(),
                                        viewState.lessonLevel.getId(),
                                        avgScore,
                                        avgScore >= percentPass
                                );
                            }
                        }
                        question.setUserVoiceModel(MainApplication.fromJson(MainApplication.toJson(viewState.currentModel), UserVoiceModel.class));
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not save score to database",e);
                    }
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SimpleAppLog.debug("on save instance state for lesson fragment");
        MainApplication.getContext().setLessonViewState(viewState);
    }

    /**
     * Define all button state
     */
    enum ButtonState {
        DEFAULT,
        RECORDING,
        PLAYING,
        ORANGE,
        RED,
        GREEN,
        DISABLED
    }

    enum AnalyzingState {
        DEFAULT,
        RECORDING,
        ANALYZING,
        WAIT_FOR_ANIMATION_MIN,
        WAIT_FOR_ANIMATION_MAX
    }

    public class ViewState {

        String errorMessage;

        public boolean isLesson;

        public String sessionId = UUIDGenerator.generateUUID();

        public List<Question> questions = new ArrayList<Question>();

        public int selectedQuestionIndex;

        public LessonLevel lessonLevel;

        public Objective objective;

        public LessonTest lessonTest;

        public LessonCollection lessonCollection;

        public UserVoiceModel currentModel;

        public DictionaryItem dictionaryItem;

        public boolean willCollapseSlider = true;

        public boolean willShowHelpSearchWordAndSlider = false;

        public Question getCurrentQuestion() {
            if (questions == null || questions.size() == 0) return null;
            return questions.get(selectedQuestionIndex);
        }
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
            final Question question = viewState.questions.get(position);
            int bgColor = R.color.app_gray;
            int avgScore = 0;
            String prefix = viewState.isLesson ? "Q" : "T";
            String text = String.format(Locale.getDefault(), prefix + "%d", position + 1);
            if (question.getScoreHistory().size() > 0) {
                int totalScore = 0;
                for (Integer score : question.getScoreHistory()) {
                    totalScore += score;
                }
                avgScore = Math.round((float) totalScore / question.getScoreHistory().size());
                text = String.format(Locale.getDefault(), "%d", avgScore);
            }
            if (question.isEnabled()) {
                holder.cardView.setTag(position);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (question.isEnabled()) {
                            selectQuestion((Integer) view.getTag());
                        }
                    }
                });
                if (question.isRecorded()) {
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
            return viewState.questions.size();
        }
    }
}
