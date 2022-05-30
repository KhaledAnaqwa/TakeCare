package com.aqu.takecare.ui.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aqu.takecare.R;
import com.aqu.takecare.databinding.ActivitySupervisorBinding;
import com.aqu.takecare.ui.CreateAccount.CreatePatientAccountActivity;
import com.aqu.takecare.ui.login.LoginActivity;
import com.aqu.takecare.ui.patient.EditPatientProfileActivity;
import com.aqu.takecare.ui.patient.PatientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SupervisorActivity extends AppCompatActivity {


    public final static String PatientUID = "PatientUID";
    private ActivitySupervisorBinding binding;
    private ListView listOfPatient;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        List<String> patients = new ArrayList<>();
        List<String> user_ids = new ArrayList<>();

        binding = ActivitySupervisorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listOfPatient = binding.patientList;

        setSupportActionBar(binding.toolbar);

        Query data = fStore.collection("users").whereEqualTo("parent", fAuth.getCurrentUser().getUid());

        data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> patientsData = task.getResult().getDocuments();
                int size = patientsData.size();

                for (int i = 0; i < size; i++) {
                    DocumentSnapshot user = patientsData.get(i);
                    patients.add((String) user.get("fName"));
                    user_ids.add((String) user.getId());
//                    mFullName.setText((String)user.get("fName"));
//                    mEmail.setText((String)user.get("email"));
//                    mAge.setText((String)user.get("age"));
//                    mCity.setText((String)user.get("city"));
//                    mPhone.setText((String)user.get("phone"));
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, patients);

                listOfPatient.setAdapter(adapter);


                listOfPatient.setLongClickable(true);
                listOfPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent intent = new Intent(SupervisorActivity.this, EditPatientProfileActivity.class);
                        intent.putExtra(PatientUID, user_ids.get(position));
                        startActivity(intent);
                        return true;
                    }
                });
                listOfPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SupervisorActivity.this, PatientActivity.class);
                        intent.putExtra(PatientUID, user_ids.get(position));
                        startActivity(intent);
                    }
                });
            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupervisorActivity.this, CreatePatientAccountActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_profile:
                startActivity(new Intent(SupervisorActivity.this, EditProfileActivity.class));
                return true;
            case R.id.sign_out:
                fAuth.signOut();
                startActivity(new Intent(SupervisorActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
