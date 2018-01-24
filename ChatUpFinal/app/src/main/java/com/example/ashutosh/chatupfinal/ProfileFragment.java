package com.example.ashutosh.chatupfinal;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.transition.Fade;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ashutosh.chatupfinal.animation.DetailsTransition;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.example.ashutosh.chatupfinal.utils.DatabaseUtility;
import com.example.ashutosh.chatupfinal.utils.Utility;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ImageView backBtn;
    CircleImageView profileImage;
    ImageView cameraBtn,backProfileBtn,zoomedImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,SELECT_COVER_FILE=2;
    private String userChoosenTask,userChosenCoverTask;
    boolean isImageFitToScreen;
    RelativeLayout changer,profileShower,changer2;

    FirebaseApp app;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference mDatabaseRef;
    //Firebase reference;
    StorageReference storageReference;

    String phoneOriginalNumber;
    ProgressDialog mProgressDialog;
    boolean result,result2;

    static boolean isCalledFirst=false;

    Uri profileImageUri=null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        mAuth = FirebaseAuth.getInstance();

        storageReference=FirebaseStorage.getInstance().getReference();

        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("users");

        phoneOriginalNumber= MyPreferenceManager.getUserDetail(getActivity()).getPhoneNumber();
        mProgressDialog=new ProgressDialog(getActivity());

        result=Utility.checkPermission(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        backBtn=(ImageView)view.findViewById(R.id.back_btn);
        profileImage=(CircleImageView)view.findViewById(R.id.civProfilePic);
        cameraBtn=(ImageView)view.findViewById(R.id.imgAddBtn);
        changer=(RelativeLayout)view.findViewById(R.id.changer_rl);
        profileShower=(RelativeLayout)view.findViewById(R.id.profile_shower_rl);
        changer2=(RelativeLayout)view.findViewById(R.id.changer2_rl);
        backProfileBtn=(ImageView)view.findViewById(R.id.back_profile_btn);
        zoomedImage=(ImageView)view.findViewById(R.id.profile_zoomed);

        View photoHeader = view.findViewById(R.id.photoHeader);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* For devices equal or higher than lollipop set the translation above everything else */
            photoHeader.setTranslationZ(6);
            /* Redraw the view to show the translation */
            photoHeader.invalidate();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changer.setVisibility(View.INVISIBLE);
                changer2.setVisibility(View.GONE);
                profileShower.setVisibility(View.VISIBLE);
                changer.setBackgroundColor(Color.parseColor("#000000"));
                //zoomedImage.setImageDrawable(getResources().getDrawable(R.drawable.default_profile_image));
            }
        });

        backProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileShower.setVisibility(View.GONE);
                changer2.setVisibility(View.VISIBLE);
                changer.setBackground(getResources().getDrawable(R.drawable.background));
            }
        });


    }

    public void onBackPressed()
    {
        Intent intent=new Intent(getActivity(),BaseDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    else if(userChoosenTask.equals("Choose from Library"))
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    //code for deny
                }
                break;
            case Utility.MY_PERMISSIONS_REQUEST_CAMERA:
                if(grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    cameraIntent();
                }
                else
                {
                    Toast.makeText(getActivity(), "Not Granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result) {
                        result2 = Utility.checkCameraPermission(getActivity());
                        if (result2)
                            cameraIntent();
                    }
                    else
                        Toast.makeText(getActivity(), "Permissions are not given!", Toast.LENGTH_SHORT).show();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data,SELECT_FILE);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        //profileImageUri = getImageUri(getActivity(),thumbnail);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        profileImageUri = Uri.fromFile(destination);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileImage.setImageBitmap(thumbnail);

        uploadFile();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data,int type) {

        profileImageUri=data.getData();

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type==SELECT_FILE)
             profileImage.setImageBitmap(bm);

        uploadFile();

    }

    public void uploadFile()
    {
        if(profileImageUri!=null)
        {
            mAuth.getCurrentUser();
            final ProgressDialog progress=new ProgressDialog(getActivity());
            progress.setTitle("Uploading..");
            progress.show();
            StorageReference riversRef = storageReference.child("profile/"+phoneOriginalNumber+".jpg");

            riversRef.putFile(profileImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            mDatabaseRef.child(phoneOriginalNumber).child("profile_url").setValue(downloadUrl.toString());
                            progress.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double pr=(100.00*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progress.setMessage((int)pr + "% Completed");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
        else
        {
            Toast.makeText(getActivity(), "Image Not Found", Toast.LENGTH_SHORT).show();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
