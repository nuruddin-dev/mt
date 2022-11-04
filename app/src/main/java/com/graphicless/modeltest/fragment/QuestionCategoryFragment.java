package com.graphicless.modeltest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.OnSwipeTouchListener;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.adapter.QuestionCategoryAdapterOuter;
import com.graphicless.modeltest.model.InnerDataModel;
import com.graphicless.modeltest.model.MiddleDataModel;
import com.graphicless.modeltest.model.OuterDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionCategoryFragment extends Fragment {
    
    public static final String TAG = "qcf";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    List<OuterDataModel> outerDataModels = new ArrayList<>();
    Handler handler = new Handler();

    ProgressDialog progressDialog;
    String studyCategoryUrlPart, from;

    RelativeLayout body;

    QuestionCategoryAdapterOuter questionCategoryAdapterOuter;

    List<InnerDataModel> innerDataModels = new ArrayList<>();
    List<MiddleDataModel> middleDataModels = new ArrayList<>();

    public QuestionCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionCategoryFragment newInstance(String param1, String param2) {
        QuestionCategoryFragment fragment = new QuestionCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        from = getArguments().getString("from", "");

        studyCategoryUrlPart = HomeActivity.studyCategoryUrl;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_question_category, container, false);

//        body = view.findViewById(R.id.body);
        recyclerView = view.findViewById(R.id.rv_question_type);
        questionCategoryAdapterOuter = new QuestionCategoryAdapterOuter(outerDataModels, QuestionCategoryFragment.this);
        recyclerView.setAdapter(questionCategoryAdapterOuter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new fetchQuestionType().start();

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                requireActivity().getSupportFragmentManager().popBackStack();

                Toast.makeText(getContext(), "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    class fetchQuestionType extends Thread{


        @Override
        public void run() {


//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog = new ProgressDialog(getContext());
//                    progressDialog.setMessage("Getting Categories...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                }
//            });

            try {

                String file = HomeActivity.repo + studyCategoryUrlPart + "/question-type"; // [/api/university-admission/dhaka-university/a-unit/question-type]

                URL url = new URL(HomeActivity.protocol, HomeActivity.host, file);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                String data = "";
                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray categoryArray = jsonObject.getJSONArray("categories");
                    for(int i=0; i<categoryArray.length(); i++){
                        JSONObject categoryObj = categoryArray.getJSONObject(i);
                        String category = categoryObj.getString("category");

                        JSONArray subCategoryArray = categoryObj.getJSONArray("sub-categories");
                        for(int j=0; j<subCategoryArray.length(); j++) {
                            JSONObject subCategoryObj = subCategoryArray.getJSONObject(j);
                            String subCategory = subCategoryObj.getString("sub-category");

                            JSONArray unitArray = subCategoryObj.getJSONArray("units");
                            for(int k=0; k<unitArray.length(); k++){
                                JSONObject unitObj = unitArray.getJSONObject(k);
                                String unit = unitObj.getString("unit");
                                InnerDataModel idm = new InnerDataModel(unit);
                                innerDataModels.add(idm);
                            }

                            MiddleDataModel mdm = new MiddleDataModel(subCategory, innerDataModels);
                            middleDataModels.add(mdm);
                            innerDataModels = new ArrayList<>();
                        }

                        OuterDataModel odm = new OuterDataModel(category, middleDataModels);
                        outerDataModels.add(odm);
                        middleDataModels = new ArrayList<>();
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
//                    if(progressDialog.isShowing()){
//                        progressDialog.dismiss();
//                      questionCategoryAdapterOuter.updateData(outerDataModels);
//
//                    }
                    questionCategoryAdapterOuter.updateData(outerDataModels);
                }
            });

        }
    }
}