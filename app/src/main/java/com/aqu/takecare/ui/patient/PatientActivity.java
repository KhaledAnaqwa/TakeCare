package com.aqu.takecare.ui.patient;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.aqu.takecare.databinding.ActivityPatientBinding;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.material.snackbar.Snackbar;

public class PatientActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPatientBinding binding;
    private String patientUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        patientUID = (String) getIntent().getExtras().get(SupervisorActivity.PatientUID);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action" + patientUID, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}