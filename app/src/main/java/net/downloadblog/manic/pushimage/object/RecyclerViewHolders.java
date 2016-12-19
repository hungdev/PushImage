package net.downloadblog.manic.pushimage.object;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import net.downloadblog.manic.pushimage.R;

/**
 * Created by ManIc on 25-Jun-16.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_user;
    public TextView tv_time;
    public TextView tv_content;
    public TextView tv_percent;
    public ImageView imv_photo;
    public RatingBar rtb_rate;
    public ImageView imv_star;
    public TextView tv_starCount;
    public ImageButton btn_share;
    public TextView tv_location;
    public ImageView imv_option;
    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        tv_user =(TextView)itemView.findViewById(R.id.tv_user);
        tv_content =(TextView)itemView.findViewById(R.id.tv_content);
        tv_time =(TextView)itemView.findViewById(R.id.tv_time);
        //tv_percent =(TextView)itemView.findViewById(R.id.tv_percent);
        imv_photo=(ImageView)itemView.findViewById(R.id.imv_photo);
       // rtb_rate=(RatingBar)itemView.findViewById(R.id.rtb_rate);
        imv_star=(ImageView)itemView.findViewById(R.id.imv_star);
        tv_starCount=(TextView)itemView.findViewById(R.id.tv_starCount);
        btn_share=(ImageButton)itemView.findViewById(R.id.btn_share);
        tv_location=(TextView)itemView.findViewById(R.id.tv_location);
        imv_option=(ImageView)itemView.findViewById(R.id.imv_option);
    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(v.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();

    }

}
