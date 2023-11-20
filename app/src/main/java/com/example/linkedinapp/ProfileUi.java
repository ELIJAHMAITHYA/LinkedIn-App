package com.example.linkedinapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class ProfileUi extends AppCompatActivity {
    Button logout, uploadPhoto;
    ImageView profileImage;
    private Uri Photopath;
    TextView username, phoneNumber, gender, description, skills;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);
        logout = findViewById(R.id.logoutButton);
        uploadPhoto = findViewById(R.id.uploadPhotoButton);
        profileImage = findViewById(R.id.profile_Image);
        username = findViewById(R.id.profile_Username);
        phoneNumber = findViewById(R.id.profile_phoneNumber);
        gender = findViewById(R.id.profile_gender);
        skills = findViewById(R.id.profile_skills);
        description = findViewById(R.id.profile_Description);

        getUserInformation();

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }


        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileUi.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });

    }

    private void getUserInformation() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user").child(uid);
        userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        // Retrieve user information from the dataSnapshot
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        String userPhoneNumber = dataSnapshot.child("phone_number").getValue(String.class);
                        String userGender = dataSnapshot.child("gender").getValue(String.class);
                        String userSkills = dataSnapshot.child("skills").getValue(String.class);
                        String userDescription = dataSnapshot.child("description").getValue(String.class);

                        // Set the retrieved information to the corresponding TextViews
                        username.setText(userName);
                        phoneNumber.setText(userPhoneNumber);
                        gender.setText(userGender);
                        skills.setText(userSkills);
                        description.setText(userDescription);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Photopath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Photopath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        profileImage.setImageBitmap(bitmap);
    }

    private void uploadPhoto() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading....");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference("image/*" + UUID.randomUUID().toString()).putFile(Photopath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                upDateUserProfilePicture(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(ProfileUi.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileUi.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });
    }

    private void upDateUserProfilePicture(String uri) {
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/image").setValue(uri);
    }

}