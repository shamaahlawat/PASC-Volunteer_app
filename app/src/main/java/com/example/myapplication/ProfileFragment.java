package com.example.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
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

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(user.getEmail());

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        domainList = new ArrayList<>();
        domainList.add(new domain(R.drawable.linkedin1, "Android"));
        domainList.add(new domain(R.drawable.linkedin1, "Web"));
        domainList.add(new domain(R.drawable.linkedin1, "Android"));
        domainList.add(new domain(R.drawable.linkedin1, "Web"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);

        profile_fragment dom = new profile_fragment(getActivity(),domainList);

        recyclerView.setAdapter(dom);

        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ModelUsers md = task.getResult().toObject(ModelUsers.class);
                if(md!=null) {
                    name.setText(md.getName());
                    dept.setText(md.getDept());
                    year.setText(md.getYear());
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


}
