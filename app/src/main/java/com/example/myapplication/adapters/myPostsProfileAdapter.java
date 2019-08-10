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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.classes.ModelPosts;
import com.example.myapplication.interfaces.ItemClickListener;
import com.google.android.material.resources.TextAppearance;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.myapplication.R.style.TextAppearance_AppCompat_Medium;

public class myPostsProfileAdapter extends RecyclerView.Adapter<myPostsProfileAdapter.ViewHolder> {
    PopUpEventListener p;
private ArrayList<ModelPosts> PostList;
public static Context context;


public myPostsProfileAdapter(ArrayList<ModelPosts> postList, PopUpEventListener p) {
        this.p = p;
        PostList = postList;

        Log.d("POSTADAPTER", "CONSTR");
//    Toast.makeText(context, "func cnstr", Toast.LENGTH_SHORT).show();
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


final ModelPosts currentPost = PostList.get(position);
        holder.textView1.setText(currentPost.getTitle());
        holder.textView5.setText(currentPost.getDescription());
        holder.textView2.setVisibility(View.GONE);
        //nayan add author image code here
        holder.imageView2.setImageResource(R.drawable.ic_default_img);
        String ago=null;
        try
        {
        Date past = currentPost.getTimeStamp().toDate();
        Log.d("Timestamp ", past.toString());
        Date now = new Date();

        long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
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
        else if(days<7)
        {
        ago = (days+" days ago");
        }
        else{
            ago = past.toString();
        }
        }
        catch (Exception j){
        j.printStackTrace();
        }

        holder.textView4.setText(ago);

    if(currentPost.getType().equals("UPDATE")){
        holder.ll.setVisibility(View.GONE);
//            holder.interestedCheckBox.setVisibility(View.GONE);
    }

    Log.d("POSTADAPTER", "SET VIEW");
    // Toast.makeText(context, "func view", Toast.LENGTH_SHORT).show();


        if(!currentPost.getOwnerOfPost().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())){
            holder.textView6.setVisibility(View.GONE);
        }else {
            holder.textView6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popup = new PopupMenu(context, holder.textView6);
                    popup.inflate(R.menu.option);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.EditPost: {
                                    p.onEdit(currentPost.getDescription(), currentPost.getId());
                                    break;
                                }
                                case R.id.ViewList: {
                                    p.onViewList(currentPost.getId());
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });


        }

        }

public interface PopUpEventListener {

    void onViewList(String docid);
    void onEdit(String description, String id);

}

    @NonNull
    @Override
    public myPostsProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_template, parent, false);
        return new myPostsProfileAdapter.ViewHolder(v);
    }

    @Override

    public int getItemCount() {
        return PostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView2;
        public TextView textView1;
        public TextView textView2;

        public TextView textView4;
        public TextView textView5;
        public ImageView textView6;
        //        public CheckBox interestedCheckBox;
        public Button interestedButton, notinterestedButton;
        LinearLayout ll;



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

}
}


