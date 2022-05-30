package com.aqu.takecare.ui.patient;

import static com.aqu.takecare.ui.supervisor.SupervisorActivity.PatientUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.aqu.takecare.R;
import com.aqu.takecare.data.model.Drug;
import com.aqu.takecare.databinding.ActivityPatientBinding;
import com.aqu.takecare.ui.login.LoginActivity;
import com.aqu.takecare.ui.medicine.CreateMedicineActivity;
import com.aqu.takecare.ui.medicine.DrugArrayAdapter;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPatientBinding binding;
    private String patientUID;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ListView listOfdrug;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        List<Drug> Drugs = new ArrayList<>();
        binding = ActivityPatientBinding.inflate(getLayoutInflater());
        View header = getLayoutInflater().inflate(R.layout.head_layout, null);

        listOfdrug = binding.drugList;
        fab = binding.fab;

        listOfdrug.addHeaderView(header);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        patientUID = fAuth.getCurrentUser().getUid();
        fab.setVisibility(View.INVISIBLE);
        if (getIntent().getExtras() != null) {
            patientUID = (String) getIntent().getExtras().get(SupervisorActivity.PatientUID);
            fab.setVisibility(View.VISIBLE);
        }

        Query data = fStore.collection("drugs").whereEqualTo("patient", patientUID);

        data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> DrugsData = task.getResult().getDocuments();
                int size = DrugsData.size();

                for (int i = 0; i < size; i++) {
                    DocumentSnapshot DrugObject = DrugsData.get(i);
                    Drug drug = DrugObject.toObject(Drug.class);
                    Drugs.add(drug);
                }
                DrugArrayAdapter adapter = new DrugArrayAdapter(getApplicationContext(), Drugs);

                listOfdrug.setAdapter(adapter);
//
//
//                listOfPatient.setLongClickable(true);
//                listOfPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                        Intent intent = new Intent(SupervisorActivity.this, EditPatientProfileActivity.class);
//                        intent.putExtra(PatientUID, user_ids.get(position));
//                        startActivity(intent);
//                        return true;
//                    }
//                });
//                listOfPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = new Intent(SupervisorActivity.this, PatientActivity.class);
//                        intent.putExtra(PatientUID, user_ids.get(position));
//                        startActivity(intent);
//                    }
//                });
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientActivity.this, CreateMedicineActivity.class);
                intent.putExtra(PatientUID, patientUID);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                fAuth.signOut();
                startActivity(new Intent(PatientActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}