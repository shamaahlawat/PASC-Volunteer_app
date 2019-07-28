package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class PersonalDetailsFragment extends Fragment {


    TextView next;
    EditText name, github, linkedin;
    Spinner deptSpinner, yearSpinner;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    PersonalDetailsFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);

        next = (TextView)view.findViewById(R.id.next);
        name = (EditText)view. findViewById(R.id.name);
        github = (EditText)view. findViewById(R.id.github);
        linkedin = (EditText)view. findViewById(R.id.linkedin);
        deptSpinner = (Spinner)view.findViewById(R.id.deptSpinner);
        yearSpinner = (Spinner)view.findViewById(R.id.yearSpinner);

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.dept_list));
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(deptAdapter);

        ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year_list));
        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearArrayAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        onNextClicked();


        return view;
    }

    private void onNextClicked() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name.getText().toString().trim());
                map.put("year",yearSpinner.getSelectedItem().toString().trim());
                map.put("dept",deptSpinner.getSelectedItem().toString().toUpperCase().trim());
                map.put("github",github.getText().toString().trim());
                map.put("linkedin",linkedin.getText().toString().trim());



                // add entry to firestore
                db.collection("User").document(firebaseAuth.getCurrentUser().getEmail().toString())
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                        TechnicalChoiceFragment fragment1 = new TechnicalChoiceFragment();
                        FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, fragment1, "fragment_technical_choice");
                        ft1.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
