package com.aqu.takecare.data.model;


import java.util.Date;

public class Drug {

    private String Id;
    private String DrugName;
    private int Dosage;
    private int DailyDosage = 0;
    private int DosagePeriod = 0;
    private Date startDate;
    private Date last_update;
    private int ActualDailyDosage = 0;
    private int ActualTotalDailyDosageUntilToday = 0;
    private String patient;

    public Drug() {
    }

    public Drug(String id, String drugName, int dosage, int dailyDosage, int dosagePeriod, Date startDate, Date last_update, int actualDailyDosage, int actualTotalDailyDosageUntilToday, String patient) {
        Id = id;
        DrugName = drugName;
        Dosage = dosage;
        DailyDosage = dailyDosage;
        DosagePeriod = dosagePeriod;
        this.startDate = startDate;
        this.last_update = last_update;
        ActualDailyDosage = actualDailyDosage;
        ActualTotalDailyDosageUntilToday = actualTotalDailyDosageUntilToday;
        this.patient = patient;
    }


    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public int getDosage() {
        return Dosage;
    }

    public void setDosage(int dosage) {
        Dosage = dosage;
    }

    public int getDailyDosage() {
        return DailyDosage;
    }

    public void setDailyDosage(int dailyDosage) {
        DailyDosage = dailyDosage;
    }

    public int getDosagePeriod() {
        return DosagePeriod;
    }

    public void setDosagePeriod(int dosagePeriod) {
        DosagePeriod = dosagePeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getActualDailyDosage() {
        return ActualDailyDosage;
    }

    public void setActualDailyDosage(int actualDailyDosage) {
        ActualDailyDosage = actualDailyDosage;
    }

    public int getActualTotalDailyDosageUntilToday() {
        return ActualTotalDailyDosageUntilToday;
    }

    public void setActualTotalDailyDosageUntilToday(int actualTotalDailyDosageUntilToday) {
        ActualTotalDailyDosageUntilToday = actualTotalDailyDosageUntilToday;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }
}
