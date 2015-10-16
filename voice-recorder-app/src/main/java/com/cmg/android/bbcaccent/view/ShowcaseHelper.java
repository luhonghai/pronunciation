package com.cmg.android.bbcaccent.view;

import android.app.Activity;
import android.view.View;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
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
        DETAIL_SELECT_PHONEME("SELECT_PHONEME")
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

    public void showHelp(HelpKey helpKey, HelpState... helpStates) {
        showHelp(helpKey,new ShowcaseConfig(), helpStates);
    }

    public void showHelp(HelpKey helpKey, ShowcaseConfig config, HelpState... helpStates) {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, helpKey.toString());
        sequence.setConfig(config);
        if (helpStates != null && helpStates.length > 0) {
            for (HelpState helpState : helpStates) {
                sequence.addSequenceItem(helpState.target, helpState.content);
            }
        }
        sequence.start();
    }
}
