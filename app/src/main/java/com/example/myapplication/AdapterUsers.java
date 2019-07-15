package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    List<ModelUsers> userList;

    public AdapterUsers(Context context, List<ModelUsers> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        String userName = userList.get(position).getName();
        String userYear = userList.get(position).getYear();
        String userDept = userList.get(position).getDept();
        //similarly for image or pro pic

        holder.name.setText(userName);
        holder.year.setText(userYear);
        holder.dept.setText(userDept);

        /*
        * for image
        * try{
        *   Picasso.get().load(usrImage).placeHolder(R.drawable.ic_defalutImg).into(holder.profilePic);
        *
        * }catch(Exception e){
        *
        * }
        * */

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+holder.name, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView name, year, dept;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePicture);
            name = itemView.findViewById(R.id.name);
            dept = itemView.findViewById(R.id.department);
            year = itemView.findViewById(R.id.year);
        }




    }

}
