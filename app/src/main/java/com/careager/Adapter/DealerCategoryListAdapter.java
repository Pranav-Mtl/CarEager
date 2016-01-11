package com.careager.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.careager.BE.DealerCategoryBE;
import com.careager.BE.DealerSignUpBE;
import com.careager.BL.DealerCategoryBL;
import com.careager.BL.DealerSignUpBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav Mittal on 11/1/2015.
 */
public class DealerCategoryListAdapter extends RecyclerView.Adapter<DealerCategoryListAdapter.DealerCategoryHolder> {

    Context mContext;

    public List listCategory=new ArrayList<>();
    DealerSignUpBL objDealerSignUpBL;
    DealerSignUpBE objDealerSignUpBE;

    public int mSelectedItem = -1;


    DealerCategoryBL objDealerCategoryBL;

    CheckBox lastChecked;




    public DealerCategoryListAdapter(Context context,DealerSignUpBL dealerSignUpBL,DealerSignUpBE dealerSignUpBE,DealerCategoryBE objDealerCategoryBE){
        mContext=context;
        objDealerSignUpBL=dealerSignUpBL;
        objDealerSignUpBE=dealerSignUpBE;
        objDealerCategoryBL=new DealerCategoryBL();
        objDealerCategoryBL.getAllCategory(objDealerCategoryBE);
        parseCategory(objDealerCategoryBE.getCategoryJSON());

    }

    @Override
    public DealerCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dealer_category_raw, parent, false);

        return new DealerCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DealerCategoryHolder holder, final int position) {
        holder.tvCategory.setText(listCategory.get(position).toString());

        holder.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (holder.cbCategory.isChecked()) {

                    if(lastChecked!=null)
                    {
                        lastChecked.setChecked(false);
                        lastChecked=null;
                        holder.cbCategory.setChecked(false);

                    }
                    mSelectedItem=-1;



                } else {
                    if(lastChecked!=null)
                    {
                        lastChecked.setChecked(false);
                        lastChecked=null;


                    }

                    holder.cbCategory.setChecked(true);
                    mSelectedItem=position;
                    lastChecked=holder.cbCategory;


                }


            }
        });

        holder.cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {

                    if(lastChecked!=null)
                    {
                        lastChecked.setChecked(false);
                        lastChecked=null;
                    }

                    mSelectedItem=position;
                    lastChecked=cb;

                } else {
                    if(lastChecked!=null)
                    {
                        lastChecked.setChecked(false);
                        lastChecked=null;
                    }
                    mSelectedItem=-1;
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        int count;
        if(listCategory==null)
            count=0;
        else
            count=listCategory.size();

        return count;
    }

    public  class DealerCategoryHolder extends RecyclerView.ViewHolder {

        TextView tvCategory;
        CheckBox cbCategory;

        public DealerCategoryHolder(View itemView) {
            super(itemView);

            tvCategory = (TextView) itemView.findViewById(R.id.dealer_category_text);
            cbCategory = (CheckBox) itemView.findViewById(R.id.dealer_category_cb);


        }


    }
    private void parseCategory(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;


            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listCategory.add(jsonObject.get("category"));
            }


        }
        catch (Exception e){

        }
    }
}
