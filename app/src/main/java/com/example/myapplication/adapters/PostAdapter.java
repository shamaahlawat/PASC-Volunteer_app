package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.interfaces.ItemClickListener;
import com.example.myapplication.R;
import com.example.myapplication.classes.ModelPosts;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    PopUpEventListener p;
    private ArrayList<ModelPosts> PostList;
    public Context mCntxt;
    public static Context context;
    String name;
    Boolean flag=false;
    private String imgname;


    public String nameOfInterested = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference1, storageReference = FirebaseStorage.getInstance().getReference();


    public PostAdapter(ArrayList<ModelPosts> postList, PopUpEventListener p) {

        this.p = p;
        PostList = postList;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final ModelPosts currentPost = PostList.get(position);
        holder.textView1.setText(currentPost.getTitle());
        holder.textView2.setText("@" + currentPost.getOwnerOfPost());
        holder.textView5.setText(currentPost.getDescription());

        //add correct code for using author image



//        db.collection("User").document(nameOfInterested).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot doc = task.getResult();
//                if(doc.get("Image") != null){
//                    imgname = doc.get("name").toString();
//                    flag = true;
//                }
//                else
//                    flag=false;
//            }
//        });
//
//
//        if(flag) {
//            final String image = storageReference.child("avatarIv").toString();
//            if (!image.equals("gs://fire-demo-1311.appspot.com/avatarIv")) {
//                storageReference1 = storage.getReferenceFromUrl(image).child(imgname + ".jpg");
//
//                try {
//                    final File localFile = File.createTempFile("AuthorImage", "jpg");
//                    storageReference1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
//                            holder.imageView2.setImageBitmap(bitmap);
//                            Log.d("falll", "onFailure: ");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            Log.d("failll", "onFailure: ");
//                        }
//                    });
//                } catch (IOException e) {
//                }
//
//                Log.d("imgggg", "onComplete: " + image);
//            }
//        }
//        else


        holder.imageView2.setImageResource(R.drawable.ic_default_img);
        holder.imageView2.setMaxWidth(65);
        holder.imageView2.setMaxHeight(65);


        String ago=null;
        try
        {
            //SimpleDateFormat format = new SimpleDateFormat();
            Date past = currentPost.getTimeStamp().toDate();
            Log.d("TImestamp ", past.toString());
            Date now = new Date();

                long seconds=TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                if(seconds<60)
                {
                    ago = (seconds+" seconds ago");
                }
                else if(minutes<60)
                {
                    ago = (minutes+" minutes ago");
                }
                else if(hours<24)
                {
                    ago = (hours+" hours ago");
                }
                else
                {
                   ago = (days+" days ago");
                }
            }
            catch (Exception j){
                j.printStackTrace();
            }

        holder.textView4.setText(ago);


            holder.textView6.setVisibility(View.GONE);


        holder.interestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                db.collection("User").document(nameOfInterested).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        name  = task.getResult().get("name").toString();
                    }
                });
                                   Map<String, Object> map2 = new HashMap<>();
                    map2.put("name", name);

                    db.collection("Post").document(currentPost.getId()).collection("Interested People").document(nameOfInterested)
                            .set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "name added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });


            }
        });

        holder.notinterestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                db.collection("User").document(nameOfInterested).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        name  = task.getResult().get("name").toString();
                    }
                });
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", name);

                db.collection("Post").document(currentPost.getId()).collection("Interested People").document(nameOfInterested)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "data updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onItemClick(final View v, int pos) {
//
//                CheckBox chk = (CheckBox) v;
//                db.collection("User").document(nameOfInterested).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        name  = task.getResult().get("name").toString();
//                    }
//                });
//                if (chk.isChecked()) {
//                    Map<String, Object> map2 = new HashMap<>();
//                    map2.put("name", name);
//
//                    db.collection("Post").document(currentPost.getId()).collection("Interested People").document(nameOfInterested)
//                            .set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(v.getContext(), "name added", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } else if (!chk.isChecked()) {
//
//                    db.collection("Post").document(currentPost.getId()).collection("Interested People").document(nameOfInterested)
//                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(v.getContext(), "name removed", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//
//
//
//            }
//        });

        if(currentPost.getType().equals("UPDATE")){
            holder.ll.setVisibility(View.GONE);
//            holder.interestedCheckBox.setVisibility(View.GONE);
        }

    }

    public interface PopUpEventListener {

        void onViewList(String docid);

        void onEdit(String description, String id);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_template, parent, false);
        return new PostAdapter.ViewHolder(v);
    }

    @Override

    public int getItemCount() {
        return PostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView2;
        public TextView textView1;
        public TextView textView2;
        public TextView textView4;
        public TextView textView5;
        public ImageView textView6;
//        public CheckBox interestedCheckBox;
        public Button interestedButton, notinterestedButton;
        LinearLayout ll;
        ItemClickListener itemClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            interestedCheckBox = itemView.findViewById(R.id.interestedCB);
            interestedButton = itemView.findViewById(R.id.interestedButton);
            notinterestedButton = itemView.findViewById(R.id.notInterestedButton);
            textView1 = itemView.findViewById(R.id.textView_title);
            textView2 = itemView.findViewById(R.id.textView_owner);
            textView4 = itemView.findViewById(R.id.textView_time);
            textView5 = itemView.findViewById(R.id.textView_description);
            textView6 = itemView.findViewById(R.id.textView_options);
            imageView2 = itemView.findViewById(R.id.AuthorImage);
            ll = itemView.findViewById(R.id.ll4);
            context = itemView.getContext();
 //           interestedCheckBox.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
