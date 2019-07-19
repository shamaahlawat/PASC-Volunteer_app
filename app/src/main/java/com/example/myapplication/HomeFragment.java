package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView P_recyclerView;
    RecyclerView.Adapter P_adapter;
    ArrayList<ModelPosts> postsList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        postsList = new ArrayList<ModelPosts>();

        P_recyclerView = view.findViewById(R.id.posts_recycledView);
        P_recyclerView.setHasFixedSize(true);
        P_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //P_adapter = new PostAdapter(postsList);
        //P_recyclerView.setAdapter(P_adapter);

        getAllPosts();

        return view;

    }

    private void getAllPosts() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("Post").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //ModelPosts md = document.toObject(ModelPosts.class);
                                ModelPosts md=new ModelPosts(document.get("Title").toString(),document.get("Description").toString(),document.get("Date").toString());

                                if(true) {
                                    //usersList.add(md);
                                    postsList.add(new ModelPosts(md.title,md.description,md.date));
                                    Log.d("AAAAAAAAAAAAAAAAA", md.toString());

                                }

                                //data.setText(usersList.toString());

                                P_adapter = new PostAdapter(postsList);
                                P_recyclerView.setAdapter(P_adapter);

                            }

                        } else {

                            Toast.makeText(getActivity(), "Action Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
