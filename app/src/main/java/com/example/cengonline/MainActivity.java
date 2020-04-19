package com.example.cengonline;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cengonline.model.User;
import com.example.cengonline.ui.dialog.JoinClassDialog;
import com.example.cengonline.ui.dialog.NewClassDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private User user;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        firebaseAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("user")){
            this.user = (User)getIntent().getSerializableExtra("user");
        }


        if(this.user == null){
            readUserFromDatabase();
        }
        else{
            setAttributes();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.action_logout:
                signOut();
                return true;
            case R.id.join_class:
                showJoinClassDialog();
                return true;
            case R.id.new_class:
                showNewClassDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        if(!this.user.getRoles().contains(User.Role.TEACHER)){
            invalidateOptionsMenu();
            menu.findItem(R.id.new_class).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void readUserFromDatabase(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            DatabaseReference userRef = ref.child(("users"));

            Query query = userRef.orderByChild("uid").equalTo(firebaseUser.getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        user = ds.getValue(User.class);
                    }
                    setAttributes();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void setAttributes(){
        View header = navigationView.getHeaderView(0);
        ((TextView)header.findViewById(R.id.navbarEmail)).setText(user.getEmail());
        ((TextView)header.findViewById(R.id.navbarDisplayName)).setText(user.getDisplayName().toUpperCase());
        ((TextView)header.findViewById(R.id.navbarEmailImage)).setText(String.valueOf(user.getDisplayName().toUpperCase().charAt(0)));
    }

    private void showJoinClassDialog(){
        JoinClassDialog joinD = new JoinClassDialog(this);
        joinD.show();
    }

    private void showNewClassDialog(){
        NewClassDialog newD = new NewClassDialog(this);
        newD.show();
    }

    public static void startActivity(Context context, User user) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

}
