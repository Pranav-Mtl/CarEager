package com.careager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.careager.Constant.Constant;
import com.careager.careager.R;

public class HowItWork extends AppCompatActivity implements View.OnClickListener {

    RadioGroup groupRadio;
    Button btnSignup,btnSignin,btnSkip;
    RadioButton Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_work);

        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_it_work, menu);
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
        groupRadio= (RadioGroup) findViewById(R.id.hiw_radio_group);
        btnSignup= (Button) findViewById(R.id.hiw_signup);
        btnSignin= (Button) findViewById(R.id.hiw_signin);
        btnSkip= (Button) findViewById(R.id.hiw_skip);

        btnSignin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int selectedGender=groupRadio.getCheckedRadioButtonId();
        Gender= (RadioButton) findViewById(selectedGender);
        switch (v.getId()){
            case R.id.hiw_signup:
                if(Gender.getText().toString().equalsIgnoreCase(Constant.strLoginBusiness)){
                    startActivity(new Intent(getApplicationContext(),DealerSignupCategory.class).putExtra("UserType",Gender.getText().toString()));
                }
                else {
                    startActivity(new Intent(getApplicationContext(),UserSignup.class).putExtra("UserType",Gender.getText().toString()));
                }
                break;
            case R.id.hiw_signin:
                startActivity(new Intent(getApplicationContext(),UserLogin.class).putExtra("UserType",Gender.getText().toString()));
                break;
            case R.id.hiw_skip:
                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
