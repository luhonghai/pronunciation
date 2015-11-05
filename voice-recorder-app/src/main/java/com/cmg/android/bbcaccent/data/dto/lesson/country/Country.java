package com.cmg.android.bbcaccent.data.dto.lesson.country;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class Country extends BaseLessonEntity {

    @LiteColumn
    private String name;

    @LiteColumn
    private String description;

    @LiteColumn
    private String imageUrl;

    @LiteColumn
    private boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
