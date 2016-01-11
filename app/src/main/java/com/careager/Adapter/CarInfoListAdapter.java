package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careager.BL.CarInfoListBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;

/**
 * Created by appslure on 12-12-2015.
 */
public class CarInfoListAdapter extends RecyclerView.Adapter<CarInfoListAdapter.CarInfoListHolder> {
    Context mContext;
    String modelName;
    CarInfoListBL objCarInfoListBL;

    public CarInfoListAdapter(Context context,String model){
        mContext=context;
        modelName=model;
        objCarInfoListBL=new CarInfoListBL();
        objCarInfoListBL.getCarInfoList(modelName);
    }

    @Override
    public CarInfoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.car_info_list_raw, parent, false);

        return new CarInfoListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarInfoListHolder holder, int position) {
        holder.tvMaker.setText("Model: "+Constant.modelName[position]);
        holder.tvModel.setText("");
        holder.tvOverview.setText("Overview: "+Constant.modelOverview[position]);
        holder.tvPrice.setText("Price: "+"\u20B9"+Constant.modelMinPrice[position]+" to "+"\u20B9"+Constant.modelMaxPrice[position]+" lakh");
    }

    @Override
    public int getItemCount() {
        int count;

        if(Constant.modelName==null)
            count=0;
        else
            count=Constant.modelName.length;

        return count;
    }

    public static class CarInfoListHolder extends RecyclerView.ViewHolder{

        TextView tvMaker,tvModel,tvOverview,tvPrice;

        public CarInfoListHolder(View itemView) {
            super(itemView);
            tvMaker= (TextView) itemView.findViewById(R.id.car_info_maker);
            tvModel= (TextView) itemView.findViewById(R.id.car_info_model);
            tvOverview= (TextView) itemView.findViewById(R.id.car_info_overview);
            tvPrice= (TextView) itemView.findViewById(R.id.car_info_price);


        }
    }
}
