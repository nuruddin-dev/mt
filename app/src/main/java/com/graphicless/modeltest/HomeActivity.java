package com.graphicless.modeltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.graphicless.modeltest.adapter.StudyCategoryAdapterOuter;
import com.graphicless.modeltest.databinding.ActivityHomeBinding;
import com.graphicless.modeltest.fragment.ModelTestFragment;
import com.graphicless.modeltest.fragment.QuestionCategoryFragment;
import com.graphicless.modeltest.model.InnerDataModel;
import com.graphicless.modeltest.model.MiddleDataModel;
import com.graphicless.modeltest.model.OuterDataModel;
import com.graphicless.modeltest.model.SingleItemModel;
import com.graphicless.modeltest.network.NetworkUtils;

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

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "home";

    private ActivityHomeBinding binding;

    String category, subCategory, unit;

    List<OuterDataModel> outerDataModels = new ArrayList<>();
    List<MiddleDataModel> middleDataModels = new ArrayList<>();
    List<InnerDataModel> innerDataModels = new ArrayList<>();

    RecyclerView rvQuestionCategory;
    StudyCategoryAdapterOuter studyCategoryAdapterOuter;

    Handler handler = new Handler();
    ProgressDialog progressDialog;

    public Dialog questionCategoryDialog;
    Dialog optionDialog;
    public Dialog confirmationDialog;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public static String protocol = "https";
    public static String host = "nuruddin-dev.github.io";
    public static String repo = "/testingapi/";
    public static String studyCategoryUrl = "";
    public static String questionCategoryUrl = "";

    public static String fragment = "None";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Check if questionCategory is not saved, then show question dialog to save the category
        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(!sharedPreferences.contains("unit")){
            showStudyCategoryDialog();
        }else{

            category = sharedPreferences.getString("category", null);
            subCategory = sharedPreferences.getString("subCategory", null);
            unit = sharedPreferences.getString("unit", null);
            studyCategoryUrl = category + "/" + subCategory + "/" + unit + "/";
            Toast.makeText(this, "You are taking preparation for: " + category + " > " + subCategory + " > " + unit, Toast.LENGTH_LONG).show();
        }


        optionDialog = new Dialog(this);
        optionDialog.setContentView(R.layout.dialog_option);
        optionDialog.getWindow().setGravity(Gravity.TOP | Gravity.END);
        optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        binding.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionDialog.show();
                optionDialog.findViewById(R.id.dialog_option).setVisibility(View.VISIBLE);
            }
        });

        optionDialog.findViewById(R.id.select_question_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionDialog.cancel();
                try {
                    questionCategoryDialog.show();
                }catch (NullPointerException e){
                    showStudyCategoryDialog();
                }
            }
        });

        optionDialog.findViewById(R.id.theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionDialog.findViewById(R.id.theme_expandable).getVisibility() == View.GONE)
                    optionDialog.findViewById(R.id.theme_expandable).setVisibility(View.VISIBLE);
                else
                    optionDialog.findViewById(R.id.theme_expandable).setVisibility(View.GONE);
            }
        });

        binding.examByTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(studyCategoryUrl, "")){
                    showStudyCategoryDialog();
                }
                else{
                    startQuestionCategoryFragment();
                }

            }
        });

        binding.modelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(studyCategoryUrl, "")){
                    showStudyCategoryDialog();
                }
                else{
                    startModelTestFragment();
                }

            }
        });


    }

//    String getStudyCategoryUrlPart(){
//        String category = sharedPreferences.getString("category", null);
//        String subCategory = sharedPreferences.getString("subCategory", null);
//        String unit = sharedPreferences.getString("unit", null);
//        return category + "/" + subCategory + "/" + unit;
//    }


    private void startModelTestFragment(){
        ModelTestFragment mtf = new ModelTestFragment();
//        mtf.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.pop_enter_from_left, R.anim.pop_exit_to_right)
                .add(R.id.fragment_holder, mtf, "MTF")
                .addToBackStack("ModelTestFragment")
                .commit();
    }

    private void startQuestionCategoryFragment() {

        String from = "HomeActivity";
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        QuestionCategoryFragment qcf = new QuestionCategoryFragment();
        qcf.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.pop_enter_from_left, R.anim.pop_exit_to_right)
                .add(R.id.fragment_holder, qcf, "QCF")
                .addToBackStack("QCF")
                .commit();
    }

    private void showStudyCategoryDialog() {

        boolean connection =  NetworkUtils.isNetworkConnected(this);

        if(connection){
            questionCategoryDialog = new Dialog(this);
            questionCategoryDialog.setContentView(R.layout.dialog_study_category);
            questionCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            questionCategoryDialog.setCanceledOnTouchOutside(false);
            initRecyclerView();
            new fetchStudyCategory().start();
        }else{
            Toast.makeText(this, "You need a internet connection to get question type", Toast.LENGTH_SHORT).show();
        }
        
    }

    public void restartFragment(int fragment){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(fragment);
        assert currentFragment != null;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder, currentFragment).addToBackStack("new").commit();
//        getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
//        if(getSupportFragmentManager().findFragmentById(fragment) == null){
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder, currentFragment).addToBackStack("new").commit();
//            Log.d(TAG, "restartFragment: ");
//        }

    }

    public void showConfirmationDialog(String message){
        confirmationDialog = new Dialog(this);
        confirmationDialog.setContentView(R.layout.dialog_confirmation);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmationDialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView) confirmationDialog.findViewById(R.id.tv_message);
        textView.setText(message);
        questionCategoryDialog.hide();
        confirmationDialog.show();

        confirmationDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog.dismiss();
                questionCategoryDialog.show();
            }
        });

        confirmationDialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog.dismiss();
                questionCategoryDialog.hide();
                if(Objects.equals(fragment, "QuestionCategoryFragment")){
                    startQuestionCategoryFragment();
                }
            }
        });
    }

    class fetchStudyCategory extends Thread{


        @Override
        public void run() {
            Log.d(TAG, "run: called");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setMessage("Getting Categories...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
//                URL url = new URL(baseUrl + "study-category");

                String file = repo + "study-category";
                URL url = new URL(protocol, host, file);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                String data = "";
                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                Log.d(TAG, "run: " + data);
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
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
//                        Random random = new Random();
//                        int color = Color.argb(255, random.nextInt(140-120)+120, random.nextInt(140-120)+120, random.nextInt(140-120)+120);
//                        int color = Color.argb(255, 30, 30, 30);

                        int color = getResources().getColor(R.color.button_color_common);
                        studyCategoryAdapterOuter.updateData(color,outerDataModels);
                        questionCategoryDialog.show();

                    }
                }
            });

        }
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called");
        rvQuestionCategory = questionCategoryDialog.findViewById(R.id.rv_question_category);
        studyCategoryAdapterOuter = new StudyCategoryAdapterOuter(this, outerDataModels);
        rvQuestionCategory.setAdapter(studyCategoryAdapterOuter);
        rvQuestionCategory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();

        questionCategoryDialog.dismiss();
    }
}