package com.example.cengonline.ui.course;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;
import com.example.cengonline.model.Course;
import com.example.cengonline.model.User;
import com.example.cengonline.post.Assignment;


public class AssignmentFragment extends AppCompatActivity implements View.OnClickListener {

    private final int DELETE_ITEM = 1000;
    private final int EDIT_ITEM = 1001;

    private Toolbar toolbar;
    private User user;
    private Course course;
    private Assignment assignment;
    private TextView assignmentTitle;
    private TextView assignmentDueDate;
    private TextView assignmentDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_assignment);

        this.toolbar = findViewById(R.id.course_toolbar);
        this.assignmentTitle = findViewById(R.id.assignment_title);
        this.assignmentDueDate = findViewById(R.id.assignment_due_date);
        this.assignmentDescription = findViewById(R.id.assignment_description);
        this.assignmentDescription.setFocusable(false);

        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getIntent() != null && getIntent().getSerializableExtra("user") != null && getIntent().getSerializableExtra("assignment") != null && getIntent().getSerializableExtra("course") != null){

            this.user = (User)getIntent().getSerializableExtra("user");
            this.course = (Course)getIntent().getSerializableExtra("course");
            this.assignment = (Assignment) getIntent().getSerializableExtra("assignment");

            this.assignmentTitle.setText(this.assignment.getTitle());
            this.assignmentDescription.setText(this.assignment.getBody());
            this.assignmentDueDate.setText(this.assignment.getDueDate().toStringAssignmentDue());


            DatabaseUtility.getInstance().getCurrentUserWithAllInfo(new DatabaseCallback() {
                @Override
                public void onSuccess(Object result) {
                    User user = (User)result;

                }

                @Override
                public void onFailed(String message) {

                }
            });


        }
        else{
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        /*NewAnnouncementDialog newAD = new NewAnnouncementDialog(this, this.course);
        newAD.show();*/
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

        setMenuItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                onBackPressed();
                break;
            case EDIT_ITEM:
                showEditAnnouncementDialog();
                break;
            case DELETE_ITEM:
                deleteAnnouncement();
                break;
        }
        return true;
    }

    private void deleteAnnouncement(){
       /* DatabaseUtility.getInstance().deleteCourseAnnouncement(course, announcement, new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                String message = (String)result;
                makeToastMessage(message);
                finish();
            }

            @Override
            public void onFailed(String message) {
                makeToastMessage(message);
            }
        });*/
    }

    private void setMenuItems(final Menu menu){

        DatabaseUtility.getInstance().getCurrentUserWithAllInfo(new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                User user = (User)result;
                if(assignment.getPostedBy().equals(user.getKey())){
                    menu.add(0, EDIT_ITEM, 0, "Edit");
                    menu.add(0, DELETE_ITEM, 1, "Delete");
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });
    }

    private void showEditAnnouncementDialog(){
       /* EditAnnouncementDialog eaD = new EditAnnouncementDialog(this, this.course, this.assignment);
        eaD.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                recreate();
            }
        });
        eaD.show();*/
    }



    private void makeToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }






}
