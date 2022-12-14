package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView logoname, register;
    private EditText firstname, middlename, lastname, email, age, password;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        logoname = (TextView) findViewById(R.id.logoname);
        logoname.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        firstname = (EditText) findViewById(R.id.firstname);

        middlename = (EditText) findViewById(R.id.middlename);

        lastname = (EditText) findViewById(R.id.lastname);

        email = (EditText) findViewById(R.id.email);

        age = (EditText) findViewById(R.id.age);

        password = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoname:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                register();
                break;
        }

    }

    public void register() {

        String getFirstname = firstname.getText().toString().trim();
        String getMiddlename = middlename.getText().toString().trim();
        String getLastname = lastname.getText().toString().trim();
        String getEmail = email.getText().toString().trim();
        String getAge = age.getText().toString().trim();
        String getPassword = password.getText().toString().trim();
        String getfullname = getFirstname + getMiddlename + getLastname;

        if (getFirstname.isEmpty()) {
            firstname.setError("This field is required");
            firstname.requestFocus();
            return;
        }
        if (getMiddlename.isEmpty()) {
            middlename.setError("This field is required");
            middlename.requestFocus();
            return;
        }
        if (getLastname.isEmpty()) {
            lastname.setError("This field is required");
            lastname.requestFocus();
            return;
        }
        if (getEmail.isEmpty()) {
            email.setError("This field is empty!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            email.setError("Please set a valid email!");
        }
        if (getPassword.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

       progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(getFirstname, getEmail, getAge);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterUser.this, "registered Successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(RegisterUser.this, "Unsuccessful. Try again", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterUser.this, "Unsuccessful. Try again", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }
}