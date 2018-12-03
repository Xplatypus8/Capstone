package com.example.ftmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ViewScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner locSpinner;
    private ArrayList<String> locationList;
    private ArrayList<Schedule> schedule, locationSchedule;
    private HashMap<String,Location> locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);


        schedule = new ArrayList<Schedule>();

        fillSchedule();

        Bundle b = getIntent().getExtras();
        if(b != null){
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }


        locSpinner = (Spinner)findViewById(R.id.locationSpinnerVS);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0, "Select a location");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        //init recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(locationList.indexOf(locSpinner.getSelectedItem().toString())!=0){
                    locationSchedule = getSchedulesAtLocation();
                    ViewScheduleAdapter adapter = new ViewScheduleAdapter(ViewScheduleActivity.this, locationSchedule);

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

    private ArrayList<Schedule> getSchedulesAtLocation(){
        ArrayList<Schedule> schedulesAtLocation = new ArrayList<>();
        for(Schedule slot: schedule){
            if(slot.getLocationID() == locationMap.get(locSpinner.getSelectedItem().toString()).getLocationID()){
                schedulesAtLocation.add(slot);
            }
        }
        return schedulesAtLocation;
    }
}
