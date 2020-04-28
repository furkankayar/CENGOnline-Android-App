package com.example.cengonline.post;

import com.example.cengonline.model.MyTimestamp;
import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

import java.sql.Timestamp;
import java.util.List;

public class Assignment extends AbstractPost {

    private String title;
    private String dueDate;
    private List<User> assignedStudents;

    public Assignment(){
        super();
    }

    public Assignment(String title, String dueDate, List<User> assignedStudents, String postedBy, MyTimestamp postedAt, String body){
        super(postedBy, postedAt, body);
        this.title = title;
        this.dueDate = dueDate;
        this.assignedStudents = assignedStudents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setAssignedStudents(List<User> assignedStudents){
        this.assignedStudents = assignedStudents;
    }

    public List<User> getAssignedStudents(){
        return this.assignedStudents;
    }

    public List<User> addAssignedStudents(User student){
        if(student.getRoles().contains(User.Role.STUDENT))
            this.assignedStudents.add(student);

        return this.assignedStudents;
    }

}
