package com.example.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import id.zelory.compressor.Compressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DocumentReference mRef;
    FirebaseStorage storage;


    private ImageView userImage;
    private Uri imageUri = null;
    private Bitmap compressed;
    private StorageReference storageReference, storageReference1;


    ArrayList <domain> domainList;

    RecyclerView recyclerView;

    private static final String NAME = "name";
    private static final String YEAR = "year";
    private static final String DEPT = "dept";

    private String imgname;

    ImageView profilePic;
    TextView name,dept,year,linkedin,git;



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
        final View view = inflater.inflate(R.layout.profile, container, false);
        setHasOptionsMenu(true);

        userImage = (ImageView)view.findViewById(R.id.avatarIv);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("User").document(user.getEmail());
        storageReference = FirebaseStorage.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        name = view.findViewById(R.id.name);
        year = view.findViewById(R.id.year);
        dept = view.findViewById(R.id.dept);
        linkedin = view.findViewById(R.id.linkedin);
        git = view.findViewById(R.id.github);

        domainList = new ArrayList<>();
        userImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Select im", Toast.LENGTH_LONG);

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    Toast.makeText(getContext(), "Select", Toast.LENGTH_LONG);
                    choseImage();

                }
            }
        });

        FirebaseFirestore.getInstance().collection("User").document(firebaseAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                ModelUsers md = task.getResult().toObject(ModelUsers.class);
                md.addnew();
                Iterator<String> itr = md.dom.iterator();
                Log.d("while11", "onComplete: entered");
                while (itr.hasNext()) {
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
                if (md != null) {
                    name.setText(md.getName());
                    dept.setText(md.getDept());
                    year.setText(md.getYear());
                    linkedin.setText(md.getLinkedin());
                    git.setText(md.getGithub());
                    imgname = md.getName();
                    storage = FirebaseStorage.getInstance();

                    String image = storageReference.child("avatarIv").toString();
                    storageReference1 = storage.getReferenceFromUrl(image).child(imgname + ".jpg");

                    try {
                        final File localFile = File.createTempFile("userImage", "jpg");
                        storageReference1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                userImage.setImageBitmap(bitmap);
                                Log.d("falll", "onFailure: ");


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d("failll", "onFailure: ");
                            }
                        });
                    } catch (IOException e ) {}

                    Log.d("imgggg", "onComplete: "+image);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    recyclerView.setLayoutManager(rvLiLayoutManager);

                    profile_fragment dom = new profile_fragment(getActivity(), domainList);

                    recyclerView.setAdapter(dom);

                }

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(imageUri!=null){
            Log.d("nullnull", "onResume: ");
            File newFile = new File(getPath(getContext(),imageUri));
            try {

                compressed = new Compressor(getContext())
                        .setMaxHeight(125)
                        .setMaxWidth(125)
                        .setQuality(50)
                        .compressToBitmap(newFile);
                Log.d("compresse", "onCreateView: ");

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error11", "onResume: "+e);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] thumbData = byteArrayOutputStream.toByteArray();
            UploadTask image_path = storageReference.child("avatarIv").child(imgname + ".jpg").putBytes(thumbData);
            image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storeData(task);

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }


                private void storeData(Task<UploadTask.TaskSnapshot> task) {
                    Uri download_uri;


                        download_uri = imageUri;



                    Map<String, String> userData = new HashMap<>();
                    userData.put("userImage", download_uri.toString());

                    firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getEmail()).set(userData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "User Data is Stored Successfully", Toast.LENGTH_LONG).show();

                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }



    private void choseImage() {

      /*  Log.d("ci111", "choseImage: ");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*/
       // Intent data = Intent.createChooser(intent, "Select Picture");
       // imageUri = data.getData();


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getContext(),ProfileFragment.this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onactivity", "onActivityResult: ");
        if (resultCode == RESULT_OK)
        {
           /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                imageUri = data.getData();
            }
            if (imageUri != null) {
                // Get the path from the Uri
                Log.d("ifnull", "onActivityResult: ");
                userImage.setImageURI(null);
                userImage.setImageURI(imageUri);
            }
            Toast.makeText(getContext(), imageUri.toString(), Toast.LENGTH_SHORT).show();*/
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    imageUri = result.getUri();
                    userImage.setImageURI(imageUri);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();

                }
            }


        }
    }
           /* CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                userImage.setImageURI(imageUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getContext(),"image not added", Toast.LENGTH_SHORT);
            }*/




    public static String getPath(final Context context, final Uri uri) {


        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }


}
