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
public class ForumCommentAdapter extends RecyclerView.Adapter<ForumCommentAdapter.ForumCommentHolder> {

    Context mContext;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ForumCommentAdapter(Context context){
        mContext=context;
    }
    @Override
    public ForumCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.forum_comment_raw, parent, false);

        return new ForumCommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForumCommentHolder holder, int position) {
        holder.tvTitle.setText(Constant.forumCommentComment[position]);
        if(Constant.forumCommentTimestamp[position].trim().length()>0){
            long timestamp = Long.valueOf(Constant.forumCommentTimestamp[position]); //Example -> in ms
            Date d = new Date(timestamp*1000);
           // holder.tvDescription.setText("Posted on: "+dateFormat.format(d));
            holder.tvPosted.setText("Posted by "+Constant.forumCommentName[position]+" on "+dateFormat.format(d));

        }

    }

    @Override
    public int getItemCount() {
        if(Constant.forumCommentName==null)
            return 0;
        return Constant.forumCommentName.length;
    }

    public static class ForumCommentHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvPosted;


        public ForumCommentHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.comment_text);
            tvPosted= (TextView) itemView.findViewById(R.id.comment_posted);


        }
    }
}
