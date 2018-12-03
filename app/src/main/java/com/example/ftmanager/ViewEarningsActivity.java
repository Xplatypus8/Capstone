package com.example.ftmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewEarningsActivity extends AppCompatActivity {

    private Spinner locSpinner;
    private List<String> locationList;
    private HashMap<String, Location> locationMap;
    private List<EarningsReport> reportList;
    private TextView earningsDateTV;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private EarningsAdapter earningsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_earnings);

        Bundle b = getIntent().getExtras();
        if(b != null){
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }

        calendar = Calendar.getInstance();

        locSpinner = (Spinner)findViewById(R.id.locationSpinner4);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0, "Select a location");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        earningsDateTV = (TextView)findViewById(R.id.earningsDateTV);
        earningsDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewEarningsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month +=1;
                String date = month + "/" + day + "/" + year;
                earningsDateTV.setText(date);
            }
        };
    }

    public void getEarningsReports(View view){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        if(locSpinner.getSelectedItem().toString().equals("Select a location")){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please select a location before proceeding");
            alertDialog.show();
        }
        else if(earningsDateTV.getText().toString().equals("")){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please select a date");
            alertDialog.show();
        }
        else {

            String type = "get_f_data";
            String startDate = formatDateYYYYMMDD(earningsDateTV.getText().toString());
            String endDate = sdf.format(calendar.getTime());
            String location = locationMap.get(locSpinner.getSelectedItem().toString()).getLocationID() +"";

            DatabaseConnector databaseConnector = new DatabaseConnector();
            try {
                reportList = new ArrayList<EarningsReport>();

                String reportValues = databaseConnector.execute(type, startDate, endDate, location).get();
                if (!reportValues.equals(null) && !reportValues.equals("login failed")) {
                    for (String report : reportValues.split("@")) {
                        reportList.add(new EarningsReport(report.split(",")));
                    }
                }

                //init recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                earningsAdapter = new EarningsAdapter(this, reportList);

                recyclerView.setAdapter(earningsAdapter);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public String formatDateYYYYMMDD(String mmddyyyy){
        String [] dateArray = mmddyyyy.split("/");
        return dateArray[2] + "-" + dateArray[0] + "-" + dateArray[1];
    }
}
