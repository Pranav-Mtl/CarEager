package com.careager.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BE.UserChatBE;
import com.careager.careager.R;

import java.util.ArrayList;

/**
 * Created by appslure on 10-01-2016.
 */
public class ForumChatListAdapter extends ArrayAdapter<UserChatBE> {

    private final Activity context;
    private final ArrayList<UserChatBE> list;

    public ForumChatListAdapter(Activity context, ArrayList<UserChatBE> list) {
        super(context, R.layout.user_chat_raw, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        TextView tvMessage,tvDate;
        LinearLayout llChatBg,llRaw;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        System.out.println("SIZE : " + list.size());
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.user_chat_raw, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMessage= (TextView) convertView.findViewById(R.id.chat_message);
            viewHolder.tvDate= (TextView) convertView.findViewById(R.id.chat_date);
            viewHolder.llChatBg= (LinearLayout) convertView.findViewById(R.id.chat_text_bg);
            viewHolder.llRaw= (LinearLayout) convertView.findViewById(R.id.chat_raw);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            UserChatBE h = list.get(position);
            viewHolder.tvMessage.setText(h.getPERSON_CHAT_MESSAGE());
            viewHolder.tvDate.setText(h.getCHAT_DATE());
            if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("RECEIVED")) {
                viewHolder.llRaw.setGravity(Gravity.RIGHT);
                viewHolder.llChatBg.setBackgroundResource(R.drawable.bubble_a);
                viewHolder.llChatBg.setPadding(8,8,8,8);

            }
            else
            {
                viewHolder.llRaw.setGravity(Gravity.LEFT);
                viewHolder.llChatBg.setBackgroundResource(R.drawable.bubble_b);
                viewHolder.llChatBg.setPadding(8, 8, 8, 8);
            }
        }
        /*if (list != null) {
            ChatPeopleBE h = list.get(position);
            viewHolder.text.setText(h.getPERSON_CHAT_MESSAGE());
            viewHolder.sent_or_received.setText(h.getCHAT_DATE());
            if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("RECEIVED")) {
                viewHolder.chat_row_lin.setGravity(Gravity.RIGHT);
                viewHolder.chat_text_bg.setBackgroundResource(R.drawable.chat_recieved_bg);
                viewHolder.chat_text_bg.setPadding(8, 8, 8, 8);
            }
        }*/

        return convertView;
    }

}
