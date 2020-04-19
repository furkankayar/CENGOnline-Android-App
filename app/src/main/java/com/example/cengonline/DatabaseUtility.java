package com.example.cengonline;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cengonline.model.Course;
import com.example.cengonline.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DatabaseUtility {

    private static DatabaseUtility instance;

    private DatabaseUtility(){

    }

    public void getCurrentUserWithAllInfo(final DatabaseCallback callback){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users");
        Query query = ref.orderByChild("uid").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null){
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        callback.onSuccess(childSnapshot.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("An error occurred try again please!");
            }
        });
    }

    public void getRandomClassCode(final DatabaseCallback callback){

        final String code = UUID.randomUUID().toString().substring(0, 7);
        final DatabaseReference courseIdsRef = FirebaseDatabase.getInstance().getReference().child("courseCodeIds");
        Query query = courseIdsRef.equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    courseIdsRef.push().setValue(code);
                    callback.onSuccess(code);
                }
                else{
                    callback.onFailed("An error occurred try again please!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("An error occurred try again please!");
            }
        });
    }

    public void saveNewCourse(String courseName, String courseSection, String courseSubject, String classCode, User user){

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");
        DatabaseReference newVal = coursesRef.push();
        Course course = new Course(newVal.getKey(), courseName, courseSection, courseSubject, classCode, user.getKey(), Arrays.asList(user.getKey()), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
        newVal.setValue(course);
    }

    public void addStudentToCourse(String classCode, final DatabaseCallback callback){

        final DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("courses");
        Query query = courseRef.orderByChild("classCode").equalTo(classCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        final String courseKey = ds.getKey();
                        final Course course = ds.getValue(Course.class);
                        getCurrentUserWithAllInfo(new DatabaseCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                final User student = (User) result;
                                List<String> studentList = course.getStudentList();
                                if(studentList == null){
                                    studentList = Arrays.asList(student.getKey());
                                }
                                else{
                                    if(studentList.contains(student.getKey())){
                                        callback.onFailed("You have already enrolled!");
                                        return;
                                    }
                                    else{
                                        studentList.add(student.getKey());
                                    }
                                }
                                course.setStudentList(studentList);
                                courseRef.child(courseKey).setValue(course);
                                callback.onSuccess("You enrolled the class successfully.");
                            }

                            @Override
                            public void onFailed(String message) {
                                callback.onFailed("An error occurred!");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("An error occurred!");
            }
        });
    }

    public static DatabaseUtility getInstance(){
        if(instance == null){
            instance = new DatabaseUtility();
        }

        return instance;
    }
}
