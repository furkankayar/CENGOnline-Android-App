package com.example.cengonline.model;

import com.example.cengonline.post.Announcement;
import com.example.cengonline.post.Assignment;

import java.util.List;

public class Course {

    private String key;
    private String className;
    private String classSection;
    private String classSubject;
    private String classCode;
    private String createdBy;
    private List<String> teacherList;
    private List<String> studentList;
    private List<String> assignmentList;
    private List<String> announcementList;

    public Course(){

    }

    public Course(String key, String className, String classSection, String classSubject, String classCode, String createdBy, List<String> teacherList, List<String> studentList, List<String> assignmentList, List<String> announcementList) {
        this.key = key;
        this.className = className;
        this.classSection = classSection;
        this.classSubject = classSubject;
        this.classCode = classCode;
        this.createdBy = createdBy;
        this.teacherList = teacherList;
        this.studentList = studentList;
        this.assignmentList = assignmentList;
        this.announcementList = announcementList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(String classSubject) {
        this.classSubject = classSubject;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<String> teacherList) {
        this.teacherList = teacherList;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
    }

    public List<String> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<String> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public List<String> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<String> announcementList) {
        this.announcementList = announcementList;
    }

    public List<String> enrollStudent(User user){
        if(user.getRoles().contains(User.Role.STUDENT))
            this.studentList.add(user.getKey());
        return this.studentList;
    }

    public List<String> enrollTeacher(User user){
        if(user.getRoles().contains(User.Role.TEACHER))
            this.teacherList.add(user.getKey());
        return this.teacherList;
    }
}
