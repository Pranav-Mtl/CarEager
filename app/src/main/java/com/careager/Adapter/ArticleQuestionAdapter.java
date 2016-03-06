package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careager.BL.ArticleQuestionBL;
import com.careager.BL.ForumQuestionBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appslure on 04-01-2016.
 */
public class ArticleQuestionAdapter extends RecyclerView.Adapter<ArticleQuestionAdapter.ArticleQuestionHolder> {

    Context mContext;
   ArticleQuestionBL objArticleQuestionBL;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ArticleQuestionAdapter(Context context){
        mContext=context;
        objArticleQuestionBL=new ArticleQuestionBL();
        objArticleQuestionBL.getArticleList();
    }

    @Override
    public ArticleQuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.forum_question_raw, parent, false);

        return new ArticleQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleQuestionHolder holder, int position) {
        holder.tvTitle.setText(Constant.articleTitle[position]);

        if(Constant.articleDate[position].trim().length()>0){
            long timestamp = Long.valueOf(Constant.articleDate[position]); //Example -> in ms
            Date d = new Date(timestamp*1000);
            holder.tvDescription.setText("Posted on: "+dateFormat.format(d)+" by "+Constant.articleAuthor[position]);

        }


    }

    @Override
    public int getItemCount() {
        if(Constant.articleID==null)
            return 0;
        return Constant.articleID.length ;
    }

    public static class ArticleQuestionHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription;


        public ArticleQuestionHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.forum_title);
            tvDescription= (TextView) itemView.findViewById(R.id.forum_description);


        }
    }
}
