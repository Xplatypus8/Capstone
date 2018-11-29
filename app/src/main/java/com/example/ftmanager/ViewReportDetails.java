package com.example.ftmanager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class ViewReportDetails extends AppCompatActivity {

    private HashMap<Integer, String> userMap;
    private EarningsReport report;
    private TextView location, date, user, cash, credit, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_details);

        Bundle data = getIntent().getExtras();
        if(data != null){
            userMap = (HashMap<Integer,String>) data.getSerializable("userMap");
            report = data.getParcelable("currentReport");
        }
        else {
            userMap = null;
        }

        location = (TextView)findViewById(R.id.textView11);
        date = (TextView)findViewById(R.id.textView16);
        user = (TextView)findViewById(R.id.textView12);
        cash = (TextView)findViewById(R.id.textView13);
        credit = (TextView)findViewById(R.id.textView14);
        total = (TextView)findViewById(R.id.textView15);

        location.setText("Location: " + report.getLocationID());
        date.setText("Date: " + EarningsReport.formatDateMMDDYYYY(report.getDate()));
        user.setText("User: " + userMap.get(report.getUserID()));
        cash.setText("Cash: $" + report.getCash().toString());
        credit.setText("Credit: $" + report.getCredit().toString());
        total.setText("Total: $" + report.getTotal().toString());
    }

}
