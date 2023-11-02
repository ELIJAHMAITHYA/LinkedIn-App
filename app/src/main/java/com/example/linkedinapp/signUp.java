package com.example.linkedinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signUpname, signUpEmail, signUpPhone, sgnUpGender, signUpDescription, signUpSkill, signUpPassword, signUpConfPass;
    private ImageView signUpimage;
    private Button signUpButton;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpButton = findViewById(R.id.signup_button);
        signUpimage = findViewById(R.id.signUp_image);
        signUpname = findViewById(R.id.signup_name);
        signUpEmail = findViewById(R.id.signup_Email);
        signUpPhone = findViewById(R.id.signup_Phonenumber);
        sgnUpGender = findViewById(R.id.signup_gender);
        signUpDescription = findViewById(R.id.signUp_Description);
        signUpSkill = findViewById(R.id.signUp_skills);
        signUpPassword = findViewById(R.id.signup_Password);
        signUpConfPass = findViewById(R.id.signup_Confirm_Password);
        loginText = findViewById(R.id.login_text);
        auth = FirebaseAuth.getInstance();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    signUpEmail.setError("Email cannot be empty");
                }
                if (password.isEmpty()) {
                    signUpPassword.setError("Password cannot be empty");
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(signUp.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(signUp.this, login.class));
                            } else {
                                Toast.makeText(signUp.this, "SignUp unsuccessful" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUp.this, login.class));
            }
        });
    }
}