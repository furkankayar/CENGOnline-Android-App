package com.example.cengonline.ui.home;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;
import com.example.cengonline.model.Course;
import com.example.cengonline.model.User;
import com.example.cengonline.ui.course.CourseFragment;

import org.w3c.dom.Text;

import java.util.List;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayout lineaScrollLayout = (LinearLayout)view.findViewById(R.id.scroll_view_linear_layout);


        final User userUnknown = new User();
        userUnknown.setDisplayName("Unknown User");

        DatabaseUtility.getInstance().getAllCourses(new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                final List<Course> courses = (List<Course>)result;
                lineaScrollLayout.removeAllViews();
                for(final Course course : courses){
                    DatabaseUtility.getInstance().getUserWithKey(course.getCreatedBy(), new DatabaseCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            User user = (User)result;
                            if(user != null){
                                try{
                                    drawCourse(lineaScrollLayout, course, user);
                                }
                                catch(NullPointerException ex){
                                    Log.w(ex.getClass().getName(), ex.getMessage());
                                }

                            }
                            else {
                                try{
                                    drawCourse(lineaScrollLayout, course, userUnknown);
                                }
                                catch (NullPointerException ex){
                                    Log.w(ex.getClass().getName(), ex.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailed(String message) {

                        }
                    });

                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

        return view;
    }

    private void drawCourse(LinearLayout scroll, final Course course, User teacher){

        ImageView imageView = new ImageView(getActivity());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageViewLayoutParams);
        imageView.setImageResource(course.getImageId());

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView courseName = new TextView(getActivity());
        LinearLayout.LayoutParams courseNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseNameLayoutParams.leftMargin = DPtoPX(20);
        courseNameLayoutParams.topMargin = DPtoPX(15);
        courseName.setLayoutParams(courseNameLayoutParams);
        courseName.setLines(1);
        courseName.setEllipsize(TextUtils.TruncateAt.END);
        courseName.setTextAppearance(getActivity(), R.style.fontForCourseNameOnCard);
        courseName.setText(course.getClassName());

        TextView courseSection = new TextView(getActivity());
        LinearLayout.LayoutParams courseSectionLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseSectionLayoutParams.leftMargin = DPtoPX(20);
        courseSection.setLayoutParams(courseNameLayoutParams);
        courseSection.setTextAppearance(getActivity(), R.style.fontForCourseSectionOnCard);
        courseSection.setLines(1);
        courseSection.setEllipsize(TextUtils.TruncateAt.END);
        courseSection.setText(course.getClassSection());

        TextView courseTeacher = new TextView(getActivity());
        LinearLayout.LayoutParams courseTeacherLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseTeacherLayoutParams.leftMargin = DPtoPX(20);
        courseTeacherLayoutParams.bottomMargin = DPtoPX(15);
        courseTeacherLayoutParams.topMargin = DPtoPX(60);
        courseTeacher.setLayoutParams(courseNameLayoutParams);
        courseTeacher.setLines(1);
        courseTeacher.setEllipsize(TextUtils.TruncateAt.END);
        courseTeacher.setTextAppearance(getActivity(), R.style.fontForCourseTeacherOnCard);
        courseTeacher.setText(teacher.getDisplayName());


        linearLayout.addView(courseName, courseNameLayoutParams);
        linearLayout.addView(courseSection, courseSectionLayoutParams);
        linearLayout.addView(courseTeacher, courseTeacherLayoutParams);

        CardView cardView = new CardView(getActivity());
        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.topMargin = DPtoPX(7);
        cardViewLayoutParams.leftMargin = DPtoPX(13);
        cardViewLayoutParams.rightMargin = DPtoPX(13);
        cardView.setLayoutParams(cardViewLayoutParams);
        cardView.setClickable(true);
        cardView.setForeground(getSelectedItemDrawable());
        cardView.setRadius(DPtoPX(8));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CourseFragment.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });


        cardView.addView(imageView, imageViewLayoutParams);
        cardView.addView(linearLayout, linearLayoutLayoutParams);

        scroll.addView(cardView, cardViewLayoutParams);

    }

    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[] { R.attr.selectableItemBackground };
        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }

    public int DPtoPX(int dps){
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, getActivity().getResources().getDisplayMetrics()));
    }
}
