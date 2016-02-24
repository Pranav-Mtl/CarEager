package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.careager.BL.DealerChatListBL;
import com.careager.BL.ForumUserListBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appslure on 26-01-2016.
 */
public class DealerChatListAdapter extends RecyclerView.Adapter<DealerChatListAdapter.DealerChatListHolder> {

    Context mContext;
    DealerChatListBL objDealerChatListBL;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public DealerChatListAdapter(Context context,String userID,String userType){
        mContext=context;
        objDealerChatListBL=new DealerChatListBL();
        objDealerChatListBL.getSearchCarList(userID,userType);
    }

    @Override
    public DealerChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.forum_user_raw, parent, false);

        return new DealerChatListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealerChatListHolder holder, int position) {
        holder.tvName.setText(Constant.forumUserName[position]);
        holder.tvChat.setText(Constant.forumUserChat[position]);

        if(Constant.forumUserDate[position].trim().length()>0){
            long timestamp = Long.valueOf(Constant.forumUserDate[position]); //Example -> in ms
            Date d = new Date(timestamp*1000);
            holder.tvDate.setText(dateFormat.format(d));

        }


        Picasso.with(mContext)
                .load(Constant.forumUserImage[position])
                .resize(50,50)
                .placeholder(R.drawable.ic_default_logo)
                .error(R.drawable.ic_default_logo)
                .into(holder.ivImage);
    }



    @Override
    public int getItemCount() {
        if(Constant.forumUserID==null)
            return 0;

        return Constant.forumUserID.length;
    }

    class DealerChatListHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvChat,tvDate;
        ImageView ivImage;
        CheckBox cbCategory;

        public DealerChatListHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.forum_user_name);
            tvChat = (TextView) itemView.findViewById(R.id.forum_user_chat);
            tvDate = (TextView) itemView.findViewById(R.id.forum_user_date);
            ivImage = (ImageView) itemView.findViewById(R.id.forum_user_image);


        }


    }
}

