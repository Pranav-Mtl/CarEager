package com.careager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BE.DealerProfileBE;
import com.careager.BL.DealerProfileSaleBL;
import com.careager.Constant.Constant;
import com.careager.ProfileSaleDetail;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by appslure on 02-12-2015.
 */
public class ProfileSaleAdapter extends RecyclerView.Adapter<ProfileSaleAdapter.DealerProfileSaleHolder> {

    Context mContext;
    DealerProfileSaleBL objDealerProfileSaleBL;
    DealerProfileBE objDealerProfileBE;
    int position;
    public ProfileSaleAdapter(Context context, String id, DealerProfileBE dealerProfileBE){
        mContext=context;
        objDealerProfileBE=dealerProfileBE;
        objDealerProfileSaleBL=new DealerProfileSaleBL();
        objDealerProfileSaleBL.getProfileData(id,dealerProfileBE,context);

    }

    @Override
    public DealerProfileSaleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dealer_profile_sale_raw, parent, false);

        return new DealerProfileSaleHolder(itemView);
    }




    @Override
    public void onBindViewHolder(DealerProfileSaleHolder holder, int position) {
        holder.tvTitle.setText(Constant.saleTitle[position]);
        holder.tvMaker.setText("Maker: "+Constant.saleMaker[position]);
        holder.tvYear.setText(Constant.saleYear[position]);

        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        System.out.println(format.format(new BigInteger(Constant.salePrice[position])));
        String price=format.format(new BigInteger(Constant.salePrice[position]));
        holder.tvPrice.setText(price);

        Picasso.with(mContext)
                .load(objDealerProfileBE.getSaleBaseURL()+Constant.saleImage[position])
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(holder.ivImage);
        holder.rlSaleList.setOnClickListener(clickListener);
        holder.rlSaleList.setTag(holder);


    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DealerProfileSaleHolder holder = (DealerProfileSaleHolder) view.getTag();
            position = holder.getAdapterPosition();

            switch (view.getId()){
                case R.id.rl_sale_list:
                    mContext.startActivity(new Intent(mContext, ProfileSaleDetail.class).putExtra("SaleID",Constant.saleID[position]).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }
        }};

            @Override
    public int getItemCount()
    {
                int count;
                if(Constant.saleID==null)
                    count=0;
                else
                    count=Constant.saleID.length;

        return count;
    }

    public  class DealerProfileSaleHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvYear,tvPrice,tvMaker;
        ImageView ivImage;
        RelativeLayout rlSaleList;

        public DealerProfileSaleHolder(View itemView) {
            super(itemView);

            tvTitle= (TextView) itemView.findViewById(R.id.sales_title);
            tvMaker= (TextView) itemView.findViewById(R.id.sales_maker);
            tvYear= (TextView) itemView.findViewById(R.id.sales_year);
            tvPrice= (TextView) itemView.findViewById(R.id.sales_price);
            ivImage= (ImageView) itemView.findViewById(R.id.image_sales);
            rlSaleList= (RelativeLayout) itemView.findViewById(R.id.rl_sale_list);


        }
    }
}
