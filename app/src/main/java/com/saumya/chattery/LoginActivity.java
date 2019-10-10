package com.saumya.chattery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText etName,etPhone;
    Button btnSubmit;
    String Name, Phone;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         etName = findViewById(R.id.etName);
         etPhone = findViewById(R.id.etPhone);
         btnSubmit = findViewById(R.id.btnSubmit);

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

         firebaseDatabase = FirebaseDatabase.getInstance("https://chattery-23cb9.firebaseio.com/");
         databaseReference = firebaseDatabase.getReference("Chatter/"  );

         btnSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Name = etName.getText().toString();
                 Phone = etPhone.getText().toString();
                 if (chekvalidation()){

                     Name = etName.getText().toString();
                     Phone = etPhone.getText().toString();
                     Log.e("LOG0", "onClick: " + Name  );
                    // databaseReference.child("Name").setValue(Name);
                     databaseReference.child(Name).child("Phone").setValue(Phone);


                     Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);
                     startActivity(intent);
                     finish();
                 }


                 SharedPreferences.Editor editor = sharedPreferences.edit();

                 editor.putString("Phone", Phone);
                 editor.putString("Name", Name);
                 editor.apply();

             }
         });


    }

    public  boolean chekvalidation() {
        boolean check = true;

        if (etName.getText().toString().isEmpty()) {
            etName.setError("Required");
            check = false;
        }
        if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError("Required");
            check = false;
        }
        if(etPhone.getText().toString().length() != 10)
        {
            etPhone.setError("Enter Correct");
            check = false;
        }
        return check;
    }
}
