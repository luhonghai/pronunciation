package com.cmg.lesson.data.dto.country;

import com.cmg.lesson.data.jdo.country.CountryMappingCourse;

import java.util.List;

/**
 * Created by CMG Dev156 on 11/2/2015.
 */
public class CountryMappingCourseDTO {
    private String message;
    List<CountryMappingCourse> data;
    CountryMappingCourse item;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CountryMappingCourse> getData() {
        return data;
    }

    public void setData(List<CountryMappingCourse> data) {
        this.data = data;
    }

    public CountryMappingCourse getItem() {
        return item;
    }

    public void setItem(CountryMappingCourse item) {
        this.item = item;
    }
}
