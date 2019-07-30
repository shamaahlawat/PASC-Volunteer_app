package com.example.myapplication;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.Adapter Adapter;

    ArrayList<ModelUsers> usersList;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    TextView data;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);
        setHasOptionsMenu(true);

        usersList = new ArrayList<ModelUsers>();
        firebaseAuth = FirebaseAuth.getInstance();
        db =  FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.users_recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

   private void getAllUsers() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       db.collection("User").get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ModelUsers md = document.toObject(ModelUsers.class);

                        if(!user.getEmail().equals(document.getId())) {
                            //usersList.add(md);
                            usersList.add(new ModelUsers(md.name,md.year,md.dept));
                        }
                        Adapter = new ExampleAdapter(usersList);
                        recyclerView.setAdapter(Adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "Action Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SearchText(final String s){

        db.collection("User").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelUsers md = document.toObject(ModelUsers.class);

                                if(!firebaseAuth.getCurrentUser().getEmail().equals(document.getId())) {
                                    if(md.getName().toLowerCase().contains(s.toLowerCase())) {
                                        usersList.add(new ModelUsers(md.name, md.year, md.dept));
                                    }
                                }

                                Adapter = new ExampleAdapter(usersList);
                                recyclerView.setAdapter(Adapter);
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Action Failed",Toast.LENGTH_SHORT).show();

                    }
                });

    }

    //inflate menu items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

        menu.findItem(R.id.action_add_post).setVisible(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
         super.onCreateOptionsMenu(menu, inflater);
    }

    //handle option clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_logout:{
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }



}
