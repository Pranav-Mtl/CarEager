package com.careager.Adapter;

/**
 * Created by Pranav Mittal on 11/4/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BE.FilterBE;
import com.careager.BL.SearchCarBL;
import com.careager.Constant.Constant;
import com.careager.ProfileSaleDetail;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserSearchHolder> {

    Context mContext;
    SearchCarBL objSearchCarBL;
    int position;
    public UserSearchAdapter(Context context,String location,String min,String max){
        mContext=context;
        objSearchCarBL=new SearchCarBL();
        objSearchCarBL.getSearchCarList(location,min,max);
    }
    public UserSearchAdapter(Context context,FilterBE objFilterBE){
        mContext=context;
        objSearchCarBL=new SearchCarBL();
        objSearchCarBL.getFilteredList(objFilterBE);

    }

    @Override
    public UserSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.user_car_list_raw, parent, false);

        return new UserSearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserSearchHolder holder, int position) {
        holder.tvTitle.setText(Constant.searchTitle[position]);
        holder.tvMaker.setText("Maker: "+Constant.searchMaker[position]);
        holder.tvYear.setText(Constant.searchYear[position]);

        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        System.out.println(format.format(new BigInteger(Constant.searchPrice[position])));
        String price=format.format(new BigInteger(Constant.searchPrice[position]));
        holder.tvPrice.setText(price);


        //holder.tvPrice.setText("₹"+Constant.searchPrice[position]);


        holder.tvDescription.setText(Constant.searchDescription[position]);


        Picasso.with(mContext)
                .load(Constant.searchBaseURL+Constant.searchImage[position])
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(holder.ivImage);

        holder.rlSaleList.setOnClickListener(clickListener);
        holder.rlSaleList.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserSearchHolder holder = (UserSearchHolder) view.getTag();
            position = holder.getAdapterPosition();

            switch (view.getId()){
                case R.id.rl_sale_list:
                    mContext.startActivity(new Intent(mContext, ProfileSaleDetail.class).putExtra("SaleID",Constant.searchID[position]).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }
        }};


    @Override
    public int getItemCount() {
        if(Constant.searchID==null)
            return 0;
        return Constant.searchID.length;
    }

    public static class UserSearchHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvYear,tvPrice,tvMaker,tvDescription;
        ImageView ivImage;
        RelativeLayout rlSaleList;


        public UserSearchHolder(View itemView) {
            super(itemView);

            tvTitle= (TextView) itemView.findViewById(R.id.sales_title);
            tvMaker= (TextView) itemView.findViewById(R.id.sales_maker);
            tvYear= (TextView) itemView.findViewById(R.id.sales_year);
            tvPrice= (TextView) itemView.findViewById(R.id.sales_price);
            ivImage= (ImageView) itemView.findViewById(R.id.image_sales);
            rlSaleList= (RelativeLayout) itemView.findViewById(R.id.rl_sale_list);
            tvDescription=(TextView) itemView.findViewById(R.id.sales_description);
        }
    }
}

