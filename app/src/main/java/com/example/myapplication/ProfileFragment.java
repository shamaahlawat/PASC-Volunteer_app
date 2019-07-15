package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DocumentReference mRef;

    private static final String NAME = "name";
    private static final String YEAR = "year";
    private static final String DEPT = "dept";

    ImageView profilePic;
    TextView name,dept,year;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(user.getEmail());

        profilePic = view.findViewById(R.id.profilePicture);
        name = view.findViewById(R.id.name);
        dept = view.findViewById(R.id.dept);
        year = view.findViewById(R.id.year);

        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ModelUsers md = task.getResult().toObject(ModelUsers.class);
                if(md!=null) {
                    name.setText(md.name);
                    dept.setText(md.dept);
                    year.setText(md.year);
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
