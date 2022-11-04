package com.graphicless.modeltest.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.adapter.ModelTestListAdapter;
import com.graphicless.modeltest.model.InnerDataModel;
import com.graphicless.modeltest.model.MiddleDataModel;
import com.graphicless.modeltest.model.ModelTestListModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModelTestListFragmentAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModelTestListFragmentAll extends Fragment {

    public static final String TAG = "mtfa";

    RecyclerView recyclerView;

    Handler handler = new Handler();
    ProgressDialog progressDialog;

    List<ModelTestListModel> models = new ArrayList<>();
    ModelTestListAdapter modelTestListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModelTestListFragmentAll() {
        // Required empty public constructor
        Log.d(TAG, "ModelTestListFragmentAll: ");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModelTestListFragmentAll.
     */
    // TODO: Rename and change types and number of parameters
    public static ModelTestListFragmentAll newInstance(String param1, String param2) {
        ModelTestListFragmentAll fragment = new ModelTestListFragmentAll();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_model_test_list_all, container, false);

        recyclerView = view.findViewById(R.id.rv_model_test_list);
        modelTestListAdapter = new ModelTestListAdapter(models);
        recyclerView.setAdapter(modelTestListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if(models.size() == 0)
            new fetchTestList().start();
        else
            modelTestListAdapter.update(models, getResources().getColor(R.color.background_two));

        Log.d(TAG, "onCreateView: models: " + models.toString());

        return view;
    }

    class fetchTestList extends Thread{


        @Override
        public void run() {
            Log.d(TAG, "run: called");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(requireActivity());
                    progressDialog.setMessage("Getting Lists...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {

                String file = HomeActivity.repo + HomeActivity.studyCategoryUrl + HomeActivity.questionCategoryUrl + "test-list";
                Log.d(TAG, "run: file: "+ file);
                URL url = new URL(HomeActivity.protocol, HomeActivity.host, file);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                String data = "";
                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                models.clear();

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray testArray = jsonObject.getJSONArray("tests");
                    for(int i=0; i<testArray.length(); i++){
                        JSONObject categoryObj = testArray.getJSONObject(i);
                        String name = categoryObj.getString("name");
                        String address = categoryObj.getString("address");

                        ModelTestListModel m = new ModelTestListModel(name, address);
                        models.add(m);
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

                        int color = getResources().getColor(R.color.background_two);
                        modelTestListAdapter.update(models, color);

                    }
                }
            });

        }
    }
}