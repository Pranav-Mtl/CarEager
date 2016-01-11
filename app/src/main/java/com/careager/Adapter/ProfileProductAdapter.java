package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.careager.BL.ProfileProductBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

/**
 * Created by appslure on 13-12-2015.
 */
public class ProfileProductAdapter extends RecyclerView.Adapter<ProfileProductAdapter.LatestOfferHolder> {

    Context mContext;
    String pageNo;

    ProfileProductBL objProfileProductBL;

    public ProfileProductAdapter(String pageNo, Context context){
        mContext=context;
        this.pageNo=pageNo;
        objProfileProductBL =new ProfileProductBL();
        objProfileProductBL.getProduct(pageNo);

    }
    @Override
    public LatestOfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.product_raw, parent, false);

        return new LatestOfferHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LatestOfferHolder holder, int position) {
        Log.d("Description",Constant.productDescription[position]);
        holder.tvDescription.setText(Constant.productDescription[position]);
        holder.tvName.setText(Constant.productName[position]);
        holder.tvPrice.setText("₹" + Constant.productPrice[position]);


        Picasso.with(mContext)
                .load(Constant.productBaseURL+Constant.productImage[position])
                .resize(150, 150)
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        int count=0;
        if(Constant.productID==null){

        }
        else {
            count=Constant.productID.length;
        }
        return count;
    }

    public static class LatestOfferHolder extends RecyclerView.ViewHolder{

        TextView tvDescription,tvName,tvPrice;
        ImageView imageView;

        public LatestOfferHolder(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.product_image);
            tvDescription= (TextView) itemView.findViewById(R.id.product_description);
            tvName= (TextView) itemView.findViewById(R.id.product_name);
            tvPrice= (TextView) itemView.findViewById(R.id.product_price);


        }
    }
}
