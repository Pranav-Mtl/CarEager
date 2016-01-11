package com.careager;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.careager.Adapter.UserCarListAdapter;
import com.careager.careager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserCarFragment extends Fragment implements View.OnClickListener{


    RecyclerView recList;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    UserCarListAdapter objUserCarListAdapter;

    Button btnSearch;

    public UserCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_car, container, false);
        initialize(view);
        objUserCarListAdapter=new UserCarListAdapter(getActivity());
        recList.setAdapter(objUserCarListAdapter);

        return view;
    }

    private void initialize(View view){
        recList = (RecyclerView) view.findViewById(R.id.car_list);
        btnSearch= (Button) view.findViewById(R.id.user_search);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        progressDialog=new ProgressDialog(getActivity());

        btnSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.user_search:
                startActivity(new Intent(getActivity(),SearchCar.class));
                break;
        }

    }
}
