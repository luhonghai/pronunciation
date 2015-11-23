package com.cmg.android.bbcaccent.fragment.tab;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.ScoreDBAdapter;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.CustomGraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Collection;
import java.util.Iterator;


public class GraphFragment extends FragmentTab {

    private ScoreDBAdapter dbAdapter;

    private PhonemeScoreDBAdapter phonemeScoreDBAdapter;

    private CustomGraphView graph;

    private String word;

    private IPAMapArpabet phoneme;

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPhoneme(IPAMapArpabet phoneme) {
        this.phoneme = phoneme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        boolean isLesson = bundle != null && bundle.containsKey(MainBroadcaster.Filler.LESSON.toString());
        if (dbAdapter == null) {
            if (isLesson) {
                dbAdapter = new ScoreDBAdapter(MainApplication.getContext().getLessonHistoryDatabaseHelper());
            } else {
                dbAdapter = new ScoreDBAdapter();
            }
        }
        if (phonemeScoreDBAdapter == null) {
            if (isLesson) {
                phonemeScoreDBAdapter = new PhonemeScoreDBAdapter(MainApplication.getContext().getLessonHistoryDatabaseHelper());
            } else {
                phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
            }
        }
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = (CustomGraphView) v.findViewById(R.id.graphScore);

        isLoadedView = true;
        //graph.getLegendRenderer().setVisible(false);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().setNumVerticalLabels(3);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        int screenSize = AndroidHelper.getScreenSize(getActivity());
        Point tintTextPoint = new Point();
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                graph.getGridLabelRenderer().setPadding(30);
                tintTextPoint.x = 50;
                tintTextPoint.y = 60;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                graph.getGridLabelRenderer().setPadding(20);
                tintTextPoint.x = 30;
                tintTextPoint.y = 40;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                graph.getGridLabelRenderer().setPadding(10);
                tintTextPoint.x = 20;
                tintTextPoint.y = 25;
                break;
            default:
                graph.getGridLabelRenderer().setPadding(5);
                tintTextPoint.x = 15;
                tintTextPoint.y = 22;
        }
        graph.setTintTextPoint(tintTextPoint);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setMinX(0);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScrollable(false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadScore();
    }

    private void loadScore() {
        if (phoneme != null) {
            graph.setTintText(phoneme.getIpa());
            loadPhonemeScore();
        } else {
            graph.setTintText(word);
            loadWordScore();
        }
    }

    @Override
    protected void onUpdateData(String data) {
        loadScore();
    }

    @Override
    protected void enableView(boolean enable) {
        if (graph == null) return;
        graph.setEnabled(enable);
    }


    private void loadPhonemeScore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Collection<SphinxResult.PhonemeScore> scores = null;
                try {
                    phonemeScoreDBAdapter.open();
                    if (phoneme == null) {
                        scores = phonemeScoreDBAdapter.toList(phonemeScoreDBAdapter.getAll());
                    } else {
                        scores = phonemeScoreDBAdapter.toList(phonemeScoreDBAdapter.getByPhoneme(phoneme.getArpabet().toUpperCase()));
                    }
                } catch (Exception e) {
                    SimpleAppLog.error("Could not open database",e);
                } finally {
                    try {
                        phonemeScoreDBAdapter.close();
                    } catch (Exception ex) {
                    }
                }
                if (scores != null && scores.size() > 0) {
                    int size = scores.size();
                    final DataPoint[] points = new DataPoint[size];
                    Iterator<SphinxResult.PhonemeScore> scoreIterator = scores.iterator();
                    int i = 0;
                    float latestScore = -1;
                    while (scoreIterator.hasNext()) {
                        SphinxResult.PhonemeScore score = scoreIterator.next();
                        if (score.getTotalScore() > 100) score.setTotalScore(100.0f);
                        if (latestScore == -1)
                            latestScore = score.getTotalScore();
                        DataPoint dataPoint = new DataPoint(size - 1 - i, score.getTotalScore());
                        points[size - 1 - i] = dataPoint;
                        i++;
                    }
                    final float score = latestScore;
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawData(points, score);
                                graph.setDrawChart(true);
                                graph.invalidate();
                            }
                        });

                } else {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                graph.setDrawChart(false);
                                graph.invalidate();
                            }
                        });
                }
                return null;
            }
        }.execute();

    }

    private void loadWordScore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (getActivity() != null) {
                    Collection<PronunciationScore> scores = null;
                    UserProfile profile = Preferences.getCurrentProfile();
                    if (profile != null) {
                        try {
                            dbAdapter.open();
                            if (word == null || word.length() == 0) {
                                scores = dbAdapter.toList(dbAdapter.getAll(profile.getUsername()));
                            } else {
                                scores = dbAdapter.toList(dbAdapter.getByWord(word, profile.getUsername()));
                            }
                        } catch (Exception e) {
                            SimpleAppLog.error("Could not open database", e);
                        } finally {
                            try {
                                dbAdapter.close();
                            } catch (Exception ex) {

                            }
                        }
                    }
                    if (scores != null && scores.size() > 0) {
                        int size = scores.size();
                        final DataPoint[] points = new DataPoint[size];
                        Iterator<PronunciationScore> scoreIterator = scores.iterator();
                        int i = 0;
                        float latestScore = -1;
                        while (scoreIterator.hasNext()) {
                            PronunciationScore score = scoreIterator.next();
                            if (latestScore == -1)
                                latestScore = score.getScore();
                            DataPoint dataPoint = new DataPoint(size - 1 - i, score.getScore());
                            points[size - 1 - i] = dataPoint;
                            i++;
                        }
                        final float score = latestScore;
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    drawData(points, score);
                                    graph.setDrawChart(true);
                                    graph.invalidate();
                                }
                            });

                    } else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    graph.setDrawChart(false);
                                    graph.invalidate();
                                }
                            });
                    }
                }
                return null;
            }
        }.execute();

    }

    private void drawData(DataPoint[] points, float latestScore) {
        if (getActivity() == null) return;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        series.setDrawDataPoints(true);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);


        int screenSize = AndroidHelper.getScreenSize(getActivity());
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                series.setDataPointsRadius(4);
                series.setThickness(5);
                paint.setPathEffect(new CornerPathEffect(5));
                paint.setStrokeWidth(5);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                series.setDataPointsRadius(2);
                series.setThickness(3);
                paint.setPathEffect(new CornerPathEffect(3));
                paint.setStrokeWidth(3);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                series.setDataPointsRadius(2);
                series.setThickness(2);
                paint.setPathEffect(new CornerPathEffect(2));
                paint.setStrokeWidth(2);
                break;
            default:
                series.setDataPointsRadius(2);
                series.setThickness(2);
                paint.setPathEffect(new CornerPathEffect(2));
                paint.setStrokeWidth(2);
        }

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        int color;
        if (latestScore >= 80.0f) {
            color = ColorHelper.getColor(R.color.app_green);
        } else if (latestScore >= 45.0f) {
            color = ColorHelper.getColor(R.color.app_orange);
        } else {
            color = ColorHelper.getColor(R.color.app_red);
        }
        paint.setColor(color);
        series.setCustomPaint(paint);
        series.setColor(color);
        //series.setDrawDataPoints(true);
        //series.setThickness(2);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.getViewport().scrollToEnd();
    }
}
