package com.example.ftmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportFinanceActivity extends AppCompatActivity {

    private EditText cashET, creditET;
    private Spinner locSpinner;
    private User currentUser;
    private HashMap<String, Location> locationMap;
    private List<String> locationList;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_finance);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            currentUser = b.getParcelable("currentUser");
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }


        cashET = (EditText)findViewById(R.id.cashET);
        creditET = (EditText)findViewById(R.id.creditET);
        locSpinner = (Spinner)findViewById(R.id.locationSpinner);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0,"Select a location");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(adapter);
        locSpinner.setSelection(0);
    }

    public void submitFinanceReport(View view) {
        final String location = locSpinner.getSelectedItem().toString();
        final String cash = cashET.getText().toString();
        final String credit = creditET.getText().toString();
        final String userID = currentUser.getUserID() + "";
        if (location.equals("Select a location") || cash.equals("") || credit.equals("")) {
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please fill out all fields");
            alertDialog.show();
        } else {
            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);
            verifyDialog.setTitle("Verify Information:")
                    .setMessage("Today, you've earned $" + cash + " in cash, and $" + credit + " in credit at " +location +".")
            .setPositiveButton("Correct", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseConnector dbConnect = new DatabaseConnector();
                    try {
                        AlertDialog.Builder resultDialog = new AlertDialog.Builder(ReportFinanceActivity.this);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = Calendar.getInstance().getTime();
                        String data = dbConnect.execute("send_f_report", cash, credit, location, userID, simpleDateFormat.format(date)).get();
                        if (data.equals("success")){
                            resultDialog.setTitle("Status")
                                    .setMessage("Report has been submitted!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }).show();

                        }
                        else{
                            resultDialog.setTitle("Status")
                                    .setMessage("Failure. Did not submit report. Please try again or contact the owner.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Do nothing
                                        }
                                    }).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                }
            }).show();

        }
    }
}
