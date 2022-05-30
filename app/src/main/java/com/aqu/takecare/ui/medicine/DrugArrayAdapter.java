package com.aqu.takecare.ui.medicine;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aqu.takecare.R;
import com.aqu.takecare.data.model.Drug;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class DrugArrayAdapter extends ArrayAdapter<Drug> {

    private final Context mContext;
    private List<Drug> mObjects;

    public DrugArrayAdapter(@NonNull Context context, @NonNull List<Drug> objects) {
        super(context, R.layout.drug_layout, objects);
        this.mContext = context;
        this.mObjects = objects;
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
        long days = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            days = ChronoUnit.DAYS.between(currentItem.getStartDate().toInstant(), Calendar.getInstance().getTime().toInstant());
        }
        // then according to the position of the view assign the desired image for the same

        TextView DrugName = currentItemView.findViewById(R.id.DrugName);
        TextView TodayDosage = currentItemView.findViewById(R.id.TodayDosage);
        TextView TotalDosage = currentItemView.findViewById(R.id.TotalDosage);


        assert currentItem != null;
        DrugName.setText(currentItem.getDrugName());
        TodayDosage.setText(currentItem.getActualDailyDosage() + "/" + currentItem.getDailyDosage());
        TotalDosage.setText((currentItem.getActualTotalDailyDosageUntilToday() + "/" + (currentItem.getDailyDosage() * days)));


        // then return the recyclable view
        return currentItemView;


    }

    @Override
    public int getCount() {
        return mObjects.size();
    }
}
