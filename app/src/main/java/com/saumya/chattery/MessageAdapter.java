package com.saumya.chattery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageModel> {


    public MessageAdapter(Context context, int resource, List<MessageModel> objects) {
        super(context, resource, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_message , parent , false);
        }
        ImageView photoImageView=convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);

         String name = sharedPreferences.getString("Name", "");
         String phone = sharedPreferences.getString("Phone ", "");
         FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://chattery-23cb9.firebaseio.com/");
         DatabaseReference databaseReference = firebaseDatabase.getReference("Chatter/" + name + "/Friends");

            MessageModel message = getItem(position);


        boolean isPhoto=message.getPhotoUrl() != null;
        if (isPhoto){
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        }else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(message.getName());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("Friends snap ", "onDataChange: " + dataSnapshot );

                for( DataSnapshot ds : dataSnapshot.getChildren()){


                    Log.e("Friend child snap", "onDataChange: " + ds);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
