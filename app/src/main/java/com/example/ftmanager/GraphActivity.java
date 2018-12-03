package com.example.ftmanager;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraphActivity extends AppCompatActivity {

    private TextView startDateTV, endDateTV;
    private DatePickerDialog.OnDateSetListener startDateSetListener, endDateSetListener;
    private GraphView graphView;
    private CheckBox checkBox;
    private Spinner locSpinner;
    private List<String> locationList;
    private HashMap<String,Location> locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Bundle b = getIntent().getExtras();
        if(b != null){
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }


        startDateTV = (TextView)findViewById(R.id.startDate);
        endDateTV = (TextView)findViewById(R.id.endDate);

        graphView = (GraphView)findViewById(R.id.graphView);

        checkBox = (CheckBox)findViewById(R.id.cashCredit);

        locSpinner = (Spinner)findViewById(R.id.locationSpinner3);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0, "Select a location");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                graphView.removeAllSeries();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GraphActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, startDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GraphActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, endDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month +=1;
                String date = month + "/" + day + "/" + year;
                startDateTV.setText(date);
            }
        };

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month +=1;
                String date = month + "/" + day + "/" + year;
                endDateTV.setText(date);
            }
        };

        //disabled until there is a graph
        checkBox.setEnabled(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                graphDataPoints(graphView);
            }
        });


    }

    public void graphDataPoints(View view){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        if(locSpinner.getSelectedItem().toString().equals("Select a location")){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please select a location before proceeding");
            alertDialog.show();
        }
        else if(startDateTV.getText().toString().equals("") || endDateTV.getText().toString().equals("")){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please select a range of dates");
            alertDialog.show();
        }
        else if(!validDateRange(startDateTV.getText().toString(), endDateTV.getText().toString())){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("The range of dates is invalid");
            alertDialog.show();
        }
        else {

            graphView.removeAllSeries();

            checkBox.setEnabled(true);

            //parameters for the databaseConnector
            String type = "get_f_data";
            String location = locationMap.get(locSpinner.getSelectedItem().toString()).getLocationID() + "";
            String startDate = EarningsReport.formatDateYYYYMMDD(startDateTV.getText().toString());
            String endDate = EarningsReport.formatDateYYYYMMDD(endDateTV.getText().toString());
            DatabaseConnector databaseConnector = new DatabaseConnector();
            List<EarningsReport> earningsReportList = new ArrayList<EarningsReport>();

            try {
                String reportValues = databaseConnector.execute(type, startDate, endDate, location).get();
                if(!reportValues.equals("failure")) {
                    for (String report : reportValues.split("@")) {
                        earningsReportList.add(new EarningsReport(report.split(",")));
                    }
                }


                if (checkBox.isChecked()) {
                    LineGraphSeries<DataPoint> lineGraphCash = new LineGraphSeries<DataPoint>();
                    LineGraphSeries<DataPoint> lineGraphCredit = new LineGraphSeries<DataPoint>();

                    for (EarningsReport earningsReport : earningsReportList) {
                        Date x = new SimpleDateFormat("yyyy-MM-dd").parse(earningsReport.getDate());
                        double yCash = earningsReport.getCash().doubleValue();
                        double yCredit = earningsReport.getCredit().doubleValue();

                        lineGraphCash.appendData(new DataPoint(x, yCash), true, 365);
                        lineGraphCredit.appendData(new DataPoint(x, yCredit), true, 365);
                    }
                    graphView.addSeries(lineGraphCash);
                    graphView.addSeries(lineGraphCredit);

                    lineGraphCash.setTitle("cash");
                    lineGraphCredit.setTitle("credit");
                    lineGraphCredit.setColor(Color.RED);
                    graphView.getLegendRenderer().setVisible(true);
                    graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                } else {
                    LineGraphSeries<DataPoint> lineGraph = new LineGraphSeries<DataPoint>();

                    for (EarningsReport earningsReport : earningsReportList) {
                        Date x = new SimpleDateFormat("yyyy-MM-dd").parse(earningsReport.getDate());
                        double y = earningsReport.getTotal().doubleValue();

                        lineGraph.appendData(new DataPoint(x, y), true, 365);
                    }
                    graphView.addSeries(lineGraph);

                    lineGraph.setTitle("total");
                    graphView.getLegendRenderer().setVisible(true);
                    graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                }


                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                graphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

                Date xMin = new SimpleDateFormat("MM/dd/yyyy").parse(startDateTV.getText().toString());
                Date xMax = new SimpleDateFormat("MM/dd/yyyy").parse(endDateTV.getText().toString());

                graphView.getViewport().setMinX(xMin.getTime());
                graphView.getViewport().setMaxX(xMax.getTime());
                graphView.getViewport().setXAxisBoundsManual(true);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validDateRange(String date1, String date2){
        try {
            Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(date1);
            Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse(date2);
            return d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

}
