package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careager.careager.R;

/**
 * Created by Pranav Mittal on 11/3/2015.
 */
public class UserCarListAdapter extends RecyclerView.Adapter<UserCarListAdapter.UserCarListHolder> {

    Context mContext;
    public UserCarListAdapter(Context context){
        mContext=context;
    }

    @Override
    public UserCarListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.user_car_list_raw, parent, false);

        return new UserCarListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserCarListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class UserCarListHolder extends RecyclerView.ViewHolder{



        public UserCarListHolder(View itemView) {
            super(itemView);


        }
    }
}
