package com.careager.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.careager.BL.HomeScreenBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

/**
 * Created by appslure on 12-12-2015.
 */
public class CarInfoAdapter extends BaseAdapter {
    Context mContext;
    HomeScreenBL objHomeScreenBL;

    public CarInfoAdapter(Context context){
        mContext=context;

    }

    @Override
    public int getCount() {
        int count;
        if(Constant.carLogo==null)
            count=0;
        else
            count=Constant.carLogo.length;

        return  count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(Constant.CategoryImageWidth,Constant.CategoryImageHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0,0,0,0);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.d("Image",Constant.carLogoUrl+Constant.carLogo[position]);

        Picasso.with(mContext)
                .load(Constant.carLogoUrl+Constant.carLogo[position])
                .resize(150,150)
                .placeholder(R.drawable.ic_default_logo)
                .error(R.drawable.ic_default_logo)
                .into(imageView);

        ///imageView.setImageResource();
        return imageView;
    }
}
