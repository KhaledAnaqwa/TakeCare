package com.aqu.takecare.ui.medicine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aqu.takecare.R;
import com.aqu.takecare.data.model.Drug;

import java.util.List;

public class DrugArrayAdapter extends ArrayAdapter<Drug> {

    private final Context mContext;
    private List<Drug> mObjects;
    private boolean isSupervisor;

    public DrugArrayAdapter(@NonNull Context context, @NonNull List<Drug> objects, boolean isSupervisor) {
        super(context, R.layout.drug_layout, objects);
        this.mContext = context;
        this.mObjects = objects;
        this.isSupervisor = isSupervisor;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.drug_layout, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Drug currentItem = getItem(position);

        int NewDosage = currentItem.getDosage();
//        NewDosage = currentItem.getDailyDosage() * currentItem.getDosagePeriod();
        int dosage = 0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            long hours = ChronoUnit.HOURS.between(currentItem.getStartDate().toInstant(), Calendar.getInstance().getTime().toInstant());
//            int hoursForEachDosage = 24 / currentItem.getDailyDosage();
//            dosage = (int) Math.ceil((float) hours / hoursForEachDosage);
//            Log.d("TAG", "hours " + hours + "");
//            Log.d("TAG", "hoursForEachDosage " + hoursForEachDosage + "");
//            Log.d("TAG", "dosage " + dosage + "");
//        }

        // then according to the position of the view assign the desired image for the same
        Log.d("TAG", "currentItem.getDailyDosage() : currentItem.getDosagePeriod() " + currentItem.getDailyDosage() + " : " + currentItem.getDosagePeriod());
        Log.d("TAG", "NewDosage " + NewDosage + "");
        TextView DrugName = currentItemView.findViewById(R.id.DrugName);
        TextView TodayDosage = currentItemView.findViewById(R.id.TodayDosage);
        TextView TotalDosage = currentItemView.findViewById(R.id.TotalDosage);
        if (!isSupervisor) {
            View delete = currentItemView.findViewById(R.id.delete);
            delete.setVisibility(View.GONE);
        }

        assert currentItem != null;
        DrugName.setText(currentItem.getDrugName());
        TodayDosage.setText(currentItem.getActualDailyDosage() + "/" + currentItem.getDailyDosage());
        Drawable TodayDosageRes = getRatioColor(currentItem.getActualDailyDosage(), currentItem.getDailyDosage());
        TodayDosage.setBackground(TodayDosageRes);


        TotalDosage.setText((currentItem.getActualTotalDailyDosageUntilToday() + "/" + NewDosage));
        Drawable TotalDosageRes = getRatioColor(currentItem.getActualTotalDailyDosageUntilToday(), NewDosage);
        TotalDosage.setBackground(TotalDosageRes);


        // then return the recyclable view
        return currentItemView;


    }

    private Drawable getRatioColor(int firstNum, int secondNum) {
        String green_circle = "@drawable/green_circle";
        String orange_circle = "@drawable/orange_circle";
        String red_circle = "@drawable/red_circle";
        float half = secondNum / 2;
        int actualDailyDosage = firstNum;

        String uri = green_circle;
        Log.d("TAG", "firstNum " + firstNum);

        Log.d("TAG", "secondNum " + secondNum + "");
        Log.d("TAG", "half " + half + "");


        if ((actualDailyDosage == 0) || (actualDailyDosage >= 0 && half > actualDailyDosage)) {
            uri = red_circle;
        } else if (half == actualDailyDosage) {
            uri = orange_circle;
        }

        int identifierResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

        Drawable res = mContext.getResources().getDrawable(identifierResource);
        return res;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }
}
