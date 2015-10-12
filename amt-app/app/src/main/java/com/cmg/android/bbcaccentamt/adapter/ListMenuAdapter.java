package com.cmg.android.bbcaccentamt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmg.android.bbcaccentamt.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luhonghai on 4/10/15.
 */
public class ListMenuAdapter extends BaseAdapter {

    private  List<Integer> menuText = Arrays.asList(
            R.string.menu_help,
            R.string.menu_settings,
            R.string.menu_about,
            R.string.menu_feedback,
            R.string.menu_logout);
    private  List<Integer> menuIcon = Arrays.asList(
            R.drawable.p_menu_help,
            R.drawable.p_menu_setting,
            R.drawable.p_menu_about,
            R.drawable.p_menu_feedback,
            R.drawable.p_logout_red);

    private ViewHolder view;

    private final Context context;

    public ListMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return menuText.size();
    }

    @Override
    public Object getItem(int position) {
        return menuText.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(context);

        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_menu_item,
                    null);
            view.imageButton = (ImageButton) convertView.findViewById(R.id.btnMenuImage);
            view.textView = (TextView) convertView.findViewById(R.id.txtMenuItem);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();

        }
        view.textView.setText(context.getString(menuText.get(position)));
        if (position == 4) {
            view.textView.setTextColor(context.getResources().getColor(R.color.app_red));
        }
        view.imageButton.setImageResource(menuIcon.get(position));
        return convertView;
    }

    private static class ViewHolder {

        ImageButton imageButton;

        TextView textView;
    }
}
