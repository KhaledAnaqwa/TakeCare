package com.aqu.takecare.ui.patient;

import static com.aqu.takecare.ui.supervisor.SupervisorActivity.PatientUID;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.aqu.takecare.R;
import com.aqu.takecare.data.model.Drug;
import com.aqu.takecare.databinding.ActivityPatientBinding;
import com.aqu.takecare.service.BackService;
import com.aqu.takecare.ui.login.LoginActivity;
import com.aqu.takecare.ui.medicine.CreateMedicineActivity;
import com.aqu.takecare.ui.medicine.DrugArrayAdapter;
import com.aqu.takecare.ui.supervisor.SupervisorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientActivity extends AppCompatActivity {

    WriteBatch batch = null;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPatientBinding binding;
    private String patientUID;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ListView listOfdrugs;
    private FloatingActionButton fab;
    private boolean isPatient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        List<Drug> Drugs = new ArrayList<>();
        binding = ActivityPatientBinding.inflate(getLayoutInflater());
//        View header = getLayoutInflater().inflate(R.layout.head_layout, null);

        listOfdrugs = binding.drugList;
        fab = binding.fab;

//        listOfdrugs.addHeaderView(header);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        patientUID = fAuth.getCurrentUser().getUid();
        fab.setVisibility(View.INVISIBLE);
        if (getIntent().getExtras() != null) {
            patientUID = (String) getIntent().getExtras().get(SupervisorActivity.PatientUID);
            fab.setVisibility(View.VISIBLE);
        } else {
            startService(new Intent(PatientActivity.this, BackService.class));
            isPatient = true;
        }

        Query data = fStore.collection("drugs").whereEqualTo("patient", patientUID);
        batch = null;
        data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> DrugsData = task.getResult().getDocuments();
                int size = DrugsData.size();

                for (int i = 0; i < size; i++) {
                    DocumentSnapshot DrugObject = DrugsData.get(i);
                    Drug drug = DrugObject.toObject(Drug.class);
                    drug.setId(DrugObject.getId());

                    Calendar cal = Calendar.getInstance();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(drug.getLast_update());
                    int Today = cal.get(Calendar.DAY_OF_YEAR);
                    int lastUpdateDay = cal1.get(Calendar.DAY_OF_YEAR);

                    if (Today > lastUpdateDay) {
                        if (batch == null)
                            batch = fStore.batch();
                        drug.setLast_update(cal.getTime());
                        drug.setActualDailyDosage(0);
                        batch.set(fStore.collection("drugs").document(drug.getId()), drug);
                    }
                    Drugs.add(drug);
                }
                DrugArrayAdapter adapter = new DrugArrayAdapter(getApplicationContext(), Drugs);

                listOfdrugs.setAdapter(adapter);
                if (isPatient)
                    listOfdrugs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //  checking current time is time of dosage then populate popup to confirm taking Dosage
                            //  else current dosage was taken
                            Drug currentDrug = Drugs.get(position);

//                            int currentHourOfTheDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//                        int hourPassedAfterStartOfCurrentDosage = currentHourOfTheDay % currentDrug.getDailyDosage();
//                            int hoursForEachDosage = 24 / currentDrug.getDailyDosage();
//                        int NumberOfDosageShouldBeTakenUntilCurrentTime = currentHourOfTheDay / hoursForEachDosage;


//                            long hours = 0;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                Calendar cal = Calendar.getInstance();
//                                cal.set(Calendar.HOUR_OF_DAY, 0);
//
//                                hours = ChronoUnit.HOURS.between(cal.toInstant(), Calendar.getInstance().getTime().toInstant());
//                            }
//
//                            int dosage = (int) Math.ceil((float) hours / hoursForEachDosage);
//                            if ((dosage <= currentDrug.getDailyDosage()) && dosage > currentDrug.getActualDailyDosage()) {


                            if (currentDrug.getActualTotalDailyDosageUntilToday() < (currentDrug.getDailyDosage() * currentDrug.getDosagePeriod())) {
                                AlertDialog alertDialog = new AlertDialog.Builder(PatientActivity.this).create();
                                alertDialog.setTitle("Dosage Confirmation !");
                                alertDialog.setMessage("Did you take your current dosage (" + currentDrug.getDosage() + ") of " + currentDrug.getDrugName() + "?");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //increase of taken daily & total dosages ..
                                                currentDrug.setActualDailyDosage(currentDrug.getActualDailyDosage() + 1);
                                                currentDrug.setActualTotalDailyDosageUntilToday(currentDrug.getActualTotalDailyDosageUntilToday() + 1);
                                                currentDrug.setLast_update(Calendar.getInstance().getTime());
                                                fStore.collection("drugs").document(currentDrug.getId()).set(currentDrug).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        dialog.dismiss();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("TAG", "onFailure: " + e.toString());
                                                    }
                                                });

                                            }
                                        });
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();

                            } else {

                            }

                        }
                    });
                if (batch != null)
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT);
                        }
                    });
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