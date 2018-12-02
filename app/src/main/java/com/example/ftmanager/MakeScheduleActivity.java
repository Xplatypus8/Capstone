package com.example.ftmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MakeScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner locSpinner;
    private ArrayList<String> nameList, locationList;
    private ArrayList<Schedule> schedule, locationSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_schedule);

        schedule = new ArrayList<Schedule>();

        fillSchedule();
        initializeNameList();


        locSpinner = (Spinner)findViewById(R.id.locationSpinnerMS);
        locationList = new ArrayList<String>(Arrays.asList("Select a location", "Garrisonville", "Deacon", "North Carolina"));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        //init recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(locationList.indexOf(locSpinner.getSelectedItem().toString())!=0){
                    locationSchedule = getSchedulesAtLocation();
                    addSchedule();
                    MakeScheduleAdapter adapter = new MakeScheduleAdapter(MakeScheduleActivity.this, locationSchedule, schedule, nameList);

                    if(recyclerView.getAdapter() == null) {
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        recyclerView.swapAdapter(adapter, true);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });


    }

    public void fillSchedule(){
        DatabaseConnector dbConnect = new DatabaseConnector();
        try {
            String scheduleData = dbConnect.execute("view_schedule").get();

            if(!scheduleData.equals("failure")) {
                for (String data : scheduleData.split("@")) {
                    schedule.add(new Schedule(data.split(",")));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void addSchedule(){
        while(locationSchedule.size() != 14){
            try {
                Schedule newSchedule;
                if(locationSchedule.size() > 0) {
                    //Create a date for the day after the last item in the schedule list
                    Date lastDate = new SimpleDateFormat("MM/dd/yyyy").parse(locationSchedule.get(locationSchedule.size() - 1).getDate());
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    newSchedule = new Schedule("unassigned", "unassigned", dateFormat.format(addDay(lastDate)), locationList.indexOf(locSpinner.getSelectedItem().toString()));
                }
                else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    newSchedule = new Schedule("unassigned", "unassigned", dateFormat.format(date), locationList.indexOf(locSpinner.getSelectedItem().toString()));

                }

                DatabaseConnector dbConnect = new DatabaseConnector();
                String id = dbConnect.execute("add_schedule", newSchedule.getLocationID()+"", EarningsReport.formatDateYYYYMMDD(newSchedule.getDate())).get();
                newSchedule.setID(Integer.parseInt(id));
                schedule.add(newSchedule);
                locationSchedule.add(newSchedule);

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    private void initializeNameList(){

        HashMap<Integer, String> userMap;
        Bundle data = getIntent().getExtras();
        if(data != null){
            userMap = (HashMap<Integer,String>) data.getSerializable("userMap");
        }
        else {
            userMap = null;
        }
        nameList = new ArrayList<String>(userMap.values());
    }

    private Date addDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        return date;
    }

    private ArrayList<Schedule> getSchedulesAtLocation(){
        ArrayList<Schedule> schedulesAtLocation = new ArrayList<>();
        for(Schedule slot: schedule){
            if(slot.getLocationID() == locationList.indexOf(locSpinner.getSelectedItem().toString())){
                schedulesAtLocation.add(slot);
            }
        }
        return schedulesAtLocation;
    }

    public void editSchedule(View view){

        AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);
        verifyDialog.setTitle("Verify Information:")
                .setMessage("Are you sure you want to make these change?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        for(Schedule slot: schedule){
                            if(slot.updateRequired()){
                                DatabaseConnector dbConnect = new DatabaseConnector();
                                dbConnect.execute("update_schedule", slot.getID()+"", slot.getEmployeeOne(), slot.getEmployeeTwo());
                                slot.setUpdateRequired(false);
                            }
                        }

                            AlertDialog.Builder resultDialog = new AlertDialog.Builder(MakeScheduleActivity.this);

                                resultDialog.setTitle("Status")
                                        .setMessage("Updates have been submitted.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        }).show();
    }
}