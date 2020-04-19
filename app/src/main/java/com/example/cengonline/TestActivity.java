package com.example.cengonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cengonline.model.Course;
import com.example.cengonline.model.User;
import com.example.cengonline.post.Announcement;
import com.example.cengonline.post.Assignment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity2";
    private static final String ARG_NAME = "username";

    public static void startActivity(Context context, String username) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(ARG_NAME, username);
        context.startActivity(intent);
    }

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView textView = findViewById(R.id.textViewWelcome);
        if (getIntent().hasExtra(ARG_NAME)) {
            textView.setText(String.format("Welcome - %s", getIntent().getStringExtra(ARG_NAME)));
        }
        findViewById(R.id.buttonLogout).setOnClickListener(this);
        findViewById(R.id.buttonDisconnect).setOnClickListener(this);

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        firebaseAuth = FirebaseAuth.getInstance();


        findViewById(R.id.testButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogout:
                signOut();
                break;
            case R.id.buttonDisconnect:
                revokeAccess();
                break;
            case R.id.testButton:
                test();
                break;
        }
    }

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Signed out of google");
                    }
                });

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Revoked Access");
                    }
                });
    }

    private void test(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference refCourse = ref.child("courses");


       /* User user = new User("x864DH1ajDY1vNGwSb84LI11Vtz2", "sadsada@sadak.com", "Furkan Kayar", Arrays.asList(User.Role.TEACHER));
        Course course = new Course("Test", "Test", "Test", "123123", user, new ArrayList<User>(), new ArrayList<User>(), new ArrayList<Assignment>(), new ArrayList<Announcement>());
        User user2 = new User("sgdahdsahdahg", "ahjdshaj@jadjsa.zaa", "Test User", Arrays.asList(User.Role.STUDENT));
        course.enrollTeacher(user);
        course.enrollStudent(user2);
        Assignment assignment = new Assignment("Assignment 1", "Yarın", Arrays.asList(user2), user, "Bugün", "zorunlu ödev crossroads tarzı");
        course.getAssignmentList().add(assignment);

        refCourse.push().setValue(course);*/
    }
}