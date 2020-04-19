package com.example.cengonline.post;

import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

import java.util.List;

public class Assignment extends AbstractPost {

    private String title;
    private String dueDate;
    private List<User> assignedStudents;

    public Assignment(){
        super();
    }

    public Assignment(String key, String title, String dueDate, List<User> assignedStudents, String postedBy, String postedAt, String body){
        super(key, postedBy, postedAt, body);
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

    @Override
    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy.getRoles().contains(User.Role.TEACHER) ? editedBy.getKey() : null;
    }

    @Override
    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy.getRoles().contains(User.Role.TEACHER) ? postedBy.getKey() : null;
    }
}
