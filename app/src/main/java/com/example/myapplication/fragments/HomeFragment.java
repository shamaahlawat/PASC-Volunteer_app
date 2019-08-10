package com.example.myapplication.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.AddPostActivity;
import com.example.myapplication.DashboardActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.ViewListActivity;
import com.example.myapplication.adapters.PostAdapter;
import com.example.myapplication.R;
import com.example.myapplication.classes.ModelPosts;
import com.example.myapplication.classes.ModelUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

public class HomeFragment extends Fragment implements PostAdapter.PopUpEventListener {

    RecyclerView P_recyclerView;
    RecyclerView.Adapter P_adapter;
    ArrayList<ModelPosts> postsList;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ModelUsers currentUser;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        postsList = new ArrayList<ModelPosts>();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        P_recyclerView = view.findViewById(R.id.posts_recycledView);
        P_recyclerView.setHasFixedSize(true);
        P_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        P_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && DashboardActivity.navigationView.isShown()) {
                    DashboardActivity.navigationView.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    DashboardActivity.navigationView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

                getAllPosts();

        return view;

    }

    private void getAllPosts() {


        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final ModelUsers currentUser = task.getResult().toObject(ModelUsers.class);
                currentUser.addnew();
                db.collection("Post").whereArrayContains("selectedYear", currentUser.getYear())
                        .orderBy("Timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ModelPosts md = new ModelPosts(document.get("Title").toString(), document.get("Description").toString(),
                                    document.get("Date").toString(), document.get("Time").toString(), document.get("Type").toString(),
                                    document.get("OwnerOfPost").toString(), document.get("id").toString());  //sanam
                            md.setTimeStamp((Timestamp) document.get("Timestamp"));

                            if (((ArrayList<String>) document.get("selectedDept")).contains(currentUser.getDept()) &&
                                    (((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain1()) ||
                                            ((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain2()) ||
                                            ((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain3()))) {

                                postsList.add(md);
                            }


                            P_adapter = new PostAdapter(postsList, HomeFragment.this);
                            P_recyclerView.setAdapter(P_adapter);

                        }


                    }


                });


            }
        });
    }

    private void SearchTextPost(final String s) {

        db.collection("Post").orderBy("Timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        postsList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("Title").toString().toLowerCase().contains(s.toLowerCase())
                                        || document.get("userId").toString().toLowerCase().contains(s.toLowerCase())) {
                                    ModelPosts md = new ModelPosts(document.get("Title").toString(),
                                            document.get("Description").toString(), document.get("Date").toString(), document.get("Time").toString(), document.get("Type").toString(), document.get("OwnerOfPost").toString(), document.get("id").toString());
                                    md.setTimeStamp((Timestamp)document.get("Timestamp"));
                                    postsList.add(md);  //sanam
                                }
                                P_adapter = new PostAdapter(postsList, HomeFragment.this);
                                P_recyclerView.setAdapter(P_adapter);
                            }

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Action Failed", Toast.LENGTH_SHORT).show();

                    }
                });

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTextPost(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

        //menu.findItem(R.id.action_edit).setVisible(false);
    }

    //handle option clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout: {
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            }
            case R.id.action_add_post: {
                getActivity().finish();
                startActivity(new Intent(getActivity(), AddPostActivity.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEdit(String desc, final String docid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("EDIT : ");
        final EditText et = new EditText(getContext());
        et.setText(desc);
        builder.setView(et);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newdesc = et.getText().toString();
                DocumentReference dr = db.collection("Post").document(docid);
                dr.update("Description", newdesc);
                Toast.makeText(getContext(), "Successfull", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);
    }


    @Override
    public void onViewList(String docid) {

        Intent intent = new Intent(getActivity(), ViewListActivity.class);
        intent.putExtra("POST_ID", docid);
        startActivity(intent);
    }
}