package com.aqu.takecare.ui.patient;

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

import com.aqu.takecare.databinding.ActivityEditPatientProfileBinding;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPatientProfileActivity extends AppCompatActivity {

    private ActivityEditPatientProfileBinding binding;
    private FirebaseFirestore fStore;
    private EditText mFullName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mCity;
    private EditText mAge;
    private Button msaveProfileInfoBtn;
    private ProgressBar progressBar;
    private String TAG = "EditProfileActivity";
    private String patientUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();

        binding = ActivityEditPatientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFullName = binding.profileFullName;
        mEmail = binding.profileEmailAddress;
        mPhone = binding.profilePhoneNo;
        mCity = binding.profileCity;
        mAge = binding.profileAge;
        msaveProfileInfoBtn = binding.saveProfileInfo;
        progressBar = binding.progressBar;

        patientUID = (String) getIntent().getExtras().get(SupervisorActivity.PatientUID);

        DocumentReference documentReference = fStore.collection("users").document(patientUID);
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

                DocumentReference documentReference = fStore.collection("users").document(patientUID);
                Map<String, Object> user = new HashMap<>();
                user.put("fName", fullName);
                user.put("email", email);
                user.put("age", age);
                user.put("city", city);
                user.put("phone", phone);


                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onSuccess: User Profile is updated for " + patientUID);
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