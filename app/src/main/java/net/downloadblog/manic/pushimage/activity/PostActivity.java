package net.downloadblog.manic.pushimage.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.ServerValue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.common.BaseActivity;
import net.downloadblog.manic.pushimage.object.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//http://stackoverflow.com/questions/11292950/push-up-content-when-clicking-in-edit-text
public class PostActivity extends AppCompatActivity {

    ImageView imvPhoto;
    ImageButton imbChoosephoto, imbTakephoto, imbKeyboard, imbLocation;
    EditText edtContent, edtLocation;
    ImageButton imbShare;
    private static final int SELECT_PHOTO = 100;
    private static final int REQUEST_IMAGE_CAPTURE=200;
    String check = "";
    private File mPhotoFile;
    String mCurrentPhotoPath;

    private Toolbar toolbar;
    Uri url_photo;
    Uri uri_resize;
    Uri toImage;

    String getUrlPhoto;

    UploadTask uploadTask;
    private FirebaseAuth mAuth;
    public static final String firebase = "gs://pushimage-e8256.appspot.com";
    private DatabaseReference mDatabase;
    private DatabaseReference mDataPost;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;

    private String temp_key;

    LocationManager manager;
    //Uri photo_url= Uri.parse("");

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        findView();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference().child(ImageEntity.class.getSimpleName());
        mDatabase = FirebaseDatabase.getInstance().getReference();



         bundle=getIntent().getBundleExtra("data");
        if (bundle != null) {
            String dataContent = bundle.getString("dataContent");
            String dataLocation = bundle.getString("dataLocation");
            String dataPhoto = bundle.getString("dataPhoto");
            String dataKey=bundle.getString("dataKey");
            edtContent.setText(dataContent);
            edtLocation.setText(dataLocation);
            edtLocation.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(dataPhoto)
                    .thumbnail(1f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imvPhoto);

            imbTakephoto.setEnabled(false);
            imbChoosephoto.setEnabled(false);


        }



        imbChoosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        imbTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        imbKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edtContent.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        imbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getContent= edtContent.getText().toString();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, toImage);
                shareIntent.putExtra(Intent.EXTRA_TEXT,getContent);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                shareIntent.setType("*/*");
                //shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

        imbLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
