package com.example.ftmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//Adapter for the MakeSchedule class
public class MakeScheduleAdapter extends RecyclerView.Adapter<MakeScheduleAdapter.MakeScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList, entireSchedule;
    private List<String> userList;

    public MakeScheduleAdapter(Context context, List<Schedule> scheduleList, List<Schedule> entireSchedule, List<String> userList) {
        setHasStableIds(true);
        this.context = context;
        this.scheduleList = scheduleList; //schedule of the selected location.
        this.entireSchedule = entireSchedule; //entire schedule of all locations
        this.userList = userList;
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

        removeUsedNames(holder.availableEmployees, schedule.getDate());

        holder.dateTV.setText(schedule.getDate());
        holder.employeeOneTV.setText(schedule.getEmployeeOne());
        holder.employeeTwoTV.setText(schedule.getEmployeeTwo());

        //a popup menu apears when the cardview is selected
        holder.employeeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu1 = new PopupMenu(context, holder.employeeOne);

                for(String name: holder.availableEmployees){
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
                                holder.availableEmployees.add(holder.employeeOneTV.getText().toString());
                                holder.employeeOneTV.setText(R.string.unassigned);
                                schedule.setEmployeeOne("unassigned");
                                scheduleList.get(position).setEmployeeOne("unassigned");
                                schedule.setUpdateRequired(true);
                                scheduleList.get(position).setUpdateRequired(true);

                            }
                        }
                        else{
                            for(String name: holder.availableEmployees){
                                if(menuItem.getTitle().equals(name) && !holder.employeeOneTV.getText().toString().equals("unassigned")){
                                    holder.availableEmployees.add(holder.employeeOneTV.getText().toString());
                                    holder.employeeOneTV.setText(name);
                                    holder.availableEmployees.remove(name);
                                    schedule.setEmployeeOne(name);
                                    scheduleList.get(position).setEmployeeOne(name);
                                    schedule.setUpdateRequired(true);
                                    scheduleList.get(position).setUpdateRequired(true);
                                    break;
                                }
                                else if(menuItem.getTitle().equals(name) && holder.employeeOneTV.getText().toString().equals("unassigned")){
                                    holder.employeeOneTV.setText(name);
                                    holder.availableEmployees.remove(name);
                                    schedule.setEmployeeOne(name);
                                    scheduleList.get(position).setEmployeeOne(name);
                                    schedule.setUpdateRequired(true);
                                    scheduleList.get(position).setUpdateRequired(true);
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

                for(String name: holder.availableEmployees){
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
                                holder.availableEmployees.add(holder.employeeTwoTV.getText().toString());
                                holder.employeeTwoTV.setText(R.string.unassigned);
                                schedule.setEmployeeTwo("unassigned");
                                scheduleList.get(position).setEmployeeTwo("unassigned");
                                schedule.setUpdateRequired(true);
                                scheduleList.get(position).setUpdateRequired(true);
                            }
                        }
                        else{
                            for(String name: holder.availableEmployees){
                                if(menuItem.getTitle().equals(name) && !holder.employeeTwoTV.getText().toString().equals("unassigned")){
                                    holder.availableEmployees.add(holder.employeeTwoTV.getText().toString());
                                    holder.employeeTwoTV.setText(name);
                                    holder.availableEmployees.remove(name);
                                    schedule.setEmployeeTwo(name);
                                    scheduleList.get(position).setEmployeeTwo(name);
                                    schedule.setUpdateRequired(true);
                                    scheduleList.get(position).setUpdateRequired(true);
                                    break;
                                }
                                else if(menuItem.getTitle().equals(name) && holder.employeeTwoTV.getText().toString().equals("unassigned")){
                                    holder.employeeTwoTV.setText(name);
                                    holder.availableEmployees.remove(name);
                                    schedule.setEmployeeTwo(name);
                                    scheduleList.get(position).setEmployeeTwo(name);
                                    schedule.setUpdateRequired(true);
                                    scheduleList.get(position).setUpdateRequired(true);
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

    //Remove names that are already on the schedule for a given date
    public void removeUsedNames(ArrayList<String> list, String date){
        for(Schedule slot: entireSchedule){
            if(slot.getDate().equals(date)){
                list.remove(slot.getEmployeeOne());
                list.remove(slot.getEmployeeTwo());
            }
        }
    }

    public class MakeScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, employeeOneTV, employeeTwoTV;
        CardView employeeOne, employeeTwo;
        ArrayList<String> availableEmployees; //list of emplyee names that have not been scheduled on a certain date.

        public MakeScheduleViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.msDateTV);
            employeeOne = itemView.findViewById(R.id.msEmployee1);
            employeeTwo = itemView.findViewById(R.id.msEmployee2);
            employeeOneTV = itemView.findViewById(R.id.msEmployee1TV);
            employeeTwoTV = itemView.findViewById(R.id.msEmployee2TV);
            availableEmployees = new ArrayList<String>(userList);

        }


    }
}