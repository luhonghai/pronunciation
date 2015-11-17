package com.cmg.android.bbcaccent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luhonghai on 4/10/15.
 */
public class ListMenuAdapter extends BaseAdapter {

    public static final MenuItem[] DEFAULT_MENU_ITEMS = new MenuItem[] {
            MenuItem.FREESTYLE,
            MenuItem.LESSON,
            MenuItem.ACTIVATE_SUBSCRIPTION,
            MenuItem.SETTING,
            MenuItem.ABOUT,
            MenuItem.LICENCE,
            MenuItem.FEEDBACK,
            MenuItem.LOGOUT,
    };

    public enum MenuItem {
        FREESTYLE(R.string.menu_freestyle, R.drawable.p_menu_freestyle),
        LESSON(R.string.menu_lesson, R.drawable.p_menu_lesson),
        HELP(R.string.menu_help, R.drawable.p_menu_help),
        ACTIVATE_SUBSCRIPTION(R.string.menu_subscription, R.drawable.p_menu_help),
        SETTING(R.string.menu_settings, R.drawable.p_menu_setting),
        ABOUT(R.string.menu_about, R.drawable.p_menu_about),
        LICENCE(R.string.menu_licence, R.drawable.p_menu_license),
        FEEDBACK(R.string.menu_feedback, R.drawable.p_menu_feedback),
        SUBSCRIPTION(R.string.menu_subscription, R.drawable.p_menu_feedback),
        LOGOUT(R.string.menu_logout, R.drawable.p_logout_red)
        ;
        int stringId;
        int drawableId;
        MenuItem(int stringId, int drawableId) {
            this.stringId = stringId;
            this.drawableId = drawableId;
        }

        public static MenuItem fromName(String name) {
            for (MenuItem menuItem : values()) {
                if (menuItem.toString().equals(name)) return menuItem;
            }
            return null;
        }

        @Override
        public String toString() {
            return MainApplication.getContext().getString(stringId);
        }
    }

    private ViewHolder view;

    private final Context context;

    private LayoutInflater inflater;

    private MenuItem[] menuItems;

    public ListMenuAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        menuItems = DEFAULT_MENU_ITEMS;
    }

    public ListMenuAdapter(Context context, MenuItem[] menuItems) {
        this(context);
        this.menuItems = menuItems;
    }

    public MenuItem[] getMenuItems() {
        return this.menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public Object getItem(int position) {
        return menuItems[position];
    }

    @Override
    public long getItemId(int position) {
        return menuItems[position].stringId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_menu_item,
                    null);
            view.imageButton = (ImageButton) convertView.findViewById(R.id.btnMenuImage);
            view.textView = (TextView) convertView.findViewById(R.id.txtMenuItem);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();

        }
        MenuItem item = menuItems[position];
        view.textView.setText(item.toString());
        if (item == MenuItem.LOGOUT) {
            view.textView.setTextColor(context.getResources().getColor(R.color.app_red));
        }
        view.imageButton.setImageResource(item.drawableId);
        return convertView;
    }

    private static class ViewHolder {

        ImageButton imageButton;

        TextView textView;
    }
}
