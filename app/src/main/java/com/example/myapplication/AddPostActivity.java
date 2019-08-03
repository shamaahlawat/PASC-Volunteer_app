package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.classes.TimePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText post_title;
    private EditText post_description;
    private Button save_post, select_users;

    String Title,Description,Date,Time;
    String[] listItems;
    ArrayList<String> dept_user, year_user, domain_user;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        post_title = findViewById(R.id.nameET);
        post_description = findViewById(R.id.DescET);
        save_post = findViewById(R.id.Save);

        select_users = findViewById(R.id.selectMultipleUsers);

        dept_user = new ArrayList<>();
        year_user = new ArrayList<>();
        domain_user = new ArrayList<>();

        Button post_date = findViewById(R.id.DatePicker);
        post_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.myapplication.classes.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        Button post_time = findViewById(R.id.TimePicker);
        post_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        db = FirebaseFirestore.getInstance();

        onSelectUsersClicked();
        onSavePostClicked();



    }

    private void onSelectUsersClicked() {
        mUserItems.clear();
        select_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddPostActivity.this);
                mBuilder.setTitle("departments");
                listItems = getResources().getStringArray(R.array.dept_list);
                checkedItems = new boolean[listItems.length];
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            dept_user.add(listItems[mUserItems.get(i)]);
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        selectUsersBasedOnYear();
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < listItems.length; i++) {
                            checkedItems[i] = true;
                            dept_user.add(listItems[i]);
                        }selectUsersBasedOnYear();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void selectUsersBasedOnYear() {
        mUserItems.clear();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddPostActivity.this);
        listItems = getResources().getStringArray(R.array.year_list);
        checkedItems = new boolean[listItems.length];
        mBuilder.setTitle("Year");
        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    year_user.add(listItems[mUserItems.get(i)]);
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                selectUsersBasedOnDomain();
                Toast.makeText(AddPostActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        mBuilder.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("select all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < listItems.length; i++) {
                    checkedItems[i] = true;
                    year_user.add(listItems[i]);
                }selectUsersBasedOnDomain();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }

    private void selectUsersBasedOnDomain() {
        mUserItems.clear();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddPostActivity.this);
        listItems = getResources().getStringArray(R.array.domain_list);
        checkedItems = new boolean[listItems.length];
        mBuilder.setTitle("Domains");
        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    domain_user.add(listItems[mUserItems.get(i)]);
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                Toast.makeText(AddPostActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        mBuilder.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("select all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < listItems.length; i++) {
                    checkedItems[i] = true;
                    domain_user.add(listItems[i]);
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        Time = DateFormat.getTimeInstance().format(c.getTime());
    }

    public void onSavePostClicked(){
        save_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Date = post_date.getText().toString().trim();
                Title = post_title.getText().toString().trim();
                Description = post_description.getText().toString().trim();

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                //add values to the resp fields
                Map<String, Object> map = new HashMap<>();
                map.put("Title", Title);
                map.put("Description",Description);
                map.put("Date",Date);
                map.put("Time",Time);
                map.put("Timestamp", FieldValue.serverTimestamp());
                map.put("userId",email);
                map.put("selectedYear", year_user);
                map.put("selectedDept", dept_user);
                map.put("selectedDomain", domain_user);
                if(Title.isEmpty() || Description.isEmpty() || Date == null || Time == null || year_user.isEmpty() || dept_user.isEmpty() || domain_user.isEmpty()) {
                    Toast.makeText(AddPostActivity.this,"Enter all details",Toast.LENGTH_SHORT).show();
                    return;
                }

                // add entry to firestore
                db.collection("Post").document()
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddPostActivity.this, "Post updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPostActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                startActivity(new Intent(AddPostActivity.this, DashboardActivity.class));



            }
        });

    }


}
