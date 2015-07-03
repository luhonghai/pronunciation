package com.cmg.android.bbcaccentamt.activity.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.view.CustomGraphView;
import com.cmg.android.bbcaccentamt.data.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccentamt.data.ScoreDBAdapter;
import com.cmg.android.bbcaccentamt.data.SphinxResult;
import com.cmg.android.bbcaccentamt.utils.AndroidHelper;
import com.cmg.android.bbcaccentamt.utils.ColorHelper;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;


public class GraphFragment extends FragmentTab {

    private ScoreDBAdapter dbAdapter;

    private PhonemeScoreDBAdapter phonemeScoreDBAdapter;

    private CustomGraphView graph;

    private String word;

    private String phoneme;

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (dbAdapter == null)
            dbAdapter = new ScoreDBAdapter(getActivity());
        if (phonemeScoreDBAdapter == null)
            phonemeScoreDBAdapter = new PhonemeScoreDBAdapter(getActivity());
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
        Bundle bundle =  getArguments();
        if (bundle != null)
            word = bundle.getString(ARG_WORD);
        loadScore();
        return v;
    }

    private void loadScore() {
        if (phoneme != null && phoneme.length() > 0) {
            graph.setTintText(phoneme);
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
        Collection<SphinxResult.PhonemeScore> scores = null;
        try {
            phonemeScoreDBAdapter.open();
            if (phoneme == null || phoneme.length() == 0) {
                scores = phonemeScoreDBAdapter.toCollection(phonemeScoreDBAdapter.getAll());
            } else {
                scores = phonemeScoreDBAdapter.toCollection(phonemeScoreDBAdapter.getByPhoneme(phoneme));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                phonemeScoreDBAdapter.close();
            } catch (Exception ex) {

            }
        }
        if (scores != null && scores.size() > 0) {
            int size = scores.size();
            DataPoint[] points = new DataPoint[size];
            Iterator<SphinxResult.PhonemeScore> scoreIterator = scores.iterator();
            int i = 0;
            float latestScore = -1;
            while (scoreIterator.hasNext()) {
                SphinxResult.PhonemeScore score = scoreIterator.next();
                if (latestScore == -1)
                    latestScore = score.getTotalScore();
                DataPoint dataPoint = new DataPoint(size - 1 - i, score.getTotalScore());
                points[size - 1 - i] = dataPoint;
                i++;
            }
            drawData(points, latestScore);
        }
    }

    private void loadWordScore() {
        Collection<ScoreDBAdapter.PronunciationScore> scores = null;
        try {
            dbAdapter.open();
            if (word == null || word.length() == 0) {
                scores = dbAdapter.toCollection(dbAdapter.getAll());
            } else {
                scores = dbAdapter.toCollection(dbAdapter.getByWord(word));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                dbAdapter.close();
            } catch (Exception ex) {

            }
        }
        if (scores != null && scores.size() > 0) {
            int size = scores.size();
            DataPoint[] points = new DataPoint[size];
            Iterator<ScoreDBAdapter.PronunciationScore> scoreIterator = scores.iterator();
            int i = 0;
            float latestScore = -1;
            while (scoreIterator.hasNext()) {
                ScoreDBAdapter.PronunciationScore score = scoreIterator.next();
                if (latestScore == -1)
                    latestScore = score.getScore();
                DataPoint dataPoint = new DataPoint(size - 1 - i, score.getScore());
                points[size - 1 - i] = dataPoint;
                i++;
            }
            drawData(points, latestScore);
        }
    }

    private void drawData(DataPoint[] points, float latestScore) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        series.setDrawDataPoints(true);


        Paint paint = new Paint();
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
        series.setCustomPaint(paint);
        if (latestScore >= 80.0f) {
            paint.setColor(ColorHelper.COLOR_GREEN);
            series.setColor(ColorHelper.COLOR_GREEN);
        } else if (latestScore >= 45.0f) {
            paint.setColor(ColorHelper.COLOR_ORANGE);
            series.setColor(ColorHelper.COLOR_ORANGE);
        } else {
            paint.setColor(ColorHelper.COLOR_RED);
            series.setColor(ColorHelper.COLOR_RED);
        }
        //series.setDrawDataPoints(true);
        //series.setThickness(2);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.getViewport().scrollToEnd();
    }
}
