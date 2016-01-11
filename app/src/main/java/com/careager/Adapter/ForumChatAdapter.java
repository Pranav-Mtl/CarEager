package com.careager.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BE.UserChatBE;
import com.careager.careager.R;

import java.util.ArrayList;

/**
 * Created by appslure on 02-01-2016.
 */
public class ForumChatAdapter  extends RecyclerView.Adapter<ForumChatAdapter.UserChatHolder> {

    private final Activity context;
    private final ArrayList<UserChatBE> list;

    public ForumChatAdapter(Activity context, ArrayList<UserChatBE> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UserChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.user_chat_raw, parent, false);

        return new UserChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserChatHolder holder, int position) {
        if (list != null) {
            UserChatBE h = list.get(position);
            holder.tvMessage.setText(h.getPERSON_CHAT_MESSAGE());
            holder.tvDate.setText(h.getCHAT_DATE());
            if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("RECEIVED")) {
                holder.llRaw.setGravity(Gravity.RIGHT);
                holder.llChatBg.setBackgroundResource(R.drawable.chat_recieved_bg);
                holder.llChatBg.setPadding(8,8,8,8);

            }
            else
            {
                holder.llRaw.setGravity(Gravity.LEFT);
                holder.llChatBg.setBackgroundResource(R.drawable.chat_bg_shape);
                holder.llChatBg.setPadding(8, 8, 8, 8);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserChatHolder extends RecyclerView.ViewHolder{

        TextView tvMessage,tvDate;
        LinearLayout llChatBg,llRaw;

        public UserChatHolder(View itemView) {
            super(itemView);
            tvMessage= (TextView) itemView.findViewById(R.id.chat_message);
            tvDate= (TextView) itemView.findViewById(R.id.chat_date);
            llChatBg= (LinearLayout) itemView.findViewById(R.id.chat_text_bg);
            llRaw= (LinearLayout) itemView.findViewById(R.id.chat_raw);


        }
    }
}