package com.example.ftmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewInventoryAdapter extends RecyclerView.Adapter<ViewInventoryAdapter.ViewInventoryViewHolder> {

    private Context context;
    private List<Product> productList;
    private List<Product> productsAtLocation;

    public ViewInventoryAdapter(Context context, List<Product> productList, int locationID) {
        setHasStableIds(true);
        this.context = context;
        this.productList = productList;
        this.productsAtLocation = new ArrayList<Product>();
        for(Product product: productList){
            if(product.getLocationID()==locationID){
                productsAtLocation.add(product);
            }
        }
    }

    public List<Product> getSelectedItems(){
        List<Product> selected = new ArrayList<Product>();
        for(Product product: productList){
            if(product.isNeeded()){
                selected.add(product);
            }
        }
        return selected;
    }


    @Override
    public ViewInventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_products, null);
        return new ViewInventoryViewHolder(view);
    }

    //Stops items from duplicating in recyclerview.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Stops items from duplicating in recyclerview.
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewInventoryViewHolder holder, final int position) {
        Product product = productsAtLocation.get(position);
        holder.titleTV.setText(product.getName());
        holder.quantityTV.setText(product.getQuantity());
        holder.dateTV.setText(product.getDate());

    }

    @Override
    public int getItemCount() {

        return productsAtLocation.size();
    }

    public class ViewInventoryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, quantityTV, dateTV;

        public ViewInventoryViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.invTitleTV);
            quantityTV = itemView.findViewById(R.id.invQuantityTV);
            dateTV = itemView.findViewById(R.id.invDateTV);
        }
    }
}
