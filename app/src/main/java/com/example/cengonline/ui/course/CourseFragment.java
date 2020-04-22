package com.example.cengonline.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;
import com.example.cengonline.model.Course;
import com.example.cengonline.model.User;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class CourseFragment extends AppCompatActivity {

    private final int DELETE_ITEM = 1000;
    private TextView courseName;
    private TextView courseSection;
    private TextView courseCode;
    private ImageView courseImage;
    private Course course;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course);

        this.courseName = findViewById(R.id.course_fragment_course_name);
        this.courseSection = findViewById(R.id.course_fragment_course_section);
        this.courseImage = findViewById(R.id.course_fragment_course_image);
        this.courseCode = findViewById(R.id.course_fragment_course_code);
        this.toolbar = findViewById(R.id.course_toolbar);

        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getIntent() != null && getIntent().getSerializableExtra("course") != null){
            this.course = (Course)getIntent().getSerializableExtra("course");

            this.courseName.setText(this.course.getClassName());
            this.courseSection.setText(this.course.getClassSection());
            this.courseCode.setText("Class code: " + this.course.getClassCode());
            this.courseImage.setImageResource(this.course.getImageId());
            this.toolbar.setTitle(this.course.getClassName());

        }
        else{
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.course_options_menu, menu);
        setMenuItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch(id){
            case R.id.course_options_unenroll:
                unenrollCourse();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case DELETE_ITEM:
                removeCourse();
                break;
        }
        return true;
    }

    private void setMenuItems(final Menu menu){

        DatabaseUtility.getInstance().getCurrentUserWithAllInfo(new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                User user = (User)result;
                if(course.getCreatedBy().equals(user.getKey())){
                    menu.add(0, DELETE_ITEM, 0, "Delete Course");
                    menu.removeItem(R.id.course_options_unenroll);
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

    }

    private void unenrollCourse(){

        DatabaseUtility.getInstance().removeStudentFromCourse(this.course.getClassCode(), new DatabaseCallback(){
            @Override
            public void onSuccess(Object result) {
                String msg = (String) result;
                makeToastMessage(msg);
                onBackPressed();
            }

            @Override
            public void onFailed(String message) {
                makeToastMessage(message);
                onBackPressed();
            }
        });
    }

    private void removeCourse(){

        DatabaseUtility.getInstance().removeCourse(this.course.getClassCode(), new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                String msg = (String) result;
                makeToastMessage(msg);
                onBackPressed();
            }

            @Override
            public void onFailed(String message) {
                makeToastMessage(message);
                onBackPressed();
            }
        });
    }

    private void makeToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
