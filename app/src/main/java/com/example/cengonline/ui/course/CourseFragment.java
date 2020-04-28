package com.example.cengonline.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;
import com.example.cengonline.Utility;
import com.example.cengonline.model.Course;
import com.example.cengonline.model.CourseAnnouncements;
import com.example.cengonline.model.User;
import com.example.cengonline.post.Announcement;
import com.example.cengonline.ui.dialog.NewAnnouncementDialog;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;


public class CourseFragment extends AppCompatActivity implements View.OnClickListener, DatabaseCallback {

    private final int DELETE_ITEM = 1000;
    private TextView courseName;
    private TextView courseSection;
    private TextView courseCode;
    private ImageView courseImage;
    private Course course;
    private Toolbar toolbar;
    private TextView shareImageText;
    private CardView shareCard;
    private LinearLayout scrollLinearLayout;
    private List<CardView> announcementCards;
    private List<User> users;
    private List<Announcement> announcements;
    private List<CardModel> cardModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course);
        this.announcementCards = new ArrayList<CardView>();
        this.users = new ArrayList<User>();
        this.cardModels = new ArrayList<CardModel>();
        this.announcements = new ArrayList<Announcement>();
        this.courseName = findViewById(R.id.course_fragment_course_name);
        this.courseSection = findViewById(R.id.course_fragment_course_section);
        this.courseImage = findViewById(R.id.course_fragment_course_image);
        this.courseCode = findViewById(R.id.course_fragment_course_code);
        this.toolbar = findViewById(R.id.course_toolbar);
        this.shareImageText = findViewById(R.id.share_image_text);
        this.shareCard = findViewById(R.id.share_card);
        this.scrollLinearLayout = findViewById(R.id.course_fragment_scroll_linear_layout);

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
                        shareImageText.setText(user.getDisplayName().toUpperCase().substring(0, 1));
                        shareCard.setVisibility(View.VISIBLE);
                    }
                    else{
                        scrollLinearLayout.removeView(shareCard);
                    }
                }

                @Override
                public void onFailed(String message) {

                }
            });

            this.shareCard.setOnClickListener(this);
            DatabaseUtility.getInstance().getCourseAnnouncements(this.course, this);

        }
        else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        NewAnnouncementDialog newAD = new NewAnnouncementDialog(this, this.course);
        newAD.show();
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
        dateText.setText(cardModel.getAnnouncement().getPostedAt().toString());


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
        bodyText.setText(cardModel.getAnnouncement().getBody());


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
                goToAnnouncement(cardModel.getUser(), cardModel.getAnnouncement());
            }
        });

        cardView.addView(outerLinearLayout, outerLinearLayoutLayoutParams);

        this.announcementCards.add(cardView);
        scrollLinearLayout.addView(cardView, cardViewLayoutParams);
    }

    private void goToAnnouncement(User user, Announcement announcement){
        Intent intent = new Intent(this, AnnouncementFragment.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        intent.putExtra("announcement", announcement);
        startActivity(intent);
    }

    private void printCardModels(){

        Collections.sort(cardModels);

        for(CardModel cm : this.cardModels){
            createAnnouncementCard(cm);
        }
    }

    //DATABASE CALLBACK
    @Override
    public void onSuccess(Object result) {
        CourseAnnouncements ca = (CourseAnnouncements)result;

        this.cardModels = new ArrayList<CardModel>();

        for(final Announcement an : ca.getAnnouncements()){

            DatabaseUtility.getInstance().getUserWithKey(an.getPostedBy(), new DatabaseCallback() {
                @Override
                public void onSuccess(Object result) {
                    User user = (User)result;
                    for(CardView cv : announcementCards){
                        scrollLinearLayout.removeView(cv);
                    }
                    cardModels.add(new CardModel(user, an));
                    printCardModels();
                }

                @Override
                public void onFailed(String message) {

                }
            });
        }


    }

    @Override
    public void onFailed(String message) {
        makeToastMessage(message);
    }


    private class CardModel implements Comparable{
        private Announcement announcement;
        private User user;

        public CardModel(User user, Announcement announcement){
            this.user = user;
            this.announcement = announcement;
        }

        public Announcement getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(Announcement announcement) {
            this.announcement = announcement;
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
            return this.getAnnouncement().getPostedAt().compareTo(o2.getAnnouncement().getPostedAt()) * -1;
        }
    }
}
