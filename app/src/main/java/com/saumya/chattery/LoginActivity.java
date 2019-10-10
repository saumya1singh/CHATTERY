package com.saumya.chattery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         etName = findViewById(R.id.etName);
         etPhone = findViewById(R.id.etPhone);
         btnSubmit = findViewById(R.id.btnSubmit);

         Name = etName.getText().toString();
         Phone = etPhone.getText().toString();

         firebaseDatabase = FirebaseDatabase.getInstance("https://chattery-23cb9.firebaseio.com/");
         databaseReference = firebaseDatabase.getReference("");








    }
}
