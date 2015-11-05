package com.cmg.android.bbcaccent.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cmg on 6/2/15.
 */
public class AlwaysMarqueeTextView extends TextView {

    protected boolean a;

    public AlwaysMarqueeTextView(Context context)
    {
        super(context);
        setAlwaysMarquee(true);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        setAlwaysMarquee(true);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setAlwaysMarquee(true);
    }

    public boolean isFocused()
    {
        return a || super.isFocused();
    }

    public void setAlwaysMarquee(boolean flag)
    {
        setSelected(flag);
        setSingleLine(flag);
        if(flag) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        } else {
            setEllipsize(null);
        }
        a = flag;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
    {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused)
    {
        if(focused)
            super.onWindowFocusChanged(focused);
    }
}
