package com.cmg.android.bbcaccent.view.dialog;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.CursorRecyclerViewAdapter;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

/**
 * Created by luhonghai on 27/10/2015.
 */
public class LanguageDialog extends DefaultDialog {

    private final RecyclerView recyclerView;

    private final LessonDBAdapterService dbAdapterService;

    public LanguageDialog(Context context) {
        super(context, R.layout.choose_language);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbAdapterService = new LessonDBAdapterService();
        Cursor cursor = dbAdapterService.cursorAllCountry();
        if (cursor != null) {
            SimpleAppLog.info("Number of supported country " + cursor.getCount());
        }
        Adapter adapter = new Adapter(context, cursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    public class Adapter extends CursorRecyclerViewAdapter<ViewHolder> {

        public Adapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            try {
                Country country = dbAdapterService.toObject(cursor, Country.class);
                if (country.getName().equalsIgnoreCase("thailand")) {
                    viewHolder.imgCountry.setImageResource(R.drawable.th_round);
                    viewHolder.txtTitle.setText("ประเทศไทย");
                } else if (country.getName().equalsIgnoreCase("vietnamese")) {
                    viewHolder.imgCountry.setImageResource(R.drawable.vn_round);
                    viewHolder.txtTitle.setText("Tiếng Việt");
                } else {
                    viewHolder.imgCountry.setImageResource(R.drawable.gb_round);
                    viewHolder.txtTitle.setText("English");
                }
                viewHolder.llContainer.setTag(country);
                viewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserProfile userProfile = Preferences.getCurrentProfile(MainApplication.getContext());
                        if (userProfile != null) {
                            userProfile.setSelectedCountry((Country) view.getTag());
                            Preferences.updateProfile(MainApplication.getContext(), userProfile);
                        }
                        LanguageDialog.this.dismiss();
                    }
                });
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not cast cursor to object",e);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.choose_language_item, parent, false);
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCountry;
        TextView txtTitle;
        LinearLayout llContainer;
        public ViewHolder(View itemView) {
            super(itemView);
            imgCountry = (ImageView) itemView.findViewById(R.id.imgCountry);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            llContainer = (LinearLayout) itemView.findViewById(R.id.llContainer);
        }
    }
}
