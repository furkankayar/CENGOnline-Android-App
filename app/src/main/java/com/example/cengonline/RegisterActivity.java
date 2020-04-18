package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cengonline.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.redirectLoginText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        final EditText memberEmail = (EditText)findViewById(R.id.registerEmail);
        final EditText memberPassword = (EditText)findViewById(R.id.registerPassword);
        final EditText memberFirstName = (EditText)findViewById(R.id.registerFirstName);
        final EditText memberLastName = (EditText)findViewById(R.id.registerLastName);
        final Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String email = memberEmail.getText().toString();
                final String password = memberPassword.getText().toString();
                final String firstName = memberFirstName.getText().toString();
                final String lastName = memberLastName.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(getApplicationContext(), "Please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lastName)){
                    Toast.makeText(getApplicationContext(), "Please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(getApplicationContext(), "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    runOnUiThread(new Runnable(){
                                        public void run(){
                                            Toast.makeText(RegisterActivity.this, "Authentication Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    FirebaseUser firebaseUser = task.getResult().getUser();
                                    if(firebaseUser != null){
                                        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firstName + " " + lastName, Arrays.asList(User.Role.STUDENT));
                                        saveUserToDatabase(user);
                                        launchMainActivity(user.getDisplayName());
                                        finish();
                                    }
                                    else{

                                    }
                                }
                            }
                        });
            }
        });
    }

    private void launchMainActivity(String displayName){
        MainActivity.startActivity(this, displayName);
    }

    private void launchLoginActivity(){

        startActivity(new Intent(this, LoginActivity.class));
    }

    private void saveUserToDatabase(User user){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseUser = database.child("users");
        databaseUser.push().setValue(user);
    }
}
