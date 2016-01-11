package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BL.ForumQuestionBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;

/**
 * Created by appslure on 04-01-2016.
 */
public class ForumQuestionAdapter extends RecyclerView.Adapter<ForumQuestionAdapter.ForumQuestionHolder> {

    Context mContext;
    ForumQuestionBL objForumQuestionBL;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ForumQuestionAdapter(Context context,String category){
        mContext=context;
        objForumQuestionBL=new ForumQuestionBL();
        objForumQuestionBL.getForumList(category);
    }

    @Override
    public ForumQuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.forum_question_raw, parent, false);

        return new ForumQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForumQuestionHolder holder, int position) {
        holder.tvTitle.setText(Constant.forumTitle[position]);

        if(Constant.forumDate[position].trim().length()>0){
            long timestamp = Long.valueOf(Constant.forumDate[position]); //Example -> in ms
            Date d = new Date(timestamp*1000);
           // holder.tvDate.setText(dateFormat.format(d));
            holder.tvDescription.setText("Posted on: "+dateFormat.format(d));

        }


    }

    @Override
    public int getItemCount() {
        if(Constant.forumID==null)
            return 0;
        return Constant.forumID.length ;
    }

    public static class ForumQuestionHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription;


        public ForumQuestionHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.forum_title);
            tvDescription= (TextView) itemView.findViewById(R.id.forum_description);


        }
    }
}
