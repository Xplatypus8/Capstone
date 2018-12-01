package com.example.ftmanager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MakeScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> nameList;
    private ArrayList<Schedule> schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_schedule);

        schedule = new ArrayList<Schedule>();

        fillSchedule();
        addSchedule();
        initializeNameList();///////////////////////////////////THIS MUST BE FIXED

        //init recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MakeScheduleAdapter adapter = new MakeScheduleAdapter(this, schedule, nameList);

        recyclerView.setAdapter(adapter);


    }

    public void fillSchedule(){
        DatabaseConnector dbConnect = new DatabaseConnector();
        try {
            String scheduleData = dbConnect.execute("view_schedule", "1").get();

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
        while(schedule.size() != 14){
            try {
                Schedule newSchedule;
                if(schedule.size() > 0) {
                    //Create a date for the day after the last item in the schedule list
                    Date lastDate = new SimpleDateFormat("MM/dd/yyyy").parse(schedule.get(schedule.size() - 1).getDate());
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    newSchedule = new Schedule("unassigned", "unassigned", dateFormat.format(addDay(lastDate)), 1);
                }
                else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    newSchedule = new Schedule("unassigned", "unassigned", dateFormat.format(date), 1);

                }

                DatabaseConnector dbConnect = new DatabaseConnector();
                String id = dbConnect.execute("add_schedule", newSchedule.getLocationID()+"", EarningsReport.formatDateYYYYMMDD(newSchedule.getDate())).get();
                newSchedule.setID(Integer.parseInt(id));
                schedule.add(newSchedule);

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
}

