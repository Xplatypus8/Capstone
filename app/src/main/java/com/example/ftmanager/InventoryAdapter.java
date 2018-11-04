package com.example.ftmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private Context context;
    private List<Product> productList;

    public InventoryAdapter(Context context, List<Product> productList) {
        setHasStableIds(true);
        this.context = context;
        this.productList = productList;
    }


    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_inventory, null);
        return new InventoryViewHolder(view);
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
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getName());
    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public InventoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productTitle);
        }
    }
}
