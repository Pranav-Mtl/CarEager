package com.careager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BL.SearchServiceBL;
import com.careager.Constant.Constant;
import com.careager.Profile;
import com.careager.ProfileSaleDetail;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

/**
 * Created by appslure on 13-12-2015.
 */
public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.SearchServiceHolder> {

    Context mContext;
    SearchServiceBL objSearchServiceBL;
    int position;

    public SearchServiceAdapter(Context context,String location,String category){
        mContext=context;
        objSearchServiceBL=new SearchServiceBL();
        objSearchServiceBL.getSearchServiceList(location,category);
    }

    @Override
    public SearchServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.service_list_raw, parent, false);

        return new SearchServiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchServiceHolder holder, int position) {
        String text = "<font color=#f55d2b>"+" more... "+"</font>";

        try {
            if (Constant.serviceOverview[position].length() > 0) {
                if (Constant.serviceOverview[position].trim().length() > 200) {
                    holder.tvOverview.setText(Html.fromHtml(Constant.serviceOverview[position].substring(0, 200) + text));
                } else {
                    holder.tvOverview.setText(Constant.serviceOverview[position]);
                }
            }
        }catch (Exception e){

        }

        holder.tvName.setText(Constant.serviceName[position]);

        Picasso.with(mContext)
                .load(Constant.serviceURL+Constant.serviceImage[position])
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(holder.ivProfile);

        holder.llServiceList.setOnClickListener(clickListener);
        holder.llServiceList.setTag(holder);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SearchServiceHolder holder = (SearchServiceHolder) view.getTag();
            position = holder.getAdapterPosition();

            switch (view.getId()){
                case R.id.rl_service_list:
                    mContext.startActivity(new Intent(mContext, Profile.class).putExtra("ID",Constant.serviceID[position]).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }
        }};


    @Override
    public int getItemCount() {
        int count;
        if(Constant.serviceID==null)
            count=0;
        else
            count=Constant.serviceID.length;

        return count;
    }

    public static class SearchServiceHolder extends RecyclerView.ViewHolder{

        ImageView ivProfile;
        TextView tvName,tvCompany,tvOverview;
        RelativeLayout llServiceList;

        public SearchServiceHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.service_name);

            tvOverview= (TextView) itemView.findViewById(R.id.service_overview);
            ivProfile= (ImageView) itemView.findViewById(R.id.image_service);
            llServiceList= (RelativeLayout) itemView.findViewById(R.id.rl_service_list);


        }
    }
}
