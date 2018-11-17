package com.example.ftmanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

public class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.EarningsViewHolder> {

    private Context context;
    private List<EarningsReport> earningsReportList;

    public EarningsAdapter(Context context, List<EarningsReport> earningsReportList) {
        setHasStableIds(true);
        this.context = context;
        this.earningsReportList = earningsReportList;
    }

    public String formatDateDDMMYYYY(String yyyymmdd){
        String [] dateArray = yyyymmdd.split("-");
        return dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
    }


    @Override
    public EarningsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_earnings, null);
        return new EarningsViewHolder(view);
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
    public void onBindViewHolder(@NonNull final EarningsViewHolder holder, final int position) {
        EarningsReport report = earningsReportList.get(position);

        holder.dateTV.setText(formatDateDDMMYYYY(report.getDate()));
        holder.totalTV.setText("Total: " + report.getTotal().toString());
        holder.cashTV.setText("Cash: " + report.getCash().toString());
        holder.creditTV.setText("Credit: " + report.getCredit().toString());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.button);
                popupMenu.getMenuInflater().inflate(R.menu.report_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Report Details")){
                            //launch new activity
                        }
                        else{
                            context.startActivity(new Intent(context, EditReportActivity.class));
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return earningsReportList.size();
    }

    public class EarningsViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, totalTV, cashTV, creditTV;
        ImageButton button;

        public EarningsViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            totalTV = itemView.findViewById(R.id.totalTV);
            cashTV = itemView.findViewById(R.id.cashTV);
            creditTV = itemView.findViewById(R.id.creditTV);
            button = itemView.findViewById(R.id.popup);

            /*title = itemView.findViewById(R.id.productTitle);
            amountET = itemView.findViewById(R.id.amountET);
            checkBox = itemView.findViewById(R.id.checkBox);

            amountET.setEnabled(false);
            //stops app from crashing when selecting next edittext
            amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);*/
        }
    }
}
