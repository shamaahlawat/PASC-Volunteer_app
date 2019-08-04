/*package com.example.myapplication;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
/*public class HomeFragment extends Fragment {

    RecyclerView P_recyclerView;
    RecyclerView.Adapter P_adapter;
    ArrayList<ModelPosts> postsList;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
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

        //P_adapter = new PostAdapter(postsList);
        //P_recyclerView.setAdapter(P_adapter);

        getAllPosts();

        return view;

    }

    private void getAllPosts() {


        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                currentUser = new ModelUsers(document.get("name").toString(),document.get("year").toString(),
//                        document.get("dept").toString(),document.get("domain1").toString(),document.get("domain2").toString(),
//                        document.get("domain3").toString(),document.get("github").toString(),document.get("linkedin").toString());
                final ModelUsers currentUser = task.getResult().toObject(ModelUsers.class);
                currentUser.addnew();
//                Toast.makeText(getActivity(), currentUser.toString(), Toast.LENGTH_SHORT).show();

                db.collection("Post").whereArrayContains("selectedYear", currentUser.getYear())
                        .orderBy("Timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //ModelPosts md = document.toObject(ModelPosts.class);
                                ModelPosts md=new ModelPosts(document.get("Title").toString(),document.get("Description").toString(),document.get("Date").toString(),document.get("Time").toString());

                                if(true) {
                                    //usersList.add(md);
                                    postsList.add(new ModelPosts(md.title,md.description,md.date,md.time));
                                }


                                //P_adapter = new PostAdapter(postsList);
                                //P_recyclerView.setAdapter(P_adapter);
                            P_adapter = new PostAdapter(postsList, );
                            P_recyclerView.setAdapter(P_adapter);

                        }


                       // Toast.makeText(getActivity(), "Action Failed", Toast.LENGTH_SHORT).show();
                    }


                }

                //            Toast.makeText(getActivity(), "action done!!", Toast.LENGTH_SHORT).show();


        });
    }
    private void SearchTextPost(final String s){

        db.collection("Post").orderBy("Timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        postsList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("Title").toString().toLowerCase().contains(s.toLowerCase())
                                        || document.get("userId").toString().toLowerCase().contains(s.toLowerCase())) {
                                    ModelPosts md = new ModelPosts(document.get("Title").toString(),
                                            document.get("Description").toString(), document.get("Date").toString(), document.get("Time").toString());
                                    postsList.add(md);
                                }
                                //P_adapter = new PostAdapter(postsList);
                               // P_recyclerView.setAdapter(P_adapter);
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

        menu.findItem(R.id.action_edit).setVisible(false);
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
            case R.id.action_add_post:{
                getActivity().finish();
                startActivity(new Intent(getActivity(), AddPostActivity.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
*/


package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

        //P_adapter = new PostAdapter(postsList);
        //P_recyclerView.setAdapter(P_adapter);

        getAllPosts();

        return view;

    }

    private void getAllPosts() {


        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                currentUser = new ModelUsers(document.get("name").toString(),document.get("year").toString(),
//                        document.get("dept").toString(),document.get("domain1").toString(),document.get("domain2").toString(),
//                        document.get("domain3").toString(),document.get("github").toString(),document.get("linkedin").toString());
                final ModelUsers currentUser = task.getResult().toObject(ModelUsers.class);
                currentUser.addnew();
//                Toast.makeText(getActivity(), currentUser.toString(), Toast.LENGTH_SHORT).show();

                db.collection("Post").whereArrayContains("selectedYear", currentUser.getYear())
                        .orderBy("Timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ModelPosts md = new ModelPosts(document.get("Title").toString(), document.get("Description").toString(),
                                    document.get("Date").toString(), document.get("Time").toString());  //sanam


                            if (((ArrayList<String>) document.get("selectedDept")).contains(currentUser.getDept()) &&
                                    (((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain1()) ||
                                            ((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain2()) ||
                                            ((ArrayList<String>) document.get("selectedDomain")).contains(currentUser.getDomain3()))) {

                                postsList.add(new ModelPosts(md.title, md.description, md.date, md.time));
                            }


                            P_adapter = new PostAdapter(postsList, (PopupMenu.OnMenuItemClickListener) getActivity());
                            P_recyclerView.setAdapter(P_adapter);

                        }


                        // Toast.makeText(getActivity(), "Action Failed", Toast.LENGTH_SHORT).show();
                    }


                });

                //            Toast.makeText(getActivity(), "action done!!", Toast.LENGTH_SHORT).show();

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
                                            document.get("Description").toString(), document.get("Date").toString(), document.get("Time").toString());
                                    postsList.add(md);  //sanam
                                }
                                P_adapter = new PostAdapter(postsList, (PopupMenu.OnMenuItemClickListener) getActivity());
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

}