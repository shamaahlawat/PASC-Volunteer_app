package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class DisplayProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DocumentReference mRef;

    //Map<String, Object> doc_id;
    ModelUsers md;
    ArrayList<domain> domainList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        Intent intent = getIntent();
        final String Name=intent.getStringExtra(ExampleAdapter.TEXT_NAME);
        String Year=intent.getStringExtra(ExampleAdapter.TEXT_YEAR);
        String Dept=intent.getStringExtra(ExampleAdapter.TEXT_DEPT);
        String ID=intent.getStringExtra(ExampleAdapter.EMAIL_ID);

        TextView name = findViewById(R.id.name);
        TextView year = findViewById(R.id.year);
        TextView dept = findViewById(R.id.dept);
        name.setText(Name);
        year.setText(Year);
        dept.setText(Dept);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(user.getEmail());

        domainList = new ArrayList<>();



        FirebaseFirestore.getInstance().collection("User").document(ID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                ModelUsers md = task.getResult().toObject(ModelUsers.class);
                md.addnew();
                Iterator<String> itr = md.dom.iterator();
                Log.d("while11", "onComplete: entered");
                while(itr.hasNext()) {
                    Log.d("while1", "onComplete: entered");
                    String d = itr.next();
                    switch (d) {
                        case "Android": {
                            domainList.add(new domain(R.drawable.android, d));
                            break;
                        }
                        case "Web": {
                            domainList.add(new domain(R.drawable.web, d));
                            break;
                        }
                        case "ML": {
                            domainList.add(new domain(R.drawable.maclearn, d));
                            break;
                        }

                    }
                }
                if(md!=null) {

                    recyclerView = findViewById(R.id.rv);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(DisplayProfileActivity.this);
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    recyclerView.setLayoutManager(rvLiLayoutManager);

                    profile_fragment dom = new profile_fragment(DisplayProfileActivity.this,domainList);

                    recyclerView.setAdapter(dom);
                     /*
                    try{
                    //if image is received, then load
                        Picasso.get().load(image).into(profilePic);
                    }catch (Exception e){
                        //if error occurs, then set default
                        Picasso.get().load(R.drawable.ic_addphoto_white).into(avatar);
                    }*/

                }
            }
        });
    }
}
