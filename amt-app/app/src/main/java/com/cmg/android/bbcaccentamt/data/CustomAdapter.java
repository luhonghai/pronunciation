package com.cmg.android.bbcaccentamt.data;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.cmg.android.bbcaccentamt.R;

import java.util.List;

/**
 * Created by CMGT400 on 7/13/2015.
 */
public class CustomAdapter extends  ArrayAdapter<SentenceModel> {


    Activity context=null;
    List<SentenceModel> myArray=null;
    int layoutId;

    public CustomAdapter(Activity context, int layoutId, List<SentenceModel> arr){
        super(context, layoutId, arr);
        this.context= context;
        this.layoutId= layoutId;
        this.myArray= arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);
        if(myArray.size()>0 && position>=0){
          TextView txtdisplay=(TextView)convertView.findViewById(R.id.textStatement);
            String sentence=myArray.get(position).getSentence();
            int status=myArray.get(position).getStatus();
            switch (status){
                case 0:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_black));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_white));
                    break;
                case -1:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_white));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_default));
                    break;
                case 1:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_white));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_orange));
                    break;
                case 2:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_white));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_red));
                    break;
                case 3:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_white));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_green));
                    break;
                case 4:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_white));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_gray));
                    break;
                default:
                    txtdisplay.setText(sentence);
                    txtdisplay.setTextColor(context.getResources().getColor(R.color.app_black));
                    txtdisplay.setBackgroundColor(context.getResources().getColor(R.color.app_white));
                    break;
            }

        }
        return convertView;
    }

}
