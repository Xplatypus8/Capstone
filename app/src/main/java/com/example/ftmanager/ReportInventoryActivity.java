package com.example.ftmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReportInventoryActivity extends AppCompatActivity {

    private List<Product> productList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_inventory);

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

        InventoryAdapter adapter = new InventoryAdapter(this, productList);

        recyclerView.setAdapter(adapter);
    }
}
