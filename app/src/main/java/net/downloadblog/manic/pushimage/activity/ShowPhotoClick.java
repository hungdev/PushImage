package net.downloadblog.manic.pushimage.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.downloadblog.manic.pushimage.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPhotoClick extends AppCompatActivity {
    ImageView imvPhoto;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo_click);

        imvPhoto=(ImageView)findViewById(R.id.imv_photo);

        bundle=getIntent().getBundleExtra("data");
        if (bundle != null) {
            String dataContent = bundle.getString("dataContent");
            String dataLocation = bundle.getString("dataLocation");
            String dataPhoto = bundle.getString("dataPhoto");
            String dataKey=bundle.getString("dataKey");

//            Glide.with(getApplicationContext()).load(dataPhoto)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imvPhoto);

            Glide
                    .with(this)
                    .load(dataPhoto)
                    .into(imvPhoto);
            PhotoViewAttacher photoView= new PhotoViewAttacher(imvPhoto);
            photoView.update();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
