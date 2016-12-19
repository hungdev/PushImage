package net.downloadblog.manic.pushimage.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.activity.IData;
import net.downloadblog.manic.pushimage.activity.Time;
import net.downloadblog.manic.pushimage.object.ImageEntity;
import net.downloadblog.manic.pushimage.object.RecyclerViewHolders;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RecycleAdapter extends RecyclerView.Adapter<RecyclerViewHolders>{
    private ArrayList<ImageEntity> itemList;
    private Context context;
    public static String key;
    int resultStar;
    Map getMap;
    Uri uriget;
    Dialog myDialog;

    public static String dataLocation;
    public static String dataContent;
    public static String dataPhoto;
    public static String dataKey;
    IData iData ;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public RecycleAdapter(ArrayList<ImageEntity> itemList, Context context,IData iData){
        this.itemList=itemList;
        this.context=context;
        this.iData = iData;
    }
    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_layout, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        holder.tv_user.setText(itemList.get(position).getUser());
        holder.tv_time.setText(Time.calenderTime(itemList.get(position).getTime()));
        holder.tv_content.setText(itemList.get(position).getContent());
        holder.tv_starCount.setText(""+itemList.get(position).getStarCount());
        holder.tv_location.setText(""+itemList.get(position).getLocation());
        //holder.tv_percent.setText("" + itemList.get(position).getPercent());
//        holder.rtb_rate.setRating(itemList.get(position).getRate());
        //holder.imv_photo.setImageResource(itemList.get(position).getPhoto());


        Picasso.with(context).load(itemList.get(position).getPhoto())
                .into(holder.imv_photo);



//        if(getMap.get(getUid())==getUid()){
//            holder.imv_star.setImageResource(R.drawable.redstar);
//        }

//        if(flag==0){
//            //holder.imv_star.setEnabled(false);
//        }
        if(itemList.get(position).getStarCount()>0){
            holder.imv_star.setEnabled(false);
        }
        holder.imv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onStarClicke
                Log.e("Click", "Star" + itemList.get(position).getKey());
                key=itemList.get(position).getKey();
//                if(itemList.get(position).getMapUserStar().containsKey(getUid())){


                    String count= holder.tv_starCount.getText().toString();
                    resultStar= Integer.parseInt(count)+1;
                    Log.e("containsKey",itemList.get(position).getMapUserStar().toString());
//                Toast.makeText(context,"id: "+mDataRef.child("posts").getKey(),Toast.LENGTH_LONG).show();

                    getMap= itemList.get(position).getMapUserStar();
                    Log.e("getMap",""+getMap);
                    starStatus();
                    starUpdate();
                }
//                else return;
//            }
        });

        holder.imv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPhoto=itemList.get(position).getPhoto();
                if (iData!=null){
                    iData.onCickData(2,"","",dataPhoto, dataKey);
                }
            }
        });




        holder.imv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog =  new Dialog(context);
                myDialog.setContentView(R.layout.option_layout);
                myDialog.setCancelable(true);
                myDialog.setTitle("Option:");

                Button btnEdit = (Button) myDialog.findViewById(R.id.btn_edit);
                Button btnDelete = (Button) myDialog.findViewById(R.id.btn_delete);
                Button btnReport = (Button) myDialog.findViewById(R.id.btn_report);
                Button btnCancel = (Button) myDialog.findViewById(R.id.btn_cancel);

                if(!getUid().equals(itemList.get(position).getIduser())){
                    btnEdit.setEnabled(false);
                    btnDelete.setEnabled(false);
                }

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                        dataKey=itemList.get(position).getKey();
                        dataLocation=itemList.get(position).getLocation();
                        dataContent=itemList.get(position).getContent();
                        dataPhoto=itemList.get(position).getPhoto();

                        if (iData!=null){
                            iData.onCickData(1,dataContent,dataLocation,dataPhoto, dataKey);
                        }

                        myDialog.dismiss();

                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("aaa",""+dataKey);
                        // TODO Auto-generated method stub
                        mAuth = FirebaseAuth.getInstance();
//                        storage = FirebaseStorage.getInstance();
                        //mDatabase = FirebaseDatabase.getInstance().getReference().child(ImageEntity.class.getSimpleName());
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        final String delKey= itemList.get(position).getKey();
                        //ShowDialog showDialog= new ShowDialog(context);
                        //showDialog.DialogPress("Do you want to delete?");
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Notice:");
                        builder1.setMessage("Do you want to delete?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mDatabase.child("posts").child(delKey).removeValue();
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        myDialog.dismiss();

                    }
                });

                btnReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String recepientEmail = "hungns126@gmail.com";
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + recepientEmail));
                        context.startActivity(intent);
                        myDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("iduser1:", getUid());
                        Log.e("iduser2:", itemList.get(position).getIduser());
                        myDialog.dismiss();

                    }
                });
                myDialog.show();
            }
        });



        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key=itemList.get(position).getKey();
                String selectedImage= itemList.get(position).getPhoto().toString();
//                Glide.with(context).load(itemList.get(position).getPhoto().toString())
//                        .thumbnail(1f)
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(imvPhoto);

                Picasso.with(context)
                        .load(itemList.get(position).getPhoto().toString())
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                      try {
                                          String root = Environment.getExternalStorageDirectory().toString();
                                          File myDir = new File(root + "/PushImage");

                                          if (!myDir.exists()) {
                                              myDir.mkdirs();
                                          }

                                          String name = new Date().toString() + ".jpg";
                                          myDir = new File(myDir, name);
                                          FileOutputStream out = new FileOutputStream(myDir);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                           uriget=Uri.fromFile(myDir);
                                          out.flush();
                                          out.close();
                                      } catch(Exception e){
                                          // some action
                                      }
                                  }

                                  @Override
                                  public void onBitmapFailed(Drawable errorDrawable) {
                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                  }
                              }
                        );

                //Uri downloadUri = Uri.parse(itemList.get(position).getPhoto().toString());
                //Log.e("ImageDownload", ""+downloadUri);
                Log.e("PhotoLink", selectedImage);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriget);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                shareIntent.setType("*/*");
                //shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

    };




    private void starStatus(){
        final DatabaseReference mDataRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference message_root = mDataRef.child("posts");
        DatabaseReference taskRef = message_root.child(key).child("ArrayListUserStar");
        Map<String,Object> taskMap = new HashMap<String,Object>();

        getMap.put(getUid(),true);

        taskRef.updateChildren(getMap);
    }

    private void starUpdate(){

        final DatabaseReference mDataRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference message_root = mDataRef.child("posts");
        DatabaseReference taskRef = message_root.child(key);
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("starCount", resultStar);
        //taskMap.put("")
        taskRef.updateChildren(taskMap);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
