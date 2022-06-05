package com.aqu.takecare.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.aqu.takecare.R;
import com.aqu.takecare.data.model.Drug;
import com.aqu.takecare.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BackService extends Service {
    public static final String CHANNEL_ID = "Take Care";
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private List<Drug> Drugs = new ArrayList<>();

    public BackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Query data = fStore.collection("drugs").whereEqualTo("patient", mAuth.getCurrentUser().getUid());
        Log.d("Service", mAuth.getCurrentUser().getUid() + "");
        data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> DrugsData = task.getResult().getDocuments();
                int size = DrugsData.size();

                for (int i = 0; i < size; i++) {
                    DocumentSnapshot DrugObject = DrugsData.get(i);
                    Drug currentItem = DrugObject.toObject(Drug.class);
                    currentItem.setId(DrugObject.getId());
                    Drugs.add(currentItem);

                    Calendar cal = Calendar.getInstance();
                    long hours = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                        cal.set(Calendar.HOUR_OF_DAY, 0);

                        hours = ChronoUnit.HOURS.between(cal.toInstant(), Calendar.getInstance().getTime().toInstant());
                    }
                    int hoursForEachDosage = 24 / currentItem.getDailyDosage();
                    int dosage = (int) Math.ceil((float) hours / hoursForEachDosage);


                    Log.d("BackService TAG", "hours " + hours + "");
                    Log.d("BackService TAG", "hoursForEachDosage " + hoursForEachDosage + "");
                    Log.d("BackService TAG", "dosage " + dosage + "");


                    Intent intent = new Intent(BackService.this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("FromService", "true");
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    String dosageOrderName = "";
                    switch (dosage) {
                        case 1:
                            dosageOrderName = "first";
                            break;
                        case 2:
                            dosageOrderName = "second";
                            break;
                        case 3:
                            dosageOrderName = "third";
                            break;
                    }
                    if ((dosage <= currentItem.getDailyDosage()) && dosage > currentItem.getActualDailyDosage()) {
                        cal.set(Calendar.HOUR_OF_DAY, hoursForEachDosage * (dosage - 1));
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_baseline_medical_services_24)
                                .setContentTitle("Dosage Time"
//                                        + " before "+cal.getTime().toString()
                                )
                                .setContentText("Please take " +
//                                                "(" + currentItem.getDosage() + ")" +
//                                                " of" +
                                                "your " +
//                                                + dosageOrderName +
                                                "dosage of " + currentItem.getDrugName()
//                                        +" between "+cal.getTime().toString()
                                )
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(i, builder.build());
                    }


                }
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}