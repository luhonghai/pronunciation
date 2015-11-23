package com.cmg.android.bbcaccent.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.view.ShowcaseHelper;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;

/**
 * Created by luhonghai on 23/11/2015.
 */
public class MainScreenShowcaseHelper {

    public interface OnSelectHelpItem {
        void onSelectHelpItem(HelpItem helpItem);
    }

    public enum HelpItem {
        ANALYZE_A_WORD(R.layout.choose_help_item_image_button, "Analyse a word", null, R.drawable.ic_record,
                ColorHelper.getColor(R.color.app_green)),
        PROGRESS(R.layout.choose_help_item_image_button, "Progress", null, R.drawable.p_graph_blue,
                ColorHelper.getColor(R.color.app_blue_sky)),
        HISTORY(R.layout.choose_help_item_image_button, "History", null, R.drawable.p_history_blue,
                ColorHelper.getColor(R.color.app_blue_sky)),
        TIPS(R.layout.choose_help_item_image_button, "Tips", null, R.drawable.p_tip_blue,
                ColorHelper.getColor(R.color.app_blue_sky)),
        PHONEME_HELP(R.layout.choose_help_item_image, "Phoneme help", null, R.drawable.p_tip_blue,
                Color.TRANSPARENT),
        LISTEN_AGAIN(R.layout.choose_help_item_image_button, "Listen again", null, R.drawable.ic_play,
                ColorHelper.getColor(R.color.app_green)),
        TRY_AGAIN(R.layout.choose_help_item_image, "Try again", null, R.drawable.p_help_score_99,
                Color.TRANSPARENT),
        NEXT_LESSON(R.layout.choose_help_item, "Next lesson", "Q", 0,
                ColorHelper.getColor(R.color.app_purple))
        ;
        int layoutId;
        String name;
        String iconText;
        int iconImage;
        int iconColor;

        HelpItem(int layoutId, String name, String iconText, int iconImage, int iconColor) {
            this.layoutId = layoutId;
            this.name = name;
            this.iconText = iconText;
            this.iconImage = iconImage;
            this.iconColor = iconColor;
        }
    }

    private static final int TIMEOUT = 30000;

    private final Context context;

    private final OnSelectHelpItem onSelectHelpItem;

    private Handler handler = new Handler();

    private Dialog helpDialog;

    private HelpItem[] helpItems;

    public MainScreenShowcaseHelper(Context context, HelpItem[] helpItems, OnSelectHelpItem onSelectHelpItem) {
        this.context = context;
        this.onSelectHelpItem = onSelectHelpItem;
        this.helpItems = helpItems;
    }

    public void recycle() {
        handler.removeCallbacksAndMessages(null);
    }

    public void resetTiming() {
        recycle();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        },TIMEOUT);
    }

    public void showDialog() {
        if (context == null) return;
        if (helpDialog == null) {
            helpDialog = new DefaultCenterDialog(context, R.layout.choose_help);
            ListView listView = (ListView) helpDialog.findViewById(R.id.listView);
            HelpAdapter helpAdapter = new HelpAdapter(context, helpItems);
            listView.setAdapter(helpAdapter);
            helpAdapter.notifyDataSetChanged();
        }
        if (!helpDialog.isShowing()) {
            helpDialog.show();
        }
    }

    class HelpAdapter extends BaseAdapter {

        final HelpItem[] helpItems;

        final Context context;

        public HelpAdapter(Context context, HelpItem[] helpItems) {
            this.context = context;
            this.helpItems = helpItems;
        }

        @Override
        public int getCount() {
            return helpItems.length;
        }

        @Override
        public Object getItem(int position) {
            return helpItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HelpItem helpItem = helpItems[position];
            View root = View.inflate(context, helpItem.layoutId, null);
            ((TextView) root.findViewById(R.id.txtTitle)).setText(helpItem.name);

            return root;
        }
    }
}
