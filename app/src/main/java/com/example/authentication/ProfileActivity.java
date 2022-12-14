package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    private Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        signout = (Button) findViewById(R.id.logout);
        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }

        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        Log.i("userid", userId);

        final TextView firstnameTextView = (TextView) findViewById(R.id.firstname);
        final TextView emailTextView = (TextView) findViewById(R.id.email);
        final TextView ageTextView = (TextView) findViewById(R.id.age);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){

                    Log.d("User profile", "worked");

                    String firstname = userProfile.getFirstname;
                    String email = userProfile.getEmail;
                    String age = userProfile.getAge;

                    firstnameTextView.setText(firstname);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                }
                else {
                    Log.d("Error", "not working");
                    Toast.makeText(ProfileActivity.this, "Something is wrong with your code",  Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong. Try again", Toast.LENGTH_LONG).show();

            }
        });
    }

}