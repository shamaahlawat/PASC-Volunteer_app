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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    TextView data;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        //data = view.findViewById(R.id.data);

        usersList = new ArrayList<ModelUsers>();

        recyclerView = view.findViewById(R.id.users_recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllUsers();

        return view;
    }

   private void getAllUsers() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       FirebaseFirestore.getInstance().collection("User").get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ModelUsers md = document.toObject(ModelUsers.class);
                        //md=new ModelUsers(document.get("name").toString(),document.get("year").toString(),document.get("dept").toString());

                        if(!user.getEmail().equals(document.getId())) {
                            //usersList.add(md);
                            usersList.add(new ModelUsers(md.name,md.year,md.dept));
                            Log.d("AAAAAAAAAAAAAAAAA", md.toString());

                        }

                        //data.setText(usersList.toString());

                        Adapter = new ExampleAdapter(usersList);
                        recyclerView.setAdapter(Adapter);

                    }

                } else {

                    Toast.makeText(getActivity(), "Action Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
