package com.cmg.android.voicerecorder.activity.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.voicerecorder.R;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;
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
        //graph.getLegendRenderer().setVisible(false);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().setNumVerticalLabels(3);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setPadding(30);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(50);
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
            DataPoint[] points = new DataPoint[scores.size()];
            Iterator<ScoreDBAdapter.PronunciationScore> scoreIterator = scores.iterator();
            int i = 0;
            while (scoreIterator.hasNext()) {
                ScoreDBAdapter.PronunciationScore score = scoreIterator.next();
                DataPoint dataPoint = new DataPoint(i, score.getScore());
                points[i] = dataPoint;
                i++;
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
            //series.setDrawDataPoints(true);
            //series.setThickness(2);
            graph.addSeries(series);
        }
    }
}
