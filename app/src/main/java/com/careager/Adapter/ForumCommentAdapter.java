package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careager.Constant.Constant;
import com.careager.careager.R;

/**
 * Created by appslure on 04-01-2016.
 */
public class ForumCommentAdapter extends RecyclerView.Adapter<ForumCommentAdapter.ForumCommentHolder> {

    Context mContext;

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
        holder.tvPosted.setText("Posted by "+Constant.forumCommentName[position]+" on "+Constant.forumCommentTimestamp[position]);
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
