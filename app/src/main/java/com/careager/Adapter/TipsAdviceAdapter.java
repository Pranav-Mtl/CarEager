package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BL.TipsAdviceBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

/**
 * Created by appslure on 05-01-2016.
 */
public class TipsAdviceAdapter extends RecyclerView.Adapter<TipsAdviceAdapter.TipsAdviceHolder> {

    Context mContext;
    TipsAdviceBL objTipsAdviceBL;
    public TipsAdviceAdapter(Context context){
        mContext=context;
        objTipsAdviceBL=new TipsAdviceBL();
        objTipsAdviceBL.getTipsList();

    }

    @Override
    public TipsAdviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.tips_list_raw, parent, false);

        return new TipsAdviceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TipsAdviceHolder holder, int position) {

        holder.tvTitle.setText(Constant.tipsTitle[position]);
        holder.tvContent.setText(Constant.tipsContent[position]);
        holder.tvPosted.setText("Posted on: "+Constant.tipsDate[position]);

        Picasso.with(mContext)
                .load(Constant.tipsURL+Constant.tipsImage[position])
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        if(Constant.tipsImage==null)
            return 0;
        return Constant.tipsImage.length;
    }

    public static class TipsAdviceHolder extends RecyclerView.ViewHolder{

        ImageView ivProfile;
        TextView tvTitle,tvContent,tvPosted;


        public TipsAdviceHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tips_title);

            tvContent= (TextView) itemView.findViewById(R.id.tips_content);
            ivProfile= (ImageView) itemView.findViewById(R.id.image_tips);
            tvPosted= (TextView) itemView.findViewById(R.id.tips_posted);


        }
    }
}
