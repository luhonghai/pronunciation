package com.cmg.android.bbcaccent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by luhonghai on 4/10/15.
 */
public class ListMenuAdapter extends BaseAdapter {

    public static final MenuItem[] LITE_MENU_ITEMS = new MenuItem[] {
            MenuItem.ACTIVATE_SUBSCRIPTION,
            MenuItem.LESSON,
            MenuItem.IPA,
            MenuItem.SETTING,
            MenuItem.ABOUT,
            MenuItem.LICENCE,
            MenuItem.FEEDBACK,
            MenuItem.LOGOUT,
    };

    public static final MenuItem[] FULL_MENU_ITEMS = new MenuItem[] {
            MenuItem.FREESTYLE,
            MenuItem.LESSON,
            MenuItem.IPA,
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
        IPA(R.string.menu_ipa,R.drawable.p_menu_ipa),
        ACTIVATE_SUBSCRIPTION(R.string.menu_subscription, R.drawable.ic_menu_accenteasy, ColorHelper.getColor(R.color.app_purple)),
        SETTING(R.string.menu_settings, R.drawable.p_menu_setting),
        ABOUT(R.string.menu_about, R.drawable.p_menu_about),
        LICENCE(R.string.menu_licence, R.drawable.p_menu_license),
        FEEDBACK(R.string.menu_feedback, R.drawable.p_menu_feedback),
        SUBSCRIPTION(R.string.menu_subscription, R.drawable.ic_menu_accenteasy),
        LOGOUT(R.string.menu_logout, R.drawable.p_logout_red, ColorHelper.getColor(R.color.app_red))
        ;
        int stringId;
        int drawableId;
        int textColor = ColorHelper.getColor(android.R.color.black);
        MenuItem(int stringId, int drawableId) {
            this.stringId = stringId;
            this.drawableId = drawableId;
        }

        MenuItem(int stringId, int drawableId, int textColor) {
            this(stringId, drawableId);
            this.textColor = textColor;
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
        menuItems = LITE_MENU_ITEMS;
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
            view.imageButton = (ImageView) convertView.findViewById(R.id.btnMenuImage);
            view.textView = (TextView) convertView.findViewById(R.id.txtMenuItem);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();

        }
        MenuItem item = menuItems[position];
        view.textView.setText(item.toString());

        ImageLoader.getInstance().displayImage("drawable://" + item.drawableId, view.imageButton);
        UserProfile profile = Preferences.getCurrentProfile();
        if (profile != null) {
            if (profile.isExpired()
                    && !profile.isPro()
                    && (item == MenuItem.FREESTYLE || item == MenuItem.LESSON)
                    ) {
                view.textView.setTextColor(ColorHelper.getColor(R.color.app_gray));
                view.imageButton.setColorFilter(ColorHelper.getColor(R.color.app_gray));
            } else {
                view.textView.setTextColor(item.textColor);
                view.imageButton.setColorFilter(Color.TRANSPARENT);
            }
        }
        return convertView;
    }

    private static class ViewHolder {

        ImageView imageButton;

        TextView textView;
    }
}
