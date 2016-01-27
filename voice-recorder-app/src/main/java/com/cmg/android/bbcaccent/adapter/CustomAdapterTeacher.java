package com.cmg.android.bbcaccent.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmg.android.bbcaccent.data.dto.StudentMappingTeacher;
import com.cmg.android.bbcaccent.R;


import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
            Button btnAccept=(Button)convertView.findViewById(R.id.accept);
            Button btnReject=(Button)convertView.findViewById(R.id.reject);
            LinearLayout itemListview=(LinearLayout)convertView.findViewById(R.id.itemListview);
            final String mailTeacher=myArray.get(position).getTeacherName();
            btnAccept.setTag(myArray.get(position));
            btnReject.setTag(myArray.get(position));
            String status=myArray.get(position).getStatus();
            switch (status){
                case "accept":
                    txtTeacher.setText(mailTeacher);
                    btnReject.setVisibility(View.GONE);
                    btnAccept.setActivated(false);
                    btnAccept.setText("accepted");
                    btnAccept.setBackgroundColor(context.getResources().getColor(R.color.app_dark_gray));
                    itemListview.setBackgroundColor(context.getResources().getColor(R.color.app_white));
                    break;
                case "reject":
                    txtTeacher.setText(mailTeacher);
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setActivated(false);
                    itemListview.setBackgroundColor(context.getResources().getColor(R.color.app_light_gray));
                    break;
                case "pending":
                    txtTeacher.setText(mailTeacher);
                    itemListview.setBackgroundColor(context.getResources().getColor(R.color.app_orange));
                    break;
                default:
                    txtTeacher.setText(mailTeacher);
                    itemListview.setBackgroundColor(context.getResources().getColor(R.color.app_white));
                    break;
            }
            btnAccept.setOnClickListener((View.OnClickListener) context);
            btnReject.setOnClickListener((View.OnClickListener) context);
        }

        return convertView;
    }
}
