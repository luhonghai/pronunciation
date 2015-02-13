package com.cmg.android.voicerecorder.activity.fragment;

import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.voicerecorder.R;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;
import com.cmg.android.voicerecorder.utils.ColorHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;


public class GraphFragment extends FragmentTab {

    private ScoreDBAdapter dbAdapter;
    private GraphView graph;

    private String word;
    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbAdapter = new ScoreDBAdapter(getActivity());
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = (GraphView) v.findViewById(R.id.graphScore);
        isLoadedView = true;
        //graph.getLegendRenderer().setVisible(false);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().setNumVerticalLabels(3);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setPadding(30);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setMinX(0);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        Bundle bundle =  getArguments();
        if (bundle != null)
            word = bundle.getString(ARG_WORD);
        loadScore();
        return v;
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

    private void loadScore() {
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
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(4);
            series.setThickness(5);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(new CornerPathEffect(5));
            paint.setStrokeWidth(5);
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
}
