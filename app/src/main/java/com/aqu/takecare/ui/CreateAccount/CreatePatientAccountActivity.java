package com.aqu.takecare.ui.CreateAccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aqu.takecare.databinding.ActivityCreatePatientAccountBinding;
import com.aqu.takecare.ui.login.LoginActivity;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreatePatientAccountActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mCreateAccountActivityBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String parentUID;
    private ActivityCreatePatientAccountBinding binding;
    private EditText mCity;
    private EditText mAge;
    private FirebaseUser LoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePatientAccountBinding.inflate(getLayoutInflater());

        mFullName = binding.fullName;
        mEmail = binding.Email;
        mPassword = binding.password;
        mPhone = binding.phone;
        mCity = binding.city;
        mAge = binding.age;
        mCreateAccountActivityBtn = binding.registerBtn;
        mLoginBtn = binding.createText;

        fAuth = FirebaseAuth.getInstance();

        FirebaseAuth fAuthForCreation = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this, new FirebaseOptions.Builder().setApplicationId("takecare-ab5ca").setApiKey("AIzaSyC8Pdf1UqOi96Hz7k-IkAygKm5BIUv3pD8").build(), "ForCreation"));
        fStore = FirebaseFirestore.getInstance();
        progressBar = binding.progressBar;
        parentUID = fAuth.getCurrentUser().getUid();
        LoggedUser = fAuth.getCurrentUser();


        setContentView(binding.getRoot());

        mCreateAccountActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone = mPhone.getText().toString();
                final String age = mAge.getText().toString();
                final String city = mCity.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuthForCreation.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(CreatePatientAccountActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                            String userID = task.getResult().getUser().getUid();

                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullName);
                            user.put("email", email);
                            user.put("age", age);
                            user.put("city", city);
                            user.put("phone", phone);
                            user.put("supervisor", false);
                            user.put("parent", parentUID);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    Log.d(TAG, "LoggedUser: user Profile is created for " + LoggedUser.getUid());

                                    fAuth.updateCurrentUser(LoggedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onSuccess: current Profile  " + fAuth.getCurrentUser().getUid());
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), SupervisorActivity.class));

                        } else {
                            Toast.makeText(CreatePatientAccountActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}