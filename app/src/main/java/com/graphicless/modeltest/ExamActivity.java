package com.graphicless.modeltest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.graphicless.modeltest.adapter.ExamAdapter;
import com.graphicless.modeltest.model.QuestionModel;

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

public class ExamActivity extends AppCompatActivity {

    private static final String TAG = "ExamActivity";

    ArrayList<QuestionModel> model;

    Button btn_submit, btnToggleDark;



    String data = "";
    Handler handler = new Handler();
    ProgressDialog progressDialog;

    String api;

    ExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        api = intent.getStringExtra("api");

        initRecyclerView();
        new fetchQuestion().start();

        btn_submit = findViewById(R.id.btn_submit);


//        new theme().start();
        btnToggleDark = findViewById(R.id.btn_theme);
//        SetTheme(isDarkModeOn);

        btnToggleDark.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view)
                    {
//                        ChangeTheme(isDarkModeOn, editor);
//                        ChangeTheme();
                    }

                });
        
        
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExamActivity.this, "result: ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class fetchQuestion extends Thread{


        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(ExamActivity.this);
                    progressDialog.setMessage("Creating Questions");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL(api);
//                URL url = new URL("https://api.npoint.io/4186a8334f06c7af205e");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }


                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("users");

//                    Objects.requireNonNull(getSupportActionBar()).setTitle("new question");

                    for(int i=0; i<users.length(); i++){
                        JSONObject user = users.getJSONObject(i);
                        String question_number = user.getString("question_number");
                        String question = user.getString("question");
                        String imageUrl = user.getString("image");
                        String option_one = user.getString("option_one");
                        String option_two = user.getString("option_two");
                        String option_three = user.getString("option_three");
                        String option_four = user.getString("option_four");
                        String correct_answer = user.getString("correct_answer");
                        QuestionModel m = new QuestionModel(question_number,question, imageUrl, option_one, option_two, option_three, option_four, correct_answer);
                        model.add(m);
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
                        adapter.updateData(model);
                    }
                }
            });

        }
    }

    private void initRecyclerView()
    {
        Log.d(TAG, "initRecyclerView: initRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        model = new ArrayList<>();
        adapter = new ExamAdapter(this, model);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}

