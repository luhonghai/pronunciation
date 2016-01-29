package com.cmg.android.bbcaccent.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmg.android.bbcaccent.data.dto.StudentMappingTeacher;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;


import java.util.List;


/**
 * Created by CMGT400 on 7/13/2015.
 */
public class CustomAdapterTeacher extends ArrayAdapter<StudentMappingTeacher> {


    Activity context=null;
    List<StudentMappingTeacher> myArray=null;
    int layoutId;

    public CustomAdapterTeacher(Activity context, int layoutId, List<StudentMappingTeacher> arr){
        super(context, layoutId, arr);
        this.context= context;
        this.layoutId= layoutId;
        this.myArray= arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);
        if(myArray.size()>0 && position>=0){
           TextView txtTeacher=(TextView)convertView.findViewById(R.id.textStatement);
            TextView txtStatus=(TextView)convertView.findViewById(R.id.textStatement);
            CircleCardView btnAccept=(CircleCardView)convertView.findViewById(R.id.accept);
            CircleCardView btnReject=(CircleCardView)convertView.findViewById(R.id.reject);
            LinearLayout itemListview=(LinearLayout)convertView.findViewById(R.id.itemListview);
            final String mailTeacher=myArray.get(position).getTeacherName();
            btnAccept.setTag(myArray.get(position));
            btnReject.setTag(myArray.get(position));
            String status=myArray.get(position).getStatus();
            switch (status){
                case "accept":
                    txtTeacher.setText(mailTeacher);
                    txtStatus.setText("accepted");
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.app_green));
                    btnReject.setVisibility(View.GONE);
                    btnAccept.setVisibility(View.GONE);
                    break;
                case "reject":
                    txtTeacher.setText(mailTeacher);
                    txtStatus.setText("rejected");
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.app_dark_gray));
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    break;
                case "pending":
                    txtTeacher.setText(mailTeacher);
                    break;
                default:
                    txtTeacher.setText(mailTeacher);
                    break;
            }
            btnAccept.setOnClickListener((View.OnClickListener) context);
            btnReject.setOnClickListener((View.OnClickListener) context);
        }

        return convertView;
    }
}
