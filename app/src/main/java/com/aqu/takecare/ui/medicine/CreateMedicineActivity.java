package com.aqu.takecare.ui.medicine;

import static com.aqu.takecare.ui.supervisor.SupervisorActivity.PatientUID;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aqu.takecare.databinding.ActivityCreateMedicineBinding;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateMedicineActivity extends AppCompatActivity {
    private static final String TAG = "CreateMedicineActivity";
    private ActivityCreateMedicineBinding binding;
    private EditText mDrugName;
    private EditText mDosage;
    private Spinner mDailyDosage;
    private Button mSave;
    private ProgressBar mProgressBar;
    private FirebaseFirestore fStore;
    private String patientUID;
    private Spinner mPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateMedicineBinding.inflate(getLayoutInflater());
        mDrugName = binding.Name;
        mDosage = binding.dosage;
        mDailyDosage = binding.DailyDosage;
        mPeriod = binding.period;
        mSave = binding.save;
        mProgressBar = binding.progressBar;
        fStore = FirebaseFirestore.getInstance();
        setContentView(binding.getRoot());

        patientUID = (String) getIntent().getExtras().get(PatientUID);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String DrugName = mDrugName.getText().toString().trim();
                String Dosage = mDosage.getText().toString().trim();
                final String DailyDosage = mDailyDosage.getSelectedItem().toString();
                final String Period = mPeriod.getSelectedItem().toString();


                if (TextUtils.isEmpty(DrugName)) {
                    mDrugName.setError("DrugName is Required.");
                    return;
                }

                if (TextUtils.isEmpty(Dosage)) {
                    mDosage.setError("Dosage is Required.");
                    return;
                }


                mProgressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                int DosageDaily = getDailyDosageValue(DailyDosage);
                int DosagePeriod = getPeriodValue(Period);


                DocumentReference documentReference = fStore.collection("drugs").document();
                Map<String, Object> Drug = new HashMap<>();
                Drug.put("DrugName", DrugName);
                Drug.put("Dosage", Dosage);
                Drug.put("DailyDosage", DosageDaily);
                Drug.put("DosagePeriod", DosagePeriod);
                Drug.put("startDate", Calendar.getInstance().getTime());
                Drug.put("last_update", Calendar.getInstance().getTime());
                Drug.put("ActualDailyDosage", 0);
                Drug.put("ActualTotalDailyDosageUntilToday", 0);

                Drug.put("patient", patientUID);

                documentReference.set(Drug).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Drug  is created for ");
                        Intent intent = new Intent(CreateMedicineActivity.this, SupervisorActivity.class);
                        intent.putExtra(PatientUID, patientUID);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });


            }
        });
    }

    int getPeriodValue(String period) {
        int value = 1;
        switch (period) {
            case "One Time":
                value = 1;
                break;
            case "1 week":
                value = 7;
                break;
            case "2 weeks":
                value = 14;
                break;
            case "3 weeks":
                value = 21;
                break;
            case "4 weeks":
                value = 27;
                break;
            case "5 weeks":
                value = 34;
                break;
        }

        return value;
    }

    int getDailyDosageValue(String period) {
        int value = 1;
        switch (period) {
            case "One Time":
                value = 1;
                break;
            case "One Time Per Day":
                value = 1;
                break;
            case "Two Times Per Day":
                value = 2;
                break;
            case "Three Time Per Day":
                value = 3;
                break;
        }

        return value;
    }

}