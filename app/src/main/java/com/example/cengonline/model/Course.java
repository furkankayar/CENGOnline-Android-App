package com.example.cengonline.model;

import com.example.cengonline.post.Announcement;
import com.example.cengonline.post.Assignment;

import java.util.List;

public class Course {

    private String className;
    private String classSection;
    private String classSubject;
    private String classCode;
    private List<User> teacherList;
    private List<User> studentList;
    private List<Assignment> assignmentList;
    private List<Announcement> announcementList;

    public Course(){

    }

    public Course(String className, String classSection, String classSubject, String classCode, List<User> teacherList, List<User> studentList, List<Assignment> assignmentList, List<Announcement> announcementList) {
        this.className = className;
        this.classSection = classSection;
        this.classSubject = classSubject;
        this.classCode = classCode;
        this.teacherList = teacherList;
        this.studentList = studentList;
        this.assignmentList = assignmentList;
        this.announcementList = announcementList;
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

    public List<User> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<User> teacherList) {
        this.teacherList = teacherList;
    }

    public List<User> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<User> studentList) {
        this.studentList = studentList;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    public List<User> enrollStudent(User user){
        if(user.getRoles().contains(User.Role.STUDENT))
            this.studentList.add(user);
        return this.studentList;
    }

    public List<User> enrollTeacher(User user){
        if(user.getRoles().contains(User.Role.TEACHER))
            this.teacherList.add(user);
        return this.teacherList;
    }
}