//
//                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//                    buildAlertMessageNoGps();
//                }
                GPSTracker gps = new GPSTracker(PostActivity.this);
                if (gps.canGetLocation()) {
                    edtLocation.setText(gps.getAddress());
                    edtLocation.setVisibility(View.VISIBLE);
                } else
                    gps.showSettingsAlert();
            }
        });

    }


    public void findView() {
        imbChoosephoto = (ImageButton) findViewById(R.id.imb_choosephoto);
        imbTakephoto = (ImageButton) findViewById(R.id.imb_takephoto);
        imbKeyboard = (ImageButton) findViewById(R.id.imb_keyboard);
        imvPhoto = (ImageView) findViewById(R.id.imv_photo);
        edtContent = (EditText) findViewById(R.id.edt_content);
        imbShare= (ImageButton)findViewById(R.id.imb_share);
        imbLocation=(ImageButton)findViewById(R.id.imb_location);
        edtLocation=(EditText)findViewById(R.id.edt_location);
    }

    private void updateData(String key, String content, String location){
        //String key = "-KZGCtQZZxYHlHRXokKv";
//        storageRef = storage.getReferenceFromUrl(firebase);
//        imagesRef = storageRef.child("images");
//
//        StorageReference riversRef = storageRef.child("images/" + uri_resize.getLastPathSegment());
//        uploadTask = riversRef.putFile(uri_resize);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                getUrlPhoto= downloadUrl.toString();
//
//            }
//        });

        mDatabase.child("posts").child(key).child("content").setValue(content);
        mDatabase.child("posts").child(key).child("location").setValue(location);
        //mDatabase.child("posts").child(key).child("photo").setValue(getUrlPhoto);
        finish();
    }

    private void post() {

        final String userId = getUid();
        //final String userName= getUsername();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e("ErrorUser: ", "User " + userId + " is unexpectedly null");
                            Toast.makeText(PostActivity.this,
                                    "Error: could not fetch user." + user.username,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            try {

                                writeNewPost(userId, user.username, edtContent.getText().toString(), uri_resize, edtLocation.getText().toString());
                                Log.e("locationxxx", "" + edtLocation.toString());
                            }
                            catch (Exception e){
                                Toast.makeText(PostActivity.this,"Please pick a photo!", Toast.LENGTH_LONG).show();
                            }
                        }

                        // Finish this Activity, back to the stream
                        //setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        //setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });


    }

    private void writeNewPost(final String iduser, final String username, final String content, final Uri uri_photo, final String location) {

        final BaseActivity baseActivity = new BaseActivity(this);
        baseActivity.showProgressDialog();

        storageRef = storage.getReferenceFromUrl(firebase);
        imagesRef = storageRef.child("images");

        //file = Uri.fromFile(new File("/storage/emulated/0/Download/dog.jpg"));
        StorageReference riversRef = storageRef.child("images/" + uri_resize.getLastPathSegment());
        uploadTask = riversRef.putFile(uri_resize);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                getUrlPhoto= downloadUrl.toString();

                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = mDatabase.push().getKey();
                mDatabase.updateChildren(map);

                DatabaseReference message_root = mDatabase.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("iduser", iduser);
                map2.put("user", username);
                map2.put("content", content);
                map2.put("photo", downloadUrl.toString());
                map2.put("time", ServerValue.TIMESTAMP);
                map2.put("starCount", 0);
                map2.put("key", temp_key);
                map2.put("myStarStatus", 0);
                map2.put("location", location);
                map2.put("flag",0);
                Map<String, Object> mapUserStar = new HashMap<String, Object>();
                //mapUserStar.put("hi","he");
//                ArrayList arrUser = new ArrayList();
//                arrUser.add("hi");
//                mapUserStar.put("listUserStar",mapUserStar);
                map2.put("arrUser", mapUserStar);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + temp_key, map2);
                childUpdates.put("/user-posts/" + iduser + "/" + temp_key, map2);

                mDatabase.updateChildren(childUpdates);
                //finish();
                //dialogLoad.dismiss();
//                baseActivity.hideProgressDialog();

            }
        });
    }


    private void dispatchTakePictureIntent() {

        check = "";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            mPhotoFile = null;
            try {
                mPhotoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        check = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.w("Path", mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            // bitPhoto = data.getData();
            Uri selectedImage = data.getData();
            Log.i("TAG",selectedImage.toString()+"");
             toImage=selectedImage;

            ResizePhoto resizePhoto = new ResizePhoto(this);
            try {
                Bitmap bitmap = resizePhoto.decodeUri(selectedImage);
                 uri_resize = resizePhoto.ReturnBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            //  Picasso.with(getApplicationContext()).load(url_stt).into(hinh);
                 }
            Glide.with(getApplicationContext()).load(toImage)
                    .thumbnail(1f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imvPhoto);
    }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
            //Log.e("UriPhoto: ",mPhotoFile.getAbsolutePath().toString());
            Uri uriTakePhoto= Uri.parse("file://"+mPhotoFile.getAbsolutePath());
            Log.e("UriPhoto:",uriTakePhoto.toString());
            ResizePhoto resizePhoto = new ResizePhoto(this);
            try {
                Bitmap bitmap = resizePhoto.decodeUri(uriTakePhoto);
                uri_resize = resizePhoto.ReturnBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                //  Picasso.with(getApplicationContext()).load(url_stt).into(hinh);
            }
            Glide.with(getApplicationContext()).load(uriTakePhoto)
                    .thumbnail(1f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imvPhoto);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_newpost:
                bundle=getIntent().getBundleExtra("data");

                if (bundle != null){
                    String dataKey=bundle.getString("dataKey");
                    updateData(dataKey, edtContent.getText().toString(), edtLocation.getText().toString());
                }
                if(imvPhoto.getDrawable() != null) {
                    post();
                }
                else {
                    Toast.makeText(PostActivity.this,"Please pick a photo!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUsername() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
}
