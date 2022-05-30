package com.aqu.takecare.data.model;


import java.util.Date;

public class Drug {
    private String DrugName;
    private String Dosage;
    private int DailyDosage = 0;
    private int DosagePeriod = 0;
    private Date startDate;
    private int ActualDailyDosage = 0;
    private int ActualTotalDailyDosageUntilToday = 0;
    private String patient;

    public Drug() {
    }

    public Drug(String drugName, String dosage, int dailyDosage, int dosagePeriod, Date startDate, int actualDailyDosage, int actualTotalDailyDosageUntilToday, String patient) {
        DrugName = drugName;
        Dosage = dosage;
        DailyDosage = dailyDosage;
        DosagePeriod = dosagePeriod;
        this.startDate = startDate;
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

    public String getDosage() {
        return Dosage;
    }

    public void setDosage(String dosage) {
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
}
