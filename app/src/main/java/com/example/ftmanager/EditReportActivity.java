package com.example.ftmanager;

import android.app.DatePickerDialog;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditReportActivity extends AppCompatActivity {

    private EarningsReport currentReport;
    private EditText cashET, creditET;
    private Spinner locSpinner;
    private List<String> locationList;
    private TextView modifyReportDateTV;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        Bundle b = getIntent().getExtras();
        if(b != null){
            currentReport = b.getParcelable("currentReport");
        }
        else {
            currentReport = null;
        }

        cashET = (EditText)findViewById(R.id.modifyCashET);
        creditET = (EditText) findViewById(R.id.modifyCreditET);

        locSpinner = (Spinner) findViewById(R.id.locationSpinner5);
        locationList = new ArrayList<String>(Arrays.asList("Select a location", "Garrisonville", "Deacon", "North Carolina"));
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

    public void modifyReport(View view){
        String type = "modify_report";
        String id = currentReport.getId() + "";
        String locationID = locationList.indexOf(locSpinner.getSelectedItem().toString())==0 ? currentReport.getLocationID() + "" : locationList.indexOf(locSpinner.getSelectedItem().toString())+"";
        String cash = cashET.getText().toString().equals("") ? currentReport.getCash().toString() : cashET.getText().toString();
        String credit = creditET.getText().toString().equals("") ? currentReport.getCredit().toString() : creditET.getText().toString();
        String date = modifyReportDateTV.getText().toString().equals("") ? currentReport.getDate() : EarningsReport.formatDateYYYYMMDD(modifyReportDateTV.getText().toString());


        DatabaseConnector dbConnector = new DatabaseConnector();

        try {
            String result = dbConnector.execute(type, id, cash, credit, locationID, date).get();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
