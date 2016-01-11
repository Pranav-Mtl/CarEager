package com.careager;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careager.Adapter.ArticleQuestionAdapter;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.UI.RecyclerItemClickListener;
import com.careager.careager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFragment extends Fragment {

    RecyclerView recList;



    ArticleQuestionAdapter objArticleQuestionAdapter;


    public UpdatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user, container, false);

        initialize(view);

        if(Util.isInternetConnection(getActivity()))
            new GetArticleQuestion().execute();

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        startActivity(new Intent(getActivity(), ArticleQuestionDetail.class).putExtra("ID", Constant.articleID[position]));

                    }

                }));
        return view;
    }


    private void initialize(View view){

        recList = (RecyclerView) view.findViewById(R.id.article_question_list);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


    }

    private class GetArticleQuestion extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            objArticleQuestionAdapter=new ArticleQuestionAdapter(getActivity());
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                recList.setAdapter(objArticleQuestionAdapter);
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {

            }
        }
    }



}
