package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.classes.ModelUsers;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public static final String TEXT_NAME = "com.example.myapplication.TEXT_NAME";
    public static final String TEXT_YEAR = "com.example.myapplication.TEXT_YEAR";
    public static final String TEXT_DEPT = "com.example.myapplication.TEXT_DEPT";
    public static final String EMAIL_ID = "com.example.myapplication.EMAIL_ID";

    private ArrayList<ModelUsers> mExampleList;
    private ArrayList<ModelUsers> modelArrayList;
    public String emailid;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView emailText;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            emailText = itemView.findViewById(R.id.email);
            emailText.setVisibility(View.GONE);

            final View view=itemView;
            final Context c =view.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(view.getContext(),"ABCDEFG",Toast.LENGTH_SHORT).show();

                    String name = (String)mTextView1.getText();
                    String year = (String)mTextView2.getText();
                    String dept = (String)mTextView3.getText();
                    String emailId = (String)emailText.getText();


                    Intent intent = new Intent(view.getContext(), DisplayProfileActivity.class);
                    intent.putExtra(TEXT_NAME,name);
                    intent.putExtra(TEXT_YEAR,year);
                    intent.putExtra(TEXT_DEPT,dept);
                    intent.putExtra(EMAIL_ID,emailId);
                    c.startActivity(intent);
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ModelUsers> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardtemplate, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ModelUsers currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getYear());
        holder.mTextView3.setText(currentItem.getDept());
        holder.emailText.setText(currentItem.getEmail());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}