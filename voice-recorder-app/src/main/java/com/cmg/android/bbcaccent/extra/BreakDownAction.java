package com.cmg.android.bbcaccent.extra;

/**
 * Created by luhonghai on 05/11/2015.
 */
public class BreakDownAction<T> {

    public enum Type {
        SELECT_MENU,
        SELECT_REDO,
        SELECT_NEXT,
        SELECT_QUESTION
    }

    private Type type;

    private T data;

    public BreakDownAction(Type type) {
        this.type = type;
    }

    public BreakDownAction(Type type, T data) {
        this(type);
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
