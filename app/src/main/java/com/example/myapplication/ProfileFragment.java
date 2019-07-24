package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DocumentReference mRef;


    ArrayList <domain> domainList;

    RecyclerView recyclerView;

    private static final String NAME = "name";
    private static final String YEAR = "year";
    private static final String DEPT = "dept";

    ImageView profilePic;
    TextView name,dept,year;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile, container, false);
        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(user.getEmail());

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        name = view.findViewById(R.id.name);
        year = view.findViewById(R.id.year);
        dept = view.findViewById(R.id.dept);

        domainList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
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
                    name.setText(md.getName());
                    dept.setText(md.getDept());
                    year.setText(md.getYear());



                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    recyclerView.setLayoutManager(rvLiLayoutManager);

                    profile_fragment dom = new profile_fragment(getActivity(),domainList);

                    recyclerView.setAdapter(dom);
                     /*
                    try{
                    //if image is received, then load
                        Picasso.get().load(image).into(profilePic);
                    }catch (Exception e){
                        //if error occurs, then set default
                        Picasso.get().load(R.drawable.ic_addphoto_white).into(avatar);
                    }
                    */
                }
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
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
            }
            case R.id.action_add_post:{

                startActivity(new Intent(getActivity(), AddPostActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
