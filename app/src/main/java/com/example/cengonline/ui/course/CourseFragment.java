package com.example.cengonline.ui.course;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;
import com.example.cengonline.Utility;
import com.example.cengonline.model.Course;
import com.example.cengonline.model.CourseAnnouncements;
import com.example.cengonline.model.CourseAssignments;
import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;
import com.example.cengonline.post.Announcement;
import com.example.cengonline.post.Assignment;
import com.example.cengonline.ui.dialog.NewAnnouncementDialog;
import com.example.cengonline.ui.dialog.NewAssignmentDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;


public class CourseFragment extends AppCompatActivity implements View.OnClickListener, DatabaseCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    private final int DELETE_ITEM = 1000;
    private TextView courseName;
    private TextView courseSection;
    private TextView courseCode;
    private ImageView courseImage;
    private Course course;
    private Toolbar toolbar;
    private TextView shareAnnouncementImageText;
    private TextView shareAssignmentImageText;
    private CardView shareAnnouncementCard;
    private CardView shareAssignmentCard;
    private LinearLayout scrollLinearLayout;
    private List<CardView> announcementCards;
    private List<CardView> assignmentCards;
    private List<CardModel> announcementCardModels;
    private List<CardModel> assignmentCardModels;
    private BottomNavigationView navigationView;
    private LinearLayout announcementsLinearLayout;
    private LinearLayout assignmentsLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course);
        this.announcementCards = new ArrayList<CardView>();
        this.assignmentCards = new ArrayList<CardView>();
        this.announcementCardModels = new ArrayList<CardModel>();
        this.assignmentCardModels = new ArrayList<CardModel>();
        this.courseName = findViewById(R.id.course_fragment_course_name);
        this.courseSection = findViewById(R.id.course_fragment_course_section);
        this.courseImage = findViewById(R.id.course_fragment_course_image);
        this.courseCode = findViewById(R.id.course_fragment_course_code);
        this.toolbar = findViewById(R.id.course_toolbar);
        this.shareAnnouncementImageText = findViewById(R.id.share_announcement_image_text);
        this.shareAssignmentImageText = findViewById(R.id.share_assignment_image_text);
        this.shareAnnouncementCard = findViewById(R.id.share_announcement_card);
        this.shareAssignmentCard = findViewById(R.id.share_assignment_card);
        this.scrollLinearLayout = findViewById(R.id.course_fragment_scroll_linear_layout);
        this.navigationView = findViewById(R.id.bottom_navigation);
        this.announcementsLinearLayout = findViewById(R.id.announcements_linear_layout);
        this.assignmentsLinearLayout = findViewById(R.id.assignments_linear_layout);

        this.navigationView.setOnNavigationItemSelectedListener(this);

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


            DatabaseUtility.getInstance().getCurrentUserWithAllInfo(new DatabaseCallback() {
                @Override
                public void onSuccess(Object result) {
                    User user = (User)result;
                    if(course.getTeacherList() != null && course.getTeacherList().contains(user.getKey())){
                        shareAnnouncementImageText.setText(user.getDisplayName().toUpperCase().substring(0, 1));
                        shareAssignmentImageText.setText(user.getDisplayName().toUpperCase().substring(0, 1));
                        shareAnnouncementCard.setVisibility(View.VISIBLE);
                        shareAssignmentCard.setVisibility(View.VISIBLE);
                    }
                    else{
                        announcementsLinearLayout.removeView(shareAnnouncementCard);
                        assignmentsLinearLayout.removeView(shareAssignmentCard);
                    }
                }

                @Override
                public void onFailed(String message) {

                }
            });

            this.shareAnnouncementCard.setOnClickListener(this);
            this.shareAssignmentCard.setOnClickListener(this);
            DatabaseUtility.getInstance().getCourseAnnouncements(this.course, this);
            DatabaseUtility.getInstance().getCourseAssignments(this.course, this);

        }
        else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.share_announcement_card:
                NewAnnouncementDialog newAnD = new NewAnnouncementDialog(this, this.course);
                newAnD.show();
                break;
            case R.id.share_assignment_card:
                NewAssignmentDialog newAsD = new NewAssignmentDialog(this, this.course);
                newAsD.show();
                break;
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

        DatabaseUtility.getInstance().removeStudentFromCourse(this.course, new DatabaseCallback(){
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

        DatabaseUtility.getInstance().removeCourse(this.course, new DatabaseCallback() {
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

    private void createAnnouncementCard(final CardModel cardModel){

        Utility util = Utility.getInstance();

        TextView imageText = new TextView(this);
        LinearLayout.LayoutParams imageTextLayoutParams = new LinearLayout.LayoutParams(util.DPtoPX(40, this), util.DPtoPX(40, this));
        imageText.setLayoutParams(imageTextLayoutParams);
        imageText.setTextAppearance(this, R.style.fontForImageTextOnCard);
        imageText.setBackgroundResource(R.drawable.rounded_textview);
        imageText.setText(String.valueOf(cardModel.getUser().getDisplayName().toUpperCase().charAt(0)));
        imageText.setGravity(Gravity.CENTER);

        LinearLayout insideLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams insideLinearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        insideLinearLayout.setLayoutParams(insideLinearLayoutLayoutParams);
        insideLinearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView displayNameText = new TextView(this);
        LinearLayout.LayoutParams displayNameTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        displayNameTextLayoutParams.leftMargin = util.DPtoPX(20, this);
        displayNameText.setLayoutParams(displayNameTextLayoutParams);
        displayNameText.setTextAppearance(this, R.style.fontForDisplayNameOnCard);
        displayNameText.setText(cardModel.getUser().getDisplayName());

        TextView dateText = new TextView(this);
        LinearLayout.LayoutParams dateTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateTextLayoutParams.leftMargin = util.DPtoPX(20, this);
        dateText.setLayoutParams(dateTextLayoutParams);
        dateText.setTextAppearance(this, R.style.fontForDateOnCard);
        dateText.setText(cardModel.getPost().getPostedAt().toString());


        LinearLayout middleLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams middleLinarLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        middleLinarLayoutLayoutParams.topMargin = util.DPtoPX(10, this);
        middleLinarLayoutLayoutParams.bottomMargin = util.DPtoPX(10, this);
        middleLinarLayoutLayoutParams.leftMargin = util.DPtoPX(15, this);
        middleLinearLayout.setLayoutParams(middleLinarLayoutLayoutParams);
        middleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        middleLinearLayout.setGravity(Gravity.CENTER);

        TextView bodyText = new TextView(this);
        LinearLayout.LayoutParams bodyTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bodyTextLayoutParams.leftMargin = util.DPtoPX(15, this);
        bodyTextLayoutParams.rightMargin = util.DPtoPX(15, this);
        bodyTextLayoutParams.topMargin = util.DPtoPX(6, this);
        bodyTextLayoutParams.bottomMargin = util.DPtoPX(10, this);
        bodyText.setTextAppearance(this, R.style.fontForBodyTextOnCard);
        bodyText.setLines(3);
        bodyText.setEllipsize(TextUtils.TruncateAt.END);
        bodyText.setText(cardModel.getPost().getBody());


        LinearLayout outerLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams outerLinearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        outerLinearLayout.setLayoutParams(outerLinearLayoutLayoutParams);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);


        insideLinearLayout.addView(displayNameText, displayNameTextLayoutParams);
        insideLinearLayout.addView(dateText, dateTextLayoutParams);
        middleLinearLayout.addView(imageText, imageTextLayoutParams);
        middleLinearLayout.addView(insideLinearLayout, insideLinearLayoutLayoutParams);
        outerLinearLayout.addView(middleLinearLayout, middleLinarLayoutLayoutParams);
        outerLinearLayout.addView(bodyText, bodyTextLayoutParams);

        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.bottomMargin = util.DPtoPX(7, this);
        cardViewLayoutParams.leftMargin = util.DPtoPX(8, this);
        cardViewLayoutParams.rightMargin = util.DPtoPX(8, this);
        cardView.setLayoutParams(cardViewLayoutParams);
        cardView.setClickable(true);
        cardView.setRadius(util.DPtoPX(8, this));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAnnouncement(cardModel.getUser(), (Announcement) cardModel.getPost());
            }
        });

        cardView.addView(outerLinearLayout, outerLinearLayoutLayoutParams);

        this.announcementCards.add(cardView);
        this.announcementsLinearLayout.addView(cardView, cardViewLayoutParams);
    }

    private void createAssignmentCard(final CardModel cardModel){
        Utility util = Utility.getInstance();

        TextView imageText = new TextView(this);
        LinearLayout.LayoutParams imageTextLayoutParams = new LinearLayout.LayoutParams(util.DPtoPX(40, this), util.DPtoPX(40, this));
        imageText.setLayoutParams(imageTextLayoutParams);
        imageText.setBackgroundResource(R.drawable.assignment);
        imageText.setGravity(Gravity.CENTER);

        LinearLayout insideLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams insideLinearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        insideLinearLayout.setLayoutParams(insideLinearLayoutLayoutParams);
        insideLinearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView displayNameText = new TextView(this);
        LinearLayout.LayoutParams displayNameTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        displayNameTextLayoutParams.leftMargin = util.DPtoPX(20, this);
        displayNameText.setLayoutParams(displayNameTextLayoutParams);
        displayNameText.setTextAppearance(this, R.style.fontForDisplayNameOnCard);
        displayNameText.setText(cardModel.getUser().getDisplayName() + " posted a new assignment: " + ((Assignment)cardModel.getPost()).getTitle());

        TextView dateText = new TextView(this);
        LinearLayout.LayoutParams dateTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateTextLayoutParams.leftMargin = util.DPtoPX(20, this);
        dateText.setLayoutParams(dateTextLayoutParams);
        dateText.setTextAppearance(this, R.style.fontForDateOnCard);
        dateText.setText(cardModel.getPost().getPostedAt().toString());


        LinearLayout middleLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams middleLinarLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        middleLinarLayoutLayoutParams.topMargin = util.DPtoPX(10, this);
        middleLinarLayoutLayoutParams.bottomMargin = util.DPtoPX(10, this);
        middleLinarLayoutLayoutParams.leftMargin = util.DPtoPX(15, this);
        middleLinearLayout.setLayoutParams(middleLinarLayoutLayoutParams);
        middleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        middleLinearLayout.setGravity(Gravity.CENTER);


        LinearLayout outerLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams outerLinearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        outerLinearLayout.setLayoutParams(outerLinearLayoutLayoutParams);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);


        insideLinearLayout.addView(displayNameText, displayNameTextLayoutParams);
        insideLinearLayout.addView(dateText, dateTextLayoutParams);
        middleLinearLayout.addView(imageText, imageTextLayoutParams);
        middleLinearLayout.addView(insideLinearLayout, insideLinearLayoutLayoutParams);
        outerLinearLayout.addView(middleLinearLayout, middleLinarLayoutLayoutParams);

        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.bottomMargin = util.DPtoPX(7, this);
        cardViewLayoutParams.leftMargin = util.DPtoPX(8, this);
        cardViewLayoutParams.rightMargin = util.DPtoPX(8, this);
        cardView.setLayoutParams(cardViewLayoutParams);
        cardView.setClickable(true);
        cardView.setRadius(util.DPtoPX(8, this));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAssignment(cardModel.getUser(), (Assignment) cardModel.getPost());
            }
        });

        cardView.addView(outerLinearLayout, outerLinearLayoutLayoutParams);

        this.assignmentCards.add(cardView);
        this.assignmentsLinearLayout.addView(cardView, cardViewLayoutParams);
    }

    private void goToAnnouncement(User user, Announcement announcement){
        Intent intent = new Intent(this, AnnouncementFragment.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        intent.putExtra("announcement", announcement);
        startActivity(intent);

    }

    private void goToAssignment(User user, Assignment assignment){
        Intent intent = new Intent(this, AssignmentFragment.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        intent.putExtra("assignment", assignment);
        startActivity(intent);
    }

    private void printAnnouncementCardModels(){

        Collections.sort(announcementCardModels);

        for(CardModel cm : this.announcementCardModels){
            createAnnouncementCard(cm);
        }
    }

    private void printAssignmentCardModels(){

        Collections.sort(assignmentCardModels);

        for(CardModel cm : this.assignmentCardModels){
            createAssignmentCard(cm);
        }
    }

    //DATABASE CALLBACK
    @Override
    public void onSuccess(Object result) {

        if(result.getClass() == CourseAnnouncements.class){
            CourseAnnouncements ca = (CourseAnnouncements)result;

            this.announcementCardModels = new ArrayList<CardModel>();

            for(final Announcement an : ca.getAnnouncements()){

                DatabaseUtility.getInstance().getUserWithKey(an.getPostedBy(), new DatabaseCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        User user = (User)result;
                        for(CardView cv : announcementCards){
                            announcementsLinearLayout.removeView(cv);
                        }
                        announcementCardModels.add(new CardModel(user, an));
                        printAnnouncementCardModels();
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            }
        }
        else if(result.getClass() == CourseAssignments.class){
            CourseAssignments ca = (CourseAssignments)result;

            this.assignmentCardModels = new ArrayList<CardModel>();

            for(final Assignment as : ca.getAssignments()){

                DatabaseUtility.getInstance().getUserWithKey(as.getPostedBy(), new DatabaseCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        User user = (User)result;
                        for(CardView cv : assignmentCards){
                            assignmentsLinearLayout.removeView(cv);
                        }
                        assignmentCardModels.add(new CardModel(user, as));
                        printAssignmentCardModels();
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            }
        }
        else if(result.getClass() == java.lang.String.class && ((String)result).equals("Announcements empty")){
            for(CardView cv : announcementCards){
                announcementsLinearLayout.removeView(cv);
            }
            announcementCardModels = new ArrayList<CardModel>();
            printAnnouncementCardModels();
        }
        else if(result.getClass() == java.lang.String.class && ((String)result).equals("Assignments empty")){
            for(CardView cv : assignmentCards){
                assignmentsLinearLayout.removeView(cv);
            }
            assignmentCardModels =  new ArrayList<CardModel>();
            printAssignmentCardModels();
        }
    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        item.setChecked(true);

        switch (itemId){
            case R.id.navigation_announcements:
                this.assignmentsLinearLayout.setVisibility(View.GONE);
                this.announcementsLinearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_assignments:
                this.assignmentsLinearLayout.setVisibility(View.VISIBLE);
                this.announcementsLinearLayout.setVisibility(View.GONE);
                break;
            default: break;
        }

        return false;
    }


    private class CardModel implements Comparable{
        private AbstractPost post;
        private User user;

        public CardModel(User user, AbstractPost post){
            this.user = user;
            this.post = post;
        }

        public AbstractPost getPost() {
            return post;
        }

        public void setAnnouncement(AbstractPost post) {
            this.post = post;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public int compareTo(Object o) {
            CardModel o2 = (CardModel)o;
            return this.getPost().getPostedAt().compareTo(o2.getPost().getPostedAt()) * -1;
        }
    }
}
