package com.cmg.android.bbcaccent.extra;

/**
 * Created by luhonghai on 23/10/2015.
 */
public class SwitchFragmentParameter {

    private boolean useAnimation = false;

    private boolean addToBackStack = false;

    private boolean createNew = false;

    private String title;

    public SwitchFragmentParameter() {}

    public SwitchFragmentParameter(boolean useAnimation, boolean addToBackStack) {
        this.useAnimation = useAnimation;
        this.addToBackStack = addToBackStack;
    }

    public SwitchFragmentParameter(boolean useAnimation, boolean addToBackStack, boolean createNew) {
        this(useAnimation, addToBackStack);
        this.createNew = createNew;
    }

    public boolean isUseAnimation() {
        return useAnimation;
    }

    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    public boolean isAddToBackStack() {
        return addToBackStack;
    }

    public void setAddToBackStack(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
    }

    public boolean isCreateNew() {
        return createNew;
    }

    public void setCreateNew(boolean createNew) {
        this.createNew = createNew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
