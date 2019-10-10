package com.saumya.chattery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeChattingActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";

    //Intent intent = getIntent().getStringExtra();
    Bundle bundle = new Bundle();
    String friendname = bundle.getString("FriendName");
    String friendphone = bundle.getString("FriendPhone");

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private ListView messageListView;
    private MessageAdapter messageAdapter;


    private ImageButton photoPickerButton;
    private EditText messageEditText;
    private Button sendButton;

    private String mUsername;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    String name, phone;
    private ChildEventListener childEventListener;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chatting);

        mUsername = ANONYMOUS;

        messageListView = (ListView) findViewById(R.id.messageListView);
        photoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name","");
        phone = sharedPreferences.getString("Phone ","");
        firebaseDatabase = FirebaseDatabase.getInstance("https://chattery-23cb9.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Chatter/" + "Friends");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        List<MessageModel> messageModels = new ArrayList<>();

        messageAdapter = new MessageAdapter(getBaseContext() , R.layout.item_message , messageModels);
        messageListView.setAdapter(messageAdapter);


        photoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });





        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length()> 0){
                    sendButton.setEnabled(true);
                }else{
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 MessageModel messageModel = new MessageModel(messageEditText.getText().toString() , mUsername , null);
                 //databaseReference.push().setValue(messageModel);

                 databaseReference.child(friendname).child("Message").push().setValue(messageEditText.getText().toString());
                 databaseReference.child(friendname).child("Phone").setValue(phone);
                // Clear input box
                messageEditText.setText("");
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                            // Set the download URL to the message box, so that the user can send it to the database
                           MessageModel messageModel = new MessageModel(null, mUsername, downloadUrl.toString());
                           databaseReference.child(friendname).child("Images").push().setValue(downloadUrl);
                        }

                    });
        }
    }


    private void onSignedInInitialize(String username){
        mUsername=username;
        attachDataBaseListener();
    }



    private void onSignedOutCleanUp(){
        mUsername=ANONYMOUS;
        messageAdapter.clear();
        detachDatabaseListener();
    }



    private void attachDataBaseListener(){

        if (childEventListener==null){
            childEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                    messageAdapter.add(messageModel);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }



    private void detachDatabaseListener(){
        if (childEventListener!=null){
            databaseReference.removeEventListener(childEventListener);
            childEventListener=null;
        }
    }


}
