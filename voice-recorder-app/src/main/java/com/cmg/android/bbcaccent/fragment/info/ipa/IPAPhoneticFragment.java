package com.cmg.android.bbcaccent.fragment.info.ipa;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.CursorRecyclerViewAdapter;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.cardview.GestureDetectorCardView;
import com.cmg.android.bbcaccent.view.dialog.DefaultCenterDialog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class IPAPhoneticFragment extends BaseFragment {

    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private PlayerHelper player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ipa_phonetic_fragment,  null);
        ButterKnife.bind(this, root);
        Bundle bundle = getArguments();
        IPAMapArpabet.IPAType ipaType = null;
        if (bundle != null) {
            if (bundle.containsKey(IPAMapArpabet.IPAType.class.getName())) {
                ipaType = IPAMapArpabet.IPAType.fromName(bundle.getString(IPAMapArpabet.IPAType.class.getName()));

            }
        }
        if (ipaType != null) {
            switch (ipaType) {
                case VOWEL:
                    txtTitle.setText(R.string.ipa_vowels_title);
                    break;
                case CONSONANT:
                    txtTitle.setText(R.string.ipa_consonant_title);
                default:
                    break;
            }
            try {
                Cursor cursor = LessonDBAdapterService.getInstance().cursorAllIPAMapArpabetByType(ipaType);
                Adapter adapter = new Adapter(getContext(), cursor);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not get ipa from database",e);
            }
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class Adapter extends CursorRecyclerViewAdapter<ViewHolder> {

        public Adapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            try {
                final IPAMapArpabet ipaMapArpabet = LessonDBAdapterService.getInstance().toObject(cursor, IPAMapArpabet.class);
                viewHolder.txtPhoneme.setText(ipaMapArpabet.getIpa());
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor(ipaMapArpabet.getColor()));
                viewHolder.cardView.setTag(ipaMapArpabet);
                viewHolder.cardView.setGestureDetector(new GestureDetectorCardView.GestureDetector() {
                    @Override
                    public void onShortPressed() {
                        playUrl(ipaMapArpabet.getMp3Url());
                    }

                    @Override
                    public void onLongPressed() {
                        Dialog dialog = new DefaultCenterDialog(getActivity(), R.layout.ipa_detail_dialog);
                        dialog.findViewById(R.id.btnAudio).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playUrl(ipaMapArpabet.getMp3Url());
                            }
                        });
                        ((TextView) dialog.findViewById(R.id.txtPhoneme)).setText(ipaMapArpabet.getIpa());
                        ((TextView) dialog.findViewById(R.id.txtDefinition)).setText(
                                String.format(Locale.getDefault(), "<%s> %s",
                                        ipaMapArpabet.getArpabet(),
                                        ipaMapArpabet.getWords())
                        );
                        ((HtmlTextView) dialog.findViewById(R.id.tv_content)).setHtmlFromString(ipaMapArpabet.getTip(), false);
                        dialog.show();
                    }
                });
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not parse ipa data",e);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ipa_item, parent, false);
            return new ViewHolder(v);
        }
    }

    private void playUrl(final String url) {
        if (url == null || url.length() == 0) return;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File file = new File(FileHelper.getCachedFilePath(url));
                if (file.exists()) {
                    play(file);
                } else {
                    SimpleAppLog.error("Could not found media to play " + url);
                }
                return null;
            }
        }.execute();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPhoneme;

        GestureDetectorCardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPhoneme = ButterKnife.findById(itemView, R.id.txtPhoneme);
            cardView = ButterKnife.findById(itemView, R.id.cvItemContainer);
        }
    }

    private void play(String file) {
        if (file == null || file.length() == 0) return;
        play(new File(file));
    }

    private void play(File file) {
        if (!file.exists()) return;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    try {
                        player.stop();
                    } catch (Exception ex) {

                    }
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}