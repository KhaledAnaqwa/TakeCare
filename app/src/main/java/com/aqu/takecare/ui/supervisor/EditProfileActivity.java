package com.aqu.takecare.ui.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aqu.takecare.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private EditText mFullName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mCity;
    private EditText mAge;
    private Button msaveProfileInfoBtn;
    private ProgressBar progressBar;
    private String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFullName = binding.profileFullName;
        mEmail = binding.profileEmailAddress;
        mPhone = binding.profilePhoneNo;
        mCity = binding.profileCity;
        mAge = binding.profileAge;
        msaveProfileInfoBtn = binding.saveProfileInfo;
        progressBar = binding.progressBar;


        DocumentReference documentReference = fStore.collection("users").document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot user = task.getResult();
                mFullName.setText((String) user.get("fName"));
                mEmail.setText((String) user.get("email"));
                mAge.setText((String) user.get("age"));
                mCity.setText((String) user.get("city"));
                mPhone.setText((String) user.get("phone"));
            }
        });

        msaveProfileInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone = mPhone.getText().toString();
                final String age = mAge.getText().toString();
                final String city = mCity.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                DocumentReference documentReference = fStore.collection("users").document(mAuth.getCurrentUser().getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("fName", fullName);
                user.put("email", email);
                user.put("age", age);
                user.put("city", city);
                user.put("phone", phone);
                user.put("supervisor", true);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onSuccess: user Profile is updated for " + mAuth.getCurrentUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                startActivity(new Intent(getApplicationContext(), SupervisorActivity.class));

            }
        });
    }
}