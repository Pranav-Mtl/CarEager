package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appslure on 14-01-2016.
 */
public class DealerRatingAdapter extends RecyclerView.Adapter<DealerRatingAdapter.DealerRatingHolder> {

    Context mContext;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public DealerRatingAdapter(Context context){
        mContext=context;
    }

    @Override
    public DealerRatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dealer_rating_raw, parent, false);

        return new DealerRatingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealerRatingHolder holder, int position) {

        holder.tvName.setText(Constant.reviewName[position]);

        long timestamp = Long.valueOf(Constant.reviewDate[position]); //Example -> in ms
        Date d = new Date(timestamp*1000);
        holder.tvDate.setText("Posted on: "+dateFormat.format(d));

        holder.tvComment.setText(Constant.reviewComment[position]);
        holder.rbRating.setRating(Constant.reviewRating[position]);
        holder.rbRating.setIsIndicator(true);
        Picasso.with(mContext)
                .load(Constant.reviewBaseURL + Constant.reviewImage[position])
                .resize(70, 70)
                .placeholder(R.drawable.ic_default_logo)
                .error(R.drawable.ic_default_logo)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        if(Constant.reviewImage==null)
            return 0;
        return Constant.reviewImage.length;
    }

    public static class DealerRatingHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvDate,tvComment;
        ImageView ivImage;
        RatingBar rbRating;


        public DealerRatingHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.review_name_one);
            tvDate= (TextView) itemView.findViewById(R.id.review_date_one);
            tvComment= (TextView) itemView.findViewById(R.id.review_comment_one);

            ivImage= (ImageView) itemView.findViewById(R.id.review_image_one);
            rbRating= (RatingBar) itemView.findViewById(R.id.review_rating_one);


        }
    }
}
