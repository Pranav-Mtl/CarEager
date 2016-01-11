package com.careager;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.careager.Adapter.CarInfoAdapter;
import com.careager.Adapter.DrawerAdapter;
import com.careager.BL.FilterBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.Container.PagerContainer;
import com.careager.careager.R;
import com.efor18.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final float MIN_SCALE = 0.85f;
    PagerContainer mContainer;
    LinearLayout llCareager,llOldNewCar,llServices;

    AutoCompleteTextView tvCarLocation,tvServiceLocation;
    Button btnSearchCar,btnSearchService,btnSearchCarinfo;
    TextView tvMinBudget,tvMaxBudget;
    Spinner spinnerCategory,spinnerBrand,spinnerModel;
    GridView gridview ;

    String strCarLocation,strMinBudget,strMaxBudget;
    CarInfoAdapter objCarInfoAdapter;

    FilterBL objFilterBL;

    ProgressDialog progressDialog;


    ArrayList<String> adapterList = new ArrayList<String>();

    Button btnSearch;

    private static final String LOG_TAG = "Careager";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    String userID;
    String userStatus;

DrawerAdapter drawerAdapter;
    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dealer_home, container, false);
        mContainer = (PagerContainer) view.findViewById(R.id.pager_container);


        llCareager= (LinearLayout) view.findViewById(R.id.ll_careager);
        llOldNewCar= (LinearLayout) view.findViewById(R.id.ll_new_old_car);
        llServices= (LinearLayout) view.findViewById(R.id.ll_service);
        tvCarLocation= (AutoCompleteTextView) view.findViewById(R.id.home_search_location);
        btnSearchCar= (Button) view.findViewById(R.id.home_btn_car);
        tvServiceLocation= (AutoCompleteTextView) view.findViewById(R.id.tv_search_service);
        spinnerCategory= (Spinner) view.findViewById(R.id.spinner_category_list);
        btnSearchService= (Button) view.findViewById(R.id.btn_search_service);
        //gridview = (GridView) view.findViewById(R.id.gridview_logo);
        spinnerBrand= (Spinner) view.findViewById(R.id.caringo_brand);
        spinnerModel= (Spinner) view.findViewById(R.id.carinfo_model);
        btnSearchCarinfo= (Button) view.findViewById(R.id.btn_search_carinfo);

        btnSearchCar.setOnClickListener(this);
        btnSearchService.setOnClickListener(this);
        btnSearchCarinfo.setOnClickListener(this);

        ViewPager pager = mContainer.getViewPager();
        //ViewPager pager=(ViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true, new BigImage());
        progressDialog=new ProgressDialog(getActivity());

        mContainer=new PagerContainer(getActivity());

        objFilterBL=new FilterBL();

        PagerContainer.setLayout(getActivity(),llCareager,llOldNewCar,llServices);

        DealerPageAdapter adapter=new DealerPageAdapter(getActivity());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages
        pager.setPageMargin(15);
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);

        pager.setPadding(30, 0, 30, 0);
        // pager.setPageMargin(45);

        tvMinBudget = (TextView) view.findViewById(R.id.minValue);
        tvMaxBudget = (TextView) view.findViewById(R.id.maxValue);
        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(1,150,getActivity());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values


                tvMinBudget.setText(minValue.toString());
                tvMaxBudget.setText(maxValue.toString());

            }
        });

        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) view.findViewById(R.id.layout);
        layout.addView(seekBar);

        tvCarLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.gender_spinner_item));
        tvServiceLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.gender_spinner_item));

        ArrayAdapter<String> adapterSpn=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,Constant.homeCategory);
        adapterSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterSpn);


        //tvLocation.showDropDown();
        //tvCarLocation.setOnItemClickListener(getActivity());

        userID= Util.getSharedPrefrenceValue(getActivity(),Constant.SP_LOGIN_ID);
        if(userID==null)
            userStatus=Constant.USER_LOGOUT;
        else
            userStatus=Constant.USER_LOGIN;

        ArrayAdapter<String> adapterVehicleType = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,Constant.carName);
        adapterVehicleType.setDropDownViewResource(R.layout.spinner_item);
        spinnerBrand.setAdapter(adapterVehicleType);

        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String model = Constant.carID[position-1];
                    new GetModels().execute(model);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*objCarInfoAdapter=new CarInfoAdapter(getActivity());

        gridview.setAdapter(objCarInfoAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),CarInfoList.class).putExtra("Model", Constant.carName[position]));
            }
        });*/


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.home_btn_car:
                strCarLocation=tvCarLocation.getText().toString();
                strMinBudget=tvMinBudget.getText().toString();
                strMaxBudget=tvMaxBudget.getText().toString();

                if(strCarLocation.trim().length()==0){

                }
                else {
                    Intent intent=new Intent(getActivity(),SearchCar.class);
                    intent.putExtra("Location",strCarLocation);
                    intent.putExtra("Min",strMinBudget);
                    intent.putExtra("Max",strMaxBudget);
                    startActivity(intent);
                }
                break;
            case R.id.btn_search_service:
                String serviceLocation=tvServiceLocation.getText().toString();
                String category=spinnerCategory.getSelectedItem().toString();

                if(serviceLocation.trim().length()==0){

                }
                else {
                    Intent intent=new Intent(getActivity(),SearchService.class);
                    intent.putExtra("Location",serviceLocation);
                    intent.putExtra("Category",category);

                    startActivity(intent);
                }
                break;
            case R.id.btn_search_carinfo:
                String strMaker=spinnerBrand.getSelectedItem().toString();
                String strModel=spinnerModel.getSelectedItem().toString();
                if(!strMaker.equalsIgnoreCase("Select Car Maker")) {
                    Intent intentCar = new Intent(getActivity(), CarInfoWebView.class);
                    intentCar.putExtra("Model", strMaker);
                    startActivity(intentCar);
                }
                break;
        }
    }

    private class DealerPageAdapter extends PagerAdapter{
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;

        DealerPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.dealer_home_pager_raw, container,
                    false);

           imgPager= (ImageView) itemView.findViewById(R.id.dealer_home_pager_tv);

           if(position==1){
               imgPager.setBackgroundResource(R.drawable.ic_carinfo);
           }
            if(position==2){
                imgPager.setBackgroundResource(R.drawable.ic_new_old_car);
            }
            if(position==0){
                imgPager.setBackgroundResource(R.drawable.ic_business);
            }


            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        /*public float getPageWidth(int position)
        {
            return .9f;
        }*/

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class BigImage implements ViewPager.PageTransformer{

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            if (position < -1) { // [-Infinity,-1)

                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);


            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                // page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                //page.setBackground(getR.drawable.ball2);

                page.clearAnimation();
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
//            view.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);

            }
        }
    }


    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&language=en");
            sb.append("components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "mmmmmmmm", e);
            return resultList;
        }

        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        System.out.println(resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    /*private class GetAllLogo extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            objCarInfoAdapter=new CarInfoAdapter(getActivity(),params[0],params[1]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                gridview.setAdapter(objCarInfoAdapter);
            }
            catch (NullPointerException e){

            }catch (Exception e){

            }
        }
    }*/


    private class GetModels extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objFilterBL.getModelData(params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                ArrayAdapter<String> adapterVehicleType = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, objFilterBL.listModel);
                adapterVehicleType.setDropDownViewResource(R.layout.spinner_item);
                spinnerModel.setAdapter(adapterVehicleType);

            }
            catch (Exception e) {
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }


}
