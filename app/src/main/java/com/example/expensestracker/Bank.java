package com.example.expensestracker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bank {
    public SharedPreferences prefs;
    public final String FOOD = "food";
    public final String PERSONAL = "personal";
    public final String TRANSPORT = "transport";
    public final String INVEST = "invest";
    public final String SAVINGS = "savings";
    public final String TAXES = "taxes";
    public final String HOME = "home";
    public final String GENERAL = "general";
    public final String STORAGE = "storage";
//    public Random rand = new Random();
//    public int keyVal;
    private final String [] CATEGORIES = { "personal", "food", "transport", "home", "taxes", "savings","invest"};//no general
    public float[] precongf_1 = {75f, 62.5f, 25f, 178.75f, 63.18f, 315.88f, 252.7f};

    public Bank(Context context) {
        prefs = context.getSharedPreferences("bank", Context.MODE_PRIVATE);
    }

    public void deposit(String category, float amount) {
        float currentAmount = prefs.getFloat(category, 0);
        prefs.edit().putFloat(category, currentAmount + amount).apply();
    }

    public void withdraw(String category, float amount) {
        float currentAmount = prefs.getFloat(category, 0);
        prefs.edit().putFloat(category, currentAmount - amount).apply();
    }

    public void setBalance(String category, float amount){
        prefs.edit().putFloat(category, amount).apply();
    }//setBalance

    public float checkBalance(String category) {
        return prefs.getFloat(category, 0);
    }

    public void setSavingGoal(String category, float goal) {
        prefs.edit().putFloat(category + "_goal", goal).apply();
    }

    public float getSavingGoal(String category) {
        return prefs.getFloat(category + "_goal", 0);
    }

    public void updateBank(){

        float sum = 0;
        for (String category : CATEGORIES) {
            float balance = checkBalance(category);
            if(category.equals("personal") || category.equals("food") || category.equals("transport")){
                sum -= balance;
            }else{
            sum += balance;}
        }
        //float summ = checkBalance(GENERAL) + sum;
        prefs.edit().putFloat(GENERAL, sum).apply();

    }

    public void setGoalsPrefab(int confg) {
        if(confg == 1){
            int i = 0;
            for (String category : CATEGORIES) {
                setSavingGoal(category, precongf_1[i]);
                i++;
            }//for
        }//congf1
    }//setGoals

    public void balanceClear(){
        softClear();
        setBalance(GENERAL, 0);
    }//balanceClear

    public void softClear(){
        for (String category : CATEGORIES) {
            setBalance(category, 0);
        }//for
    }

    public void goalsClear(){
        for (String category : CATEGORIES) {
            setSavingGoal(category, 0);
        }//for
    }
    public void deepClear(){
        balanceClear();
        goalsClear();
        setBalance(STORAGE, 0);
    }//deepClear

    public float howMuchToSpend(String category){
        return getSavingGoal(category)-checkBalance(category);
    }

    public float howMuchToSave(String category){
        return checkBalance(category) - getSavingGoal(category);
    }

    public int daysPassed(){
        long lastOpened = prefs.getLong("last_opened", 0);

        if(lastOpened == 0) {
            prefs.edit().putLong("last_opened", System.currentTimeMillis()).apply();
        } else {
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastOpened;
            int daysPassed = (int) TimeUnit.MILLISECONDS.toDays(diff);
            // do something with the number of days passed
            if (daysPassed > 7){
            prefs.edit().putLong("last_opened", currentTime).apply();
            }
            return daysPassed;
        }
        return 0;
    }

    public float lastWeekCounter(){
        return prefs.getFloat(STORAGE, 0);
    }

    public float findNetWeek(){
        float sum = 0;
        for (String category : CATEGORIES) {
            float balance = checkBalance(category);
            if(category.equals("personal") || category.equals("food") || category.equals("transport")){
                sum -= balance;
            }else{
                sum += balance;}
        }
        prefs.edit().putFloat(GENERAL, 0).apply();
        prefs.edit().putFloat(STORAGE, sum).apply();
        return sum;
    }

}//Bank
