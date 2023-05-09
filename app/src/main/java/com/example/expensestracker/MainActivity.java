package com.example.expensestracker;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.provider.Settings;


public class MainActivity extends AppCompatActivity {

    public TextView mainTextViewDashboard;
    public Button personalDashButton;
    public Button foodDashButton;
    public Button transportDashButton;
    public Button homeDashButton;
    public Button taxesDashButton;
    public Button savingsDashButton;
    public Button investDashButton;
    public Button resetDashButton;
    public EditText personalDashEditField;
    public EditText foodDashEditField;
    public EditText transportDashEditField;
    public EditText homeDashEditField;
    public EditText taxesDashEditField;
    public EditText savingsDashEditField;
    public EditText investDashEditField;
    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            // Handle the notification data here
        }
    };

    public int resetCount = 0;
    public int daysPassed = 0;
    public TextView personalDashTextView, foodDashTextView, transportDashTextView, homeDashTextView, taxesDashTextView, savingsDashTextView, investDashTextView, generalDashTextView, recordDashTextView;
    public Bank bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyAccessibilityService.setNotificationListener(this);
        startAccessibilityService();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter("com.example.expensestracker.NOTIFICATION_RECEIVED"));


        mainTextViewDashboard = (TextView) findViewById(R.id.mainTextViewDashboard);
        personalDashButton = (Button) findViewById(R.id.personalDashButton);
        foodDashButton = (Button) findViewById(R.id.foodDashButton);
        transportDashButton = (Button) findViewById(R.id.transportDashButton);
        homeDashButton = (Button) findViewById(R.id.homeDashButton);
        taxesDashButton = (Button) findViewById(R.id.taxesDashButton);
        savingsDashButton = (Button) findViewById(R.id.savingsDashButton);
        investDashButton = (Button) findViewById(R.id.investDashButton);
        resetDashButton = (Button) findViewById(R.id.resetDashButton);
        personalDashEditField = (EditText) findViewById(R.id.personalDashEditField);
        foodDashEditField = (EditText) findViewById(R.id.foodDashEditField);
        transportDashEditField = (EditText) findViewById(R.id.transportDashEditField);
        homeDashEditField = (EditText) findViewById(R.id.homeDashEditField);
        taxesDashEditField = (EditText) findViewById(R.id.taxesDashEditField);
        savingsDashEditField = (EditText) findViewById(R.id.savingsDashEditField);
        investDashEditField = (EditText) findViewById(R.id.investDashEditField);

        personalDashTextView = (TextView) findViewById(R.id.personalDashTextView);
        foodDashTextView = (TextView) findViewById(R.id.foodDashTextView);
        transportDashTextView = (TextView) findViewById(R.id.transportDashTextView);
        homeDashTextView = (TextView) findViewById(R.id.homeDashTextView);
        taxesDashTextView = (TextView) findViewById(R.id.taxesDashTextView);
        savingsDashTextView = (TextView) findViewById(R.id.savingsDashTextView);
        investDashTextView = (TextView) findViewById(R.id.investDashTextView);
        generalDashTextView = (TextView) findViewById(R.id.generalDashTextView);
        recordDashTextView = (TextView) findViewById(R.id.recordDashTextView);

        bank = new Bank(getApplicationContext());
        bank.setGoalsPrefab(1);
        clearTextViews();
        initialSpends();
        generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
        daysPassed = bank.daysPassed();



        resetDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resetCount == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "CLICK 2 more times to reset", Toast.LENGTH_LONG);
                    toast.show();
                    resetCount++;
                }//1click
                else if(resetCount == 1){
                    Toast toast = Toast.makeText(getApplicationContext(), "WARNING 1 MORE TIME", Toast.LENGTH_LONG);
                    toast.show();
                    resetCount++;
                }
                else if(resetCount == 2){
                    Toast toast = Toast.makeText(getApplicationContext(), "All reset...", Toast.LENGTH_LONG);
                    toast.show();
                    bank.deepClear();
                    bank.setGoalsPrefab(1);
                    clearTextViews();
                    initialSpends();
                    resetCount = 0;
                }
                colorCheckBalance();
            }
        });

        personalDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(personalDashEditField.getText().toString());
                    if(amount > 0) {
                        bank.deposit(bank.PERSONAL, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception if the input is not a number
                }
                personalDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.PERSONAL)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });

        foodDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(foodDashEditField.getText().toString());
                    if(amount > 0) {
                        bank.deposit(bank.FOOD, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception if the input is not a number
                }
                foodDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.FOOD)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });

        transportDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(transportDashEditField.getText().toString());
                    if(amount > 0) {
                        bank.deposit(bank.TRANSPORT, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception if the input is not a number
                }
                transportDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.TRANSPORT)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });//DO MINUS GENERAL FOR PERSONALS

        homeDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(homeDashEditField.getText().toString());
                    if (amount > 0) {
                        bank.deposit(bank.HOME, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception if
                }
                homeDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.HOME)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });

        taxesDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(taxesDashEditField.getText().toString());
                    if (amount > 0) {
                        bank.deposit(bank.TAXES, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
// Handle exception
                }
                taxesDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.TAXES)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });


        savingsDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(savingsDashEditField.getText().toString());
                    if (amount > 0) {
                        bank.deposit(bank.SAVINGS, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception
                }
                savingsDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.SAVINGS)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });

        investDashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(investDashEditField.getText().toString());
                    if (amount > 0) {
                        bank.deposit(bank.INVEST, amount);
                        bank.updateBank();
                    }
                } catch (NumberFormatException e) {
                    // Handle exception
                }
                investDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.INVEST)));
                clearTextViews();
                generalDashTextView.setText(String.valueOf(bank.checkBalance(bank.GENERAL))+"$");
                colorCheckBalance();
            }
        });

        callDayChecker();

    }//onCreate


    public void callDayChecker() {
        if (daysPassed > 7){
            bank.softClear();
        }
        lastWeekRoutine();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
        super.onDestroy();
    }

    @Override
    public void onNotificationReceived(String title, String text) {
        // Process the notification data (title and text) as needed
    }
    @Override
    protected void onResume() {


        private MyNotificationListenerService myNotificationListenerService;
    private ServiceConnection notificationListenerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyNotificationListenerService.MyBinder binder = (MyNotificationListenerService.MyBinder) iBinder;
            myNotificationListenerService = binder.getService();
            myNotificationListenerService.sendExistingNotifications();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myNotificationListenerService = null;
        }
    };

    private void bindNotificationListenerService() {
        if (isNotificationAccessEnabled()) {
            Intent intent = new Intent(this, MyNotificationListenerService.class);
            bindService(intent, notificationListenerServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }


    private void requestNotificationAccess() {
        if (!isNotificationAccessEnabled()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isNotificationAccessEnabled() {
        String enabledListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return enabledListeners != null && enabledListeners.contains(getPackageName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNotificationAccess();
    }



    private void lastWeekRoutine() {
        bank.findNetWeek();
        float money = bank.lastWeekCounter();
        recordDashTextView.setText(String.valueOf(money));


        if (money == 0){
            //do nothing
        }else if(money > 0){
            recordDashTextView.setTextColor(Color.rgb(34,139,34));
        }
        else{
            recordDashTextView.setTextColor(Color.rgb(139,0,0));
        }
    }

    private void startAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public void colorCheckBalance(){

        float money = bank.checkBalance(bank.GENERAL);

        if (money == 0){
            generalDashTextView.setTextColor(Color.rgb(105,105,105));
        }else if(money > 0){
            generalDashTextView.setTextColor(Color.rgb(34,139,34));
        }
        else{
            generalDashTextView.setTextColor(Color.rgb(139,0,0));
        }
    }
    public void initialSpends(){
        personalDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.PERSONAL)));
        foodDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.FOOD)));
        transportDashTextView.setText(String.valueOf(bank.howMuchToSpend(bank.TRANSPORT)));
        homeDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.HOME)));
        taxesDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.TAXES)));
        savingsDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.SAVINGS)));
        investDashTextView.setText(String.valueOf(bank.howMuchToSave(bank.INVEST)));
    }
    public void clearTextViews() {
        personalDashEditField.setText("");
        foodDashEditField.setText("");
        transportDashEditField.setText("");
        homeDashEditField.setText("");
        taxesDashEditField.setText("");
        savingsDashEditField.setText("");
        investDashEditField.setText("");
    }
    
    

}//main