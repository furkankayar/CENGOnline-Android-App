package com.example.cengonline.model;


import java.util.List;

public class CourseInformationStorage<T> {

    protected List<T> dataList;
    protected String courseKey;
    protected String key;


    protected CourseInformationStorage(){
    }

    protected CourseInformationStorage(String key, List<T> dataList, String courseKey){
        this.key = key;
        this.dataList = dataList;
        this.courseKey = courseKey;
    }
}
