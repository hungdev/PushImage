package net.downloadblog.manic.pushimage.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import net.downloadblog.manic.pushimage.R;

/**
 * Created by ManIc on 12/18/2016.
 */
public class OptionView extends LinearLayout {
    public OptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.option_layout,null);
        addView(view);


    }
}