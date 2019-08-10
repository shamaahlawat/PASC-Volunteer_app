package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewListActivity extends AppCompatActivity {
    public ArrayList<String> nameslist = new ArrayList<>();
    ListView listView;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        listView = (ListView) findViewById(R.id.LView);

        String id=getIntent().getStringExtra("POST_ID");
        db = FirebaseFirestore.getInstance();
        db.collection("Post").document(id).collection("Interested People").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    nameslist.add(snapshot.getString("name"));
                }
                adapter = new ArrayAdapter<String>(ViewListActivity.this, R.layout.retrieve_name, nameslist);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
    }
}
