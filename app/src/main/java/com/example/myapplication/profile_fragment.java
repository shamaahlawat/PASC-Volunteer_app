package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class profile_fragment extends RecyclerView.Adapter<profile_fragment.ViewHolder> {


    private Context mContext;
    private ArrayList <domain> dlist;

    profile_fragment(Context context, ArrayList <domain> list)
    {
       dlist = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
       View view =  layoutInflater.inflate(R.layout.info,parent,false);
       ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView image = holder.domain_image;
        TextView name = holder.domain_name;

        image.setImageResource(dlist.get(position).getImage());
        name.setText(dlist.get(position).getDname());
    }

    @Override
    public int getItemCount() {
        return dlist.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView domain_image;
        TextView domain_name;
        public ViewHolder(View itemView){
            super(itemView);
            domain_image = itemView.findViewById(R.id.img);
            domain_name = itemView.findViewById(R.id.tv);
        }
    }
}
