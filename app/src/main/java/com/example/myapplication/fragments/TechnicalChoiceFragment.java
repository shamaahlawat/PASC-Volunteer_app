package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class TechnicalChoiceFragment extends Fragment {

    TextView d1,d2,d3;
    Spinner domainSpinner;
    TextView done, prev;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_technical_choice, container, false);

        d1 = view.findViewById(R.id.domain1);
        d2 = view.findViewById(R.id.domain2);
        d3 = view.findViewById(R.id.domain3);
        done = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        domainSpinner = view.findViewById(R.id.domainSpinner);

        ArrayAdapter<String> domainArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.domain_list));
        domainArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainSpinner.setAdapter(domainArrayAdapter);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                ModelUsers md = task.getResult().toObject(ModelUsers.class);
                if(md!=null && d1!=null && d2!=null &&d3!=null){
                    d1.setText(md.getDomain1());
                    d2.setText(md.getDomain2());
                    d3.setText(md.getDomain3());
                }
            }
        });
        db = FirebaseFirestore.getInstance();



        domainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(d1.getText().equals("")){
                    if(domainSpinner.getSelectedItem().toString().equals(d2.getText()) ||
                            domainSpinner.getSelectedItem().toString().equals(d3.getText())){
                        Toast.makeText(getActivity(), "Please choose different domains.", Toast.LENGTH_SHORT).show();
                    }else {
                        d1.setText(domainSpinner.getSelectedItem().toString());
                    }
                }
                else if(d2.getText().equals("")){
                    if(domainSpinner.getSelectedItem().toString().equals(d1.getText()) ||
                            domainSpinner.getSelectedItem().toString().equals(d3.getText())){
                        Toast.makeText(getActivity(), "Please choose different domains.", Toast.LENGTH_SHORT).show();
                    }else {
                        d2.setText(domainSpinner.getSelectedItem().toString());
                    }
                }
                else if(d3.getText().equals("")){
                    if(domainSpinner.getSelectedItem().toString().equals(d1.getText()) ||
                            domainSpinner.getSelectedItem().toString().equals(d2.getText())){
                        Toast.makeText(getActivity(), "Please choose different domains.", Toast.LENGTH_SHORT).show();
                    }else {
                        d3.setText(domainSpinner.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        d1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                d1.setText("");
                return false;
            }
        });
        d2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                d2.setText("");
                return false;
            }
        });
        d3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                d3.setText("");
                return false;
            }
        });

        onNextClicked();

        return view;
    }

    TechnicalChoiceFragment(){

    }

    private void onNextClicked() {

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("domain1",d1.getText().toString().trim());
                map.put("domain2",d2.getText().toString().trim());
                map.put("domain3",d3.getText().toString().toUpperCase().trim());


                // add entry to firestore
                db.collection("User").document(firebaseAuth.getCurrentUser().getEmail().toString())
                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), DashboardActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalDetailsFragment fragment1 = new PersonalDetailsFragment();
                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.content, fragment1, "fragment_personal_details");
                ft1.commit();
            }
        });




    }

}
