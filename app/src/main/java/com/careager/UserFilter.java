package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.careager.BE.FilterBE;
import com.careager.BL.FilterBL;
import com.careager.careager.R;
import com.efor18.rangeseekbar.RangeSeekBar;

public class UserFilter extends AppCompatActivity implements View.OnClickListener {

    FilterBL objFilterBL;

    Spinner spnBrand,spnModel,spnFuel,spnColor,spnVehicleType;

    ProgressDialog progressDialog;

    Button btnBack,btnApply;

    FilterBE objFilterBE;

     TextView min,max;

    RadioButton rbNew,rbOld;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_filter);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
        initialize();

        new GetFilterData().execute();


        spnBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    String model=objFilterBL.listMakerID.get(position-1);
                    new GetModels().execute(model);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize(){

        objFilterBL=new FilterBL();
        progressDialog=new ProgressDialog(UserFilter.this);

        spnBrand= (Spinner) findViewById(R.id.filter_brand);
        spnColor= (Spinner) findViewById(R.id.filter_color);
        spnFuel= (Spinner) findViewById(R.id.filter_fuel);
        spnModel= (Spinner) findViewById(R.id.filter_model);
        spnVehicleType= (Spinner) findViewById(R.id.filter_vehicle_type);
        btnBack= (Button) findViewById(R.id.filter_back);
        btnApply= (Button) findViewById(R.id.filter_Apply);

        min = (TextView) findViewById(R.id.minValue);
        max = (TextView) findViewById(R.id.maxValue);

        rbNew= (RadioButton) findViewById(R.id.filter_new_car);
        rbOld= (RadioButton) findViewById(R.id.filter_old_car);

        intent=getIntent();
        objFilterBE= (FilterBE) intent.getSerializableExtra("FilterBE");
        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(1,150, this);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values


                min.setText(minValue.toString());
                max.setText(maxValue.toString());

            }
        });

        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        layout.addView(seekBar);

        btnBack.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filter_back:
                finish();
                break;
            case R.id.filter_Apply:
                setFilterOption();
                intent.putExtra("FilterBE",objFilterBE);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    private class GetFilterData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objFilterBL.getFilterData();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            try {

                ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listMaker);
                adapterBrand.setDropDownViewResource(R.layout.spinner_item);
                spnBrand.setAdapter(adapterBrand);

                ArrayAdapter<String> adapterFuel = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listFuel);
                adapterFuel.setDropDownViewResource(R.layout.spinner_item);
                spnFuel.setAdapter(adapterFuel);

                ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listColor);
                adapterColor.setDropDownViewResource(R.layout.spinner_item);
                spnColor.setAdapter(adapterColor);

                ArrayAdapter<String> adapterVehicleType = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listVehicleType);
                adapterVehicleType.setDropDownViewResource(R.layout.spinner_item);
                spnVehicleType.setAdapter(adapterVehicleType);

                ArrayAdapter<String> adapterModel = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listModel);
                adapterModel.setDropDownViewResource(R.layout.spinner_item);
                spnModel.setAdapter(adapterModel);


            }catch (NullPointerException e){

            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

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
                ArrayAdapter<String> adapterVehicleType = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, objFilterBL.listModel);
                adapterVehicleType.setDropDownViewResource(R.layout.spinner_item);
                spnModel.setAdapter(adapterVehicleType);

            }
            catch (Exception e) {
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

    private void setFilterOption(){
        if(rbNew.isChecked()){
            objFilterBE.setCar("New");
        }
        else if(rbOld.isChecked()){
            objFilterBE.setCar("Old");
        }else {
            objFilterBE.setCar("");
        }

        /*------------------*/

        String strMaker=spnBrand.getSelectedItem().toString();
        if(strMaker.equalsIgnoreCase("Select Brand/Maker")){
            objFilterBE.setBrand("");
        }
        else
            objFilterBE.setBrand(strMaker);

        /*----------------------*/

        String strModel=spnModel.getSelectedItem().toString();
        if(strModel.equalsIgnoreCase("Select Model")){
            objFilterBE.setModel("");
        }
        else
            objFilterBE.setModel(strModel);

        /*------------------------*/

        String strFuel=spnFuel.getSelectedItem().toString();
        if(strFuel.equalsIgnoreCase("Select Fuel Type"))
            objFilterBE.setFuel("");
        else
            objFilterBE.setFuel(strFuel);

        /*----------------------------*/

        String strColor=spnColor.getSelectedItem().toString();

        if(strColor.equalsIgnoreCase("Select Color"))
            objFilterBE.setColor("");
        else
            objFilterBE.setColor(strColor);

        /*----------------------------*/
        String strCarType=spnVehicleType.getSelectedItem().toString();
        if(strCarType.equalsIgnoreCase("Select Car Type"))
            objFilterBE.setCarType("");
        else
            objFilterBE.setCarType(strCarType);

        /*----------------------------*/

        objFilterBE.setMinPrice(min.getText().toString());
        objFilterBE.setMaxPrice(max.getText().toString());
    }
}

