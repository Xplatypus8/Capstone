package com.example.ftmanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditReportActivity extends AppCompatActivity {

    private EarningsReport currentReport;
    private EditText cashET, creditET;
    private Spinner locSpinner;
    private List<String> locationList;
    private HashMap<String, Location> locationMap;
    private TextView modifyReportDateTV;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            currentReport = b.getParcelable("currentReport");
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        } else {
            currentReport = null;
            locationMap = null;
        }

        cashET = (EditText) findViewById(R.id.modifyCashET);
        creditET = (EditText) findViewById(R.id.modifyCreditET);

        locSpinner = (Spinner) findViewById(R.id.locationSpinner5);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0, "Select a location");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);


        modifyReportDateTV = (TextView) findViewById(R.id.modifyReportDateTV);
        modifyReportDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditReportActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = month + "/" + day + "/" + year;
                modifyReportDateTV.setText(date);
            }
        };
    }

    public void modifyReport(View view) {
        final String type = "modify_report";
        final String id = currentReport.getId() + "";
        final String locationID = locationList.indexOf(locSpinner.getSelectedItem().toString()) == 0 ? currentReport.getLocationID() + "" : locationMap.get(locSpinner.getSelectedItem().toString()).getLocationID() + "";
        final String cash = cashET.getText().toString().equals("") ? currentReport.getCash().toString() : cashET.getText().toString();
        final String credit = creditET.getText().toString().equals("") ? currentReport.getCredit().toString() : creditET.getText().toString();
        final String date = modifyReportDateTV.getText().toString().equals("") ? currentReport.getDate() : EarningsReport.formatDateYYYYMMDD(modifyReportDateTV.getText().toString());


            ////////////////////////////////////////////////

            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);
            verifyDialog.setTitle("Verify Information:")
                    .setMessage("Are you sure these cahnges are correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseConnector dbConnect = new DatabaseConnector();
                            try {
                                DatabaseConnector dbConnector = new DatabaseConnector();
                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(EditReportActivity.this);
                                String result = dbConnector.execute(type, id, cash, credit, locationID, date).get();
                                if (result.equals("success")) {
                                    resultDialog.setTitle("Status")
                                            .setMessage("Report has been changed! Perform a new search to see the change.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            }).show();

                                } else {
                                    resultDialog.setTitle("Status")
                                            .setMessage("Failure. Try again.")
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

            ///////////////////////////////////////////////


        }
    }
