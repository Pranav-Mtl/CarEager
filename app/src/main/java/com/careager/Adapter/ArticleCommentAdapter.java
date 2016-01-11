package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careager.Constant.Constant;
import com.careager.careager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appslure on 04-01-2016.
 */
public class ArticleCommentAdapter extends RecyclerView.Adapter<ArticleCommentAdapter.ArticleCommentHolder> {

    Context mContext;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ArticleCommentAdapter(Context context){
        mContext=context;
    }
    @Override
    public ArticleCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.forum_comment_raw, parent, false);

        return new ArticleCommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleCommentHolder holder, int position) {
        holder.tvTitle.setText(Constant.articleCommentComment[position]);

        if(Constant.articleCommentTimestamp[position].trim().length()>0){
            long timestamp = Long.valueOf(Constant.articleCommentTimestamp[position]); //Example -> in ms
            Date d = new Date(timestamp*1000);
            holder.tvPosted.setText("Posted by "+Constant.articleCommentName[position]+" on "+dateFormat.format(d));

        }

    }

    @Override
    public int getItemCount() {
        if(Constant.articleCommentComment==null)
            return 0;
        return Constant.articleCommentComment.length;
    }

    public static class ArticleCommentHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvPosted;


        public ArticleCommentHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.comment_text);
            tvPosted= (TextView) itemView.findViewById(R.id.comment_posted);


        }
    }
}
