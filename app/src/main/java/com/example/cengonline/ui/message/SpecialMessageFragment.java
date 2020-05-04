package com.example.cengonline.ui.message;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cengonline.DatabaseCallback;
import com.example.cengonline.DatabaseUtility;
import com.example.cengonline.R;

import com.example.cengonline.model.Conversation;
import com.example.cengonline.model.Message;
import com.example.cengonline.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class SpecialMessageFragment extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private User sendUser;
    private EditText messageText;
    private FloatingActionButton sendFab;
    private String conversationKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);

        this.toolbar = findViewById(R.id.message_toolbar);
        this.messageText = findViewById(R.id.new_message);
        this.sendFab = findViewById(R.id.send_message_fab);

        if(getIntent() != null && getIntent().getSerializableExtra("sendUser") != null){
            this.sendUser = (User)getIntent().getSerializableExtra("sendUser");
            this.toolbar.setTitle(this.sendUser.getDisplayName());
            setSupportActionBar(this.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            this.sendFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message message = new Message(null, null, messageText.getText().toString());
                    messageText.setText("");
                    DatabaseUtility.getInstance().newMessage(conversationKey, message, new DatabaseCallback() {
                        @Override
                        public void onSuccess(Object result) {

                        }

                        @Override
                        public void onFailed(String message) {
                            makeToastMessage(message);
                        }
                    });
                }
            });

            if(getIntent().getStringExtra("conversationKey") != null){
                this.conversationKey = getIntent().getStringExtra("conversationKey");
                getMessages();
            }
            else{
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("conversations");
                final DatabaseReference newVal = ref.push();
                DatabaseUtility.getInstance().getCurrentUserWithAllInfo(new DatabaseCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        User user = (User) result;
                        Conversation conversation = new Conversation(newVal.getKey(), user, sendUser, new ArrayList<Message>());
                        ref.child(conversation.getKey()).setValue(conversation);
                        conversationKey = conversation.getKey();
                        getMessages();
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            }
        }
        else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {


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
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    private void getMessages(){

        DatabaseUtility.getInstance().getConversationWithKey(conversationKey, new DatabaseCallback() {
            @Override
            public void onSuccess(Object result) {
                Conversation conversation = (Conversation)result;
                makeToastMessage(conversation.getSender().getEmail());
            }

            @Override
            public void onFailed(String message) {

            }
        });
    }

    private void makeToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}