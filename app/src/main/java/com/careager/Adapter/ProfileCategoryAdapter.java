package com.careager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.DealerContact;
import com.careager.ProfileProduct;
import com.careager.ProfileSaleDetail;
import com.careager.ProfileSaleList;
import com.careager.ProfileServices;
import com.careager.careager.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 24-11-2015.
 */
public class ProfileCategoryAdapter extends RecyclerView.Adapter<ProfileCategoryAdapter.DealerAddedCategoryHolder> {

    Context mContext;
    String profileID;

    public ProfileCategoryAdapter(Context context,String profile){
        mContext=context;
        profileID=profile;
        String category = Util.getSharedPrefrenceValue(context, Constant.SP_DEALER_CATEGORY);
        Log.d("Category",category);
        parseCategory(category);
    }

    @Override
    public DealerAddedCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dealer_profile_category_raw, parent, false);

        return new DealerAddedCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealerAddedCategoryHolder holder, final int position) {
        holder.tvCategory.setText(Constant.categoryName[position].toUpperCase());

        holder.tvCategory.setOnClickListener(clickListener);
        holder.tvCategory.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DealerAddedCategoryHolder holder = (DealerAddedCategoryHolder) view.getTag();
            int position = holder.getAdapterPosition();

            switch (view.getId()){
                case R.id.dealer_profile_category_name:
                    String selectedName=Constant.categoryName[position];
                    Log.d("selected Category",selectedName);
                    if (selectedName.trim().length() > 0) {
                        if (selectedName.equalsIgnoreCase(Constant.tagSales)) {
                            mContext.startActivity(new Intent(mContext, ProfileSaleList.class).putExtra("ID", profileID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else if (selectedName.equalsIgnoreCase(Constant.tagService)) {
                            mContext.startActivity(new Intent(mContext, ProfileServices.class).putExtra("ID", profileID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else if (selectedName.equalsIgnoreCase(Constant.tagProduct)) {
                                mContext.startActivity(new Intent(mContext, ProfileProduct.class).putExtra("ID", profileID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else if (selectedName.equalsIgnoreCase(Constant.tagInsurance)) {
                            mContext.startActivity(new Intent(mContext, DealerContact.class).putExtra("CategoryName", selectedName).putExtra("ID", profileID).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                    break;
            }
        }};


    @Override
    public int getItemCount() {
        int count;

        if(Constant.categoryName==null)
            count=0;
        else
            count=Constant.categoryName.length;

        return count;
    }

    public  class DealerAddedCategoryHolder extends RecyclerView.ViewHolder{

        TextView tvCategory;
        LinearLayout llMail;

        public DealerAddedCategoryHolder(View itemView) {
            super(itemView);

            tvCategory= (TextView) itemView.findViewById(R.id.dealer_profile_category_name);
            llMail= (LinearLayout) itemView.findViewById(R.id.layout_category);




        }
    }
    /* parse Dealer added category JSON saved in shared preference from DealerProfileBL class*/

    private void parseCategory(String result){
        try {



            if (result != null) {
                JSONParser jsonP = new JSONParser();
                try {
                    Object obj = jsonP.parse(result);
                    JSONArray jsonArrayObject = (JSONArray) obj;
                    Constant.categoryName = new String[jsonArrayObject.size()];
                    //Constant.categoryID = new int[jsonArrayObject.size()];


                    for (int i = 0; i < jsonArrayObject.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                       // categoryID[i] = Integer.valueOf(jsonObject.get("category_id").toString());
                        Constant.categoryName[i] = jsonObject.get("category").toString();
                        Log.d("Category",Constant.categoryName[i]);



                    }


                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }catch (NullPointerException e){

        }catch (Exception e){

        }

    }
}
