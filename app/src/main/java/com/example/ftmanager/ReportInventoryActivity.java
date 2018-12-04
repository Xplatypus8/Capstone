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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//sends inventory data to the database
public class ReportInventoryActivity extends AppCompatActivity {

    private List<Product> productList;
    private List<String> locationList;
    private RecyclerView recyclerView;
    private HashMap<String, Location> locationMap;
    private InventoryAdapter inventoryAdapter;
    private Spinner locSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_inventory);

        Bundle b = getIntent().getExtras();
        if(b != null){
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }

        //Populate location spinner
        locSpinner = (Spinner)findViewById(R.id.locationSpinner2);
        locationList = new ArrayList<String>(locationMap.keySet());
        locationList.add(0,"Select a location");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(spinnerAdapter);
        locSpinner.setSelection(0);

        //Init recyclerview
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //list of products employees may need to buy at the store before work
        productList = new ArrayList<Product>();
        productList.add(new Product("Vanilla Mix", ""));
        productList.add(new Product("Chocolate Mix", ""));
        productList.add(new Product("Reeses Bits", ""));
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
                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(ReportInventoryActivity.this);
                                for(Product product: requiredProducts){
                                    DatabaseConnector dbConnect = new DatabaseConnector();
                                    dbConnect.execute("send_i_report", product.getName(), product.getQuantity(), location);
                                }

                                    resultDialog.setTitle("Status")
                                            .setMessage("Report has been submitted!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
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
}
