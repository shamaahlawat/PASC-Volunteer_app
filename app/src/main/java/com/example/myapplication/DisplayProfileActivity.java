package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.adapters.ExampleAdapter;
import com.example.myapplication.adapters.myPostsProfileAdapter;
import com.example.myapplication.adapters.profile_fragment;
import com.example.myapplication.classes.ModelPosts;
import com.example.myapplication.classes.ModelUsers;
import com.example.myapplication.classes.domain;
import com.example.myapplication.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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

    myPostsProfileAdapter.PopUpEventListener p;
    ArrayList<ModelPosts> myposts;


    ModelUsers md;
    ArrayList<domain> domainList;
    RecyclerView recyclerView;
    Button viewmyPosts;
    RecyclerView recyclerViewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        Intent intent = getIntent();
        final String Name = intent.getStringExtra(ExampleAdapter.TEXT_NAME);
        String Year = intent.getStringExtra(ExampleAdapter.TEXT_YEAR);
        String Dept = intent.getStringExtra(ExampleAdapter.TEXT_DEPT);
        String ID = intent.getStringExtra(ExampleAdapter.EMAIL_ID);

        TextView name = findViewById(R.id.name);
        TextView year = findViewById(R.id.year);
        TextView dept = findViewById(R.id.dept);
        name.setText(Name);
        year.setText(Year);
        dept.setText(Dept);
        viewmyPosts = findViewById(R.id.showPostButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(ID);

        domainList = new ArrayList<>();
        recyclerViewPost = findViewById(R.id.rvPosts);

        FirebaseFirestore.getInstance().collection("User").document(ID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final ModelUsers md = task.getResult().toObject(ModelUsers.class);
                md.addnew();
                Iterator<String> itr = md.dom.iterator();
                Log.d("while11", "onComplete: entered");
                while (itr.hasNext()) {
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
                if (md != null) {

                    recyclerView = findViewById(R.id.rv);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(DisplayProfileActivity.this);
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    recyclerView.setLayoutManager(rvLiLayoutManager);

                    profile_fragment dom = new profile_fragment(DisplayProfileActivity.this, domainList);

                    recyclerView.setAdapter(dom);
                    viewmyPosts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPosts();
                        }
                    });

                }
            }
        });
    }

    private void showPosts() {

        myposts = new ArrayList<>();
        if (viewmyPosts.getText().toString().toLowerCase().equals("hide posts")) {
            viewmyPosts.setText("view posts");
            myposts.clear();
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(DisplayProfileActivity.this);
            final RecyclerView.LayoutManager rvLiLayoutManager1 = layoutManager1;
            myPostsProfileAdapter ma = new myPostsProfileAdapter(myposts, p);
            recyclerViewPost.setLayoutManager(rvLiLayoutManager1);
            recyclerViewPost.setAdapter(ma);
            Log.d("PROF FRAG", "FUNC CALLED");
        } else if (viewmyPosts.getText().toString().toLowerCase().equals("view posts")) {
            viewmyPosts.setText("hide posts");
            FirebaseFirestore.getInstance().collection("Post").whereEqualTo("OwnerOfPost", firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        ModelPosts md = new ModelPosts(document.get("Title").toString(), document.get("Description").toString(),
                                document.get("Date").toString(), document.get("Time").toString(), document.get("Type").toString(),
                                document.get("OwnerOfPost").toString(), document.get("id").toString());  //sanam
                        md.setTimeStamp((Timestamp) document.get("Timestamp"));
                        myposts.add(md);

                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(DisplayProfileActivity.this);
                        final RecyclerView.LayoutManager rvLiLayoutManager1 = layoutManager1;
                        myPostsProfileAdapter ma = new myPostsProfileAdapter(myposts, p);
                        recyclerViewPost.setLayoutManager(rvLiLayoutManager1);
                        recyclerViewPost.setAdapter(ma);
                        Log.d("PROF FRAG", "FUNC CALLED");
                    }

                }
            });
        }
    }
}
