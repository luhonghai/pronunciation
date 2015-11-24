package com.cmg.android.bbcaccent.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;

/**
 * Created by luhonghai on 23/11/2015.
 */
public class PopupShowcaseHelper {

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

    public PopupShowcaseHelper(Context context, HelpItem[] helpItems, OnSelectHelpItem onSelectHelpItem) {
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
            final ListView listView = (ListView) helpDialog.findViewById(R.id.listView);
            HelpAdapter helpAdapter = new HelpAdapter(context, helpItems);
            listView.setAdapter(helpAdapter);
            helpAdapter.notifyDataSetChanged();
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidHelper.setListViewHeightBasedOnItems(listView);
                }
            }, 50);
            helpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    resetTiming();
                }
            });
        }
        if (!helpDialog.isShowing()) {
            helpDialog.show();
        }
    }

    class HelpAdapter extends BaseAdapter {

        final HelpItem[] helpItems;

        final Context context;

        final LayoutInflater inflater;

        public HelpAdapter(Context context, HelpItem[] helpItems) {
            this.context = context;
            this.helpItems = helpItems;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            View root = inflater.inflate(helpItem.layoutId, parent, false);
            ((TextView) root.findViewById(R.id.txtTitle)).setText(helpItem.name);
            CircleCardView cardView = (CircleCardView) root.findViewById(R.id.btnHelpImage);
            switch (helpItem.layoutId) {
                case R.layout.choose_help_item:
                    cardView.setCardBackgroundColor(helpItem.iconColor);
                    ((TextView)cardView.getChildAt(0)).setText(helpItem.iconText);
                    break;
                case R.layout.choose_help_item_image:
                    ((ImageView)root.findViewById(R.id.imgHelpIcon)).setImageResource(helpItem.iconImage);
                    break;
                case R.layout.choose_help_item_image_button:
                    cardView.setCardBackgroundColor(helpItem.iconColor);
                    ((ImageView)cardView.getChildAt(0)).setImageResource(helpItem.iconImage);
                    break;
            }
            View view = root.findViewById(R.id.llContainer);
            view.setTag(helpItem);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helpDialog != null && helpDialog.isShowing()) helpDialog.dismiss();
                    onSelectHelpItem.onSelectHelpItem((HelpItem) v.getTag());
                }
            });
            return root;
        }
    }
}
