package com.cmg.android.bbcaccent.view;

import android.app.Activity;
import android.view.View;

import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.target.Target;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * Created by luhonghai on 16/10/2015.
 */
public class ShowcaseHelper {

    public enum HelpKey {
        ANALYZING_WORD("ANALYZING_WORD"),
        SEARCH_WORD("SEARCH_WORD"),
        SWIPE_SLIDER("SWIPE_SLIDER"),
        SELECT_SCORE("SELECT_SCORE"),
        DETAIL_SELECT_PHONEME("SELECT_PHONEME"),
        IPA_PHONETIC("IPA_PHONETIC")
        ;
        String name;
        HelpKey(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class HelpState {
        Target target;
        String content;
        public HelpState(Target target, String content) {
            this.target = target;
            this.content = content;
        }

        public HelpState(View view, String content) {
            this.target = new ViewTarget(view);
            this.content = content;
        }
    }

    private final Activity activity;

    public ShowcaseHelper(Activity activity) {
        this.activity = activity;
    }

    public ShowcaseHelper showHelp(HelpKey helpKey, HelpState... helpStates) {
        return showHelp(helpKey,new ShowcaseConfig(), helpStates);
    }

    public ShowcaseHelper showHelp(HelpKey helpKey, ShowcaseConfig config, HelpState... helpStates) {
        try {
            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, helpKey.toString());
            sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                @Override
                public void onDismiss(MaterialShowcaseView itemView, int position) {
                    MainBroadcaster.getInstance().getSender().sendMessage(MainBroadcaster.Filler.RESET_TIMING_HELP, null);
                }
            });
            sequence.setConfig(config);
            if (helpStates != null && helpStates.length > 0) {
                for (HelpState helpState : helpStates) {
                    sequence.addSequenceItem(helpState.target, helpState.content);
                }
            }
            sequence.start();
        } catch (Exception e) {
            SimpleAppLog.error("",e);
        }
        return this;
    }

    public ShowcaseHelper showHelp(HelpState... helpStates) {
        return showHelp(new ShowcaseConfig(), helpStates);
    }

    public ShowcaseHelper showHelp(ShowcaseConfig config, HelpState... helpStates) {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity);
        sequence.setConfig(config);
        if (helpStates != null && helpStates.length > 0) {
            for (HelpState helpState : helpStates) {
                sequence.addSequenceItem(helpState.target, helpState.content);
            }
        }
        sequence.start();
        return this;
    }
}
