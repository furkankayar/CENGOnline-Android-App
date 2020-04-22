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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

        int[] images = {
                R.drawable.img_backtoschool,
                R.drawable.img_bookclub,
                R.drawable.img_breakfast,
                R.drawable.img_code,
                R.drawable.img_graduation,
                R.drawable.img_honors,
                R.drawable.img_learnlanguage,
                R.drawable.img_reachout,
                R.drawable.img_read
        };

        Random random  = new Random();
        int courseImage = images[random.nextInt(images.length)];

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");
        DatabaseReference newVal = coursesRef.push();
        Course course = new Course(newVal.getKey(), courseImage, courseName, courseSection, courseSubject, classCode, user.getKey(), Arrays.asList(user.getKey()), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
        newVal.setValue(course);
    }

    public void addStudentToCourse(String classCode, final DatabaseCallback callback){

        final DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("courses");
        Query query = courseRef.orderByChild("classCode").equalTo(classCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        final String courseKey = ds.getKey();
                        final Course course = ds.getValue(Course.class);
                        getCurrentUserWithAllInfo(new DatabaseCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                final User user = (User) result;
                                if(user.getRoles().contains(User.Role.TEACHER)){
                                    List<String> teacherList = course.getTeacherList();
                                    if(teacherList == null){
                                        teacherList = Arrays.asList(user.getKey());
                                    }
                                    else{
                                        if(teacherList.contains(user.getKey())){
                                            callback.onFailed("You have already enrolled!");
                                            return;
                                        }
                                        else{
                                            teacherList.add(user.getKey());
                                        }
                                    }
                                    course.setTeacherList(teacherList);
                                    courseRef.child(courseKey).setValue(course);
                                    callback.onSuccess("You enrolled the class successfully.");
                                }
                                else {

                                    List<String> studentList = course.getStudentList();
                                    if(studentList == null){
                                        studentList = Arrays.asList(user.getKey());
                                    }
                                    else{
                                        if(studentList.contains(user.getKey())){
                                            callback.onFailed("You have already enrolled!");
                                            return;
                                        }
                                        else{
                                            studentList.add(user.getKey());
                                        }
                                    }
                                    course.setStudentList(studentList);
                                    courseRef.child(courseKey).setValue(course);
                                    callback.onSuccess("You enrolled the class successfully.");
                                }
                            }
                            @Override
                            public void onFailed(String message) {
                                callback.onFailed("An error occurred!");
                            }
                        });
                    }
                }
                else{
                    callback.onFailed("Class is not found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("An error occurred!");
            }
        });
    }

    public void getAllCourses(final DatabaseCallback callback){



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference coursesRef = database.getReference().child("courses");

        getCurrentUserWithAllInfo(new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                final User user = (User)result;
                coursesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Course> selectedCourses = new ArrayList<Course>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Course course = ds.getValue(Course.class);
                            if(course.getStudentList() != null && course.getStudentList().contains(user.getKey())){
                                selectedCourses.add(course);
                            }
                            else if(course.getTeacherList() != null && course.getTeacherList().contains(user.getKey())){
                                selectedCourses.add(course);

                            }
                        }
                        callback.onSuccess(selectedCourses);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailed(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onFailed(String message) {
                callback.onFailed(message);
            }
        });
    }


    public void getUserWithKey(final String key, final DatabaseCallback callback){

        Query user = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("key").equalTo(key);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = null;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                }
                callback.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("Error occurred while fetching user with given key: " + key);
            }
        });
    }

    public void removeStudentFromCourse(String classCode, final DatabaseCallback callback){

        final DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("courses");
        Query query = courseRef.orderByChild("classCode").equalTo(classCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        final String courseKey = ds.getKey();
                        final Course course = ds.getValue(Course.class);
                        getCurrentUserWithAllInfo(new DatabaseCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                final User user = (User) result;
                                if(user.getRoles().contains(User.Role.TEACHER)){
                                    List<String> teacherList = course.getTeacherList();
                                    if(teacherList != null){
                                        if(teacherList.contains(user.getKey())){
                                            teacherList.remove(user.getKey());
                                            course.setTeacherList(teacherList);
                                            courseRef.child(courseKey).setValue(course);
                                            callback.onSuccess("You are no longer teacher of this course!");
                                            return;
                                        }
                                    }
                                }
                                else {
                                    List<String> studentList = course.getStudentList();
                                    if(studentList != null){
                                        if(studentList.contains(user.getKey())){
                                            studentList.remove(user.getKey());
                                            course.setStudentList(studentList);
                                            courseRef.child(courseKey).setValue(course);
                                            callback.onSuccess("You are no longer student of this course!");
                                            return;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailed(String message) {
                                callback.onFailed("An error occurred!");
                            }
                        });
                    }
                }
                else{
                    callback.onFailed("Class is not found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed("An error occurred!");
            }
        });
    }

    public void removeCourse(final String courseCode, final DatabaseCallback callback){

        final DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("courses");
        getCurrentUserWithAllInfo(new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                final User user = (User)result;
                Query query = courseRef.orderByChild("classCode").equalTo(courseCode);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Course course = null;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                course = ds.getValue(Course.class);
                            }
                            if(course != null && dataSnapshot.getKey() != null && course.getCreatedBy().equals(user.getKey())){
                                courseRef.child(course.getKey()).removeValue();
                                callback.onSuccess("You have removed course successfully!");
                            }
                            else{
                                callback.onFailed("An error occurred!");
                            }
                        }
                        else{
                            callback.onFailed("An error occurred!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailed("An error occurred!");
                    }
                });
            }

            @Override
            public void onFailed(String message) {
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
