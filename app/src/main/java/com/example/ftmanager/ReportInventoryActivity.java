package com.example.ftmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportInventoryActivity extends AppCompatActivity {

    private List<Product> productList;
    private List<String> locationList;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private Spinner locSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_inventory);

        //Populate location spinner
        locSpinner = (Spinner)findViewById(R.id.locationSpinner2);
        locationList = new ArrayList<String>(Arrays.asList("Select a location", "Garrisonville", "Deacon", "North Carolina"));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        //Init recyclerview
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<Product>();
        productList.add(new Product("Vanilla Mix", ""));
        productList.add(new Product("Chocolate Mix", ""));
        productList.add(new Product("Reese's Bits", ""));
        productList.add(new Product("Butterfingers Bits", ""));
        productList.add(new Product("M&Ms", ""));
        productList.add(new Product("Oreo Bits", ""));
        productList.add(new Product("Peanut Butter", ""));
        productList.add(new Product("Whipped Cream", ""));
        productList.add(new Product("Nuts", ""));
        productList.add(new Product("Hot Fudge", ""));

        inventoryAdapter = new InventoryAdapter(this, productList);

        recyclerView.setAdapter(inventoryAdapter);
    }

    public void submitInventory(View view){
        final List<Product> requiredProducts = inventoryAdapter.getSelectedItems();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        if(locSpinner.getSelectedItem().toString().equals(locationList.get(0))) {
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please select a location before proceeding");
            alertDialog.show();
        }
        else if(requiredProducts.size() == 0){
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("No items have been selected");
            alertDialog.show();
        }
        else{
            final String location = locSpinner.getSelectedItem().toString();

            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);
            verifyDialog.setTitle("Verify Information:")
                    .setMessage("You are submitting this report for the " +location +" location.")
                    .setPositiveButton("Correct", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           // try {
                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(ReportInventoryActivity.this);
                                for(Product product: requiredProducts){
                                    DatabaseConnector dbConnect = new DatabaseConnector();
                                    dbConnect.execute("send_i_report", product.getName(), product.getQuantity(), location);
                                    //Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                                    /*if(!data.equals("success")){
                                        resultDialog.setTitle("Status")
                                                .setMessage("Failure. Did not submit report. Please try again or contact the owner.")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //Do nothing
                                                    }
                                                }).show();
                                        return;
                                    }*/
                                }

                                    resultDialog.setTitle("Status")
                                            .setMessage("Report has been submitted!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            }).show();

                            /*} catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }*/
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