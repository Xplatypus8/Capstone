package com.example.ftmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MakeScheduleAdapter extends RecyclerView.Adapter<MakeScheduleAdapter.MakeScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;
    private List<String> userList;

    public MakeScheduleAdapter(Context context, List<Schedule> scheduleList, List<String> userList) {
        setHasStableIds(true);
        this.context = context;
        this.scheduleList = scheduleList;
        this.userList = userList;
    }

    public String formatDateMMDDYYYY(String yyyymmdd){
        String [] dateArray = yyyymmdd.split("-");
        return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];
    }


    @Override
    public MakeScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_make_schedule, null);
        return new MakeScheduleViewHolder(view);
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
    public void onBindViewHolder(@NonNull final MakeScheduleViewHolder holder, final int position) {
        final Schedule schedule = scheduleList.get(position);

        final ArrayList<String> availableEmployees = new ArrayList<String>(userList);

        holder.dateTV.setText(schedule.getDate());

        holder.employeeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu1 = new PopupMenu(context, holder.employeeOne);

                for(String name: availableEmployees){
                    popupMenu1.getMenu().add(name);
                }
                popupMenu1.getMenuInflater().inflate(R.menu.schedule_popup_menu, popupMenu1.getMenu());

                popupMenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getTitle().equals("unassigned")){


                            if(holder.employeeOneTV.getText().toString().equals("unassigned")){
                                //do nothing
                            }
                            else{
                                availableEmployees.add(holder.employeeOneTV.getText().toString());
                                holder.employeeOneTV.setText(R.string.unassigned);
                            }
                            /*Intent intent = new Intent(context, ViewReportDetails.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
                            b.putSerializable("userMap", userMap);
                            intent.putExtras(b);
                            context.startActivity(intent);*/
                        }
                        else{
                            for(String name: availableEmployees){
                                if(menuItem.getTitle().equals(name) && !holder.employeeOneTV.getText().toString().equals("unassigned")){
                                    availableEmployees.add(holder.employeeOneTV.getText().toString());
                                    holder.employeeOneTV.setText(name);
                                    availableEmployees.remove(name);
                                    break;
                                }
                                else if(menuItem.getTitle().equals(name) && holder.employeeOneTV.getText().toString().equals("unassigned")){
                                    holder.employeeOneTV.setText(name);
                                    availableEmployees.remove(name);
                                    break;

                                }
                            }
                        }

                        return true;
                    }
                });
                popupMenu1.show();
            }
        });

        holder.employeeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.employeeTwo);

                for(String name: availableEmployees){
                    popupMenu.getMenu().add(name);
                }
                popupMenu.getMenuInflater().inflate(R.menu.schedule_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getTitle().equals("unassigned")){


                            if(holder.employeeTwoTV.getText().toString().equals("unassigned")){
                                //do nothing
                            }
                            else{
                                availableEmployees.add(holder.employeeTwoTV.getText().toString());
                                holder.employeeTwoTV.setText(R.string.unassigned);
                            }
                            /*Intent intent = new Intent(context, ViewReportDetails.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
                            b.putSerializable("userMap", userMap);
                            intent.putExtras(b);
                            context.startActivity(intent);*/
                        }
                        else{
                            for(String name: availableEmployees){
                                if(menuItem.getTitle().equals(name) && !holder.employeeTwoTV.getText().toString().equals("unassigned")){
                                    availableEmployees.add(holder.employeeTwoTV.getText().toString());
                                    holder.employeeTwoTV.setText(name);
                                    availableEmployees.remove(name);
                                    break;
                                }
                                else if(menuItem.getTitle().equals(name) && holder.employeeTwoTV.getText().toString().equals("unassigned")){
                                    holder.employeeTwoTV.setText(name);
                                    availableEmployees.remove(name);
                                    break;

                                }
                            }
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

        return scheduleList.size();
    }

    public class MakeScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, employeeOneTV, employeeTwoTV;
        CardView employeeOne, employeeTwo;

        public MakeScheduleViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.msDateTV);
            employeeOne = itemView.findViewById(R.id.msEmployee1);
            employeeTwo = itemView.findViewById(R.id.msEmployee2);
            employeeOneTV = itemView.findViewById(R.id.msEmployee1TV);
            employeeTwoTV = itemView.findViewById(R.id.msEmployee2TV);
            /*title = itemView.findViewById(R.id.productTitle);
            amountET = itemView.findViewById(R.id.amountET);
            checkBox = itemView.findViewById(R.id.checkBox);

            amountET.setEnabled(false);
            //stops app from crashing when selecting next edittext
            amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);*/
        }
    }
}
