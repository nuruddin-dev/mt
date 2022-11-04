package com.graphicless.modeltest.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.graphicless.modeltest.OnSwipeTouchListener;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.adapter.GridviewAdapter;
import com.graphicless.modeltest.model.SingleItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModelTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModelTestFragment extends Fragment {

    public static final String TAG = "mtf";

    boolean horizontal = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModelTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModelTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModelTestFragment newInstance(String param1, String param2) {
        ModelTestFragment fragment = new ModelTestFragment();
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

        View view = inflater.inflate(R.layout.fragment_model_test, container, false);

        int imageOne = R.drawable.icon_chapter;
        int imageTwo = R.drawable.icon_subject;
        int imageThree = R.drawable.icon_model_test;
        int imageFour = R.drawable.icon_special_model_test;
        int imageFive = R.drawable.icon_quiz;
        int imageSix = R.drawable.icon_question_bank;

        String nameOne = "By Topic";
        String nameTwo = "By Subject";
        String nameThree = "Full Model Test";
        String nameFour = "Special Model Test";
        String nameFive = "Quiz";
        String nameSix = "Question Bank";


        List<SingleItemModel> items = new ArrayList<>();
        items.add(new SingleItemModel(imageOne, nameOne));
        items.add(new SingleItemModel(imageTwo, nameTwo));
        items.add(new SingleItemModel(imageThree, nameThree));
        items.add(new SingleItemModel(imageFour, nameFour));
        items.add(new SingleItemModel(imageFive, nameFive));
        items.add(new SingleItemModel(imageSix, nameSix));


        GridView gridView = view.findViewById(R.id.gv_offers);
        ImageButton button = view.findViewById(R.id.btn_grid_change);

        gridView.setAdapter(new GridviewAdapter(items, getContext(), "vertical", requireActivity()));
        gridView.setNumColumns(1);

        button.setOnClickListener(view1 -> {
            horizontal = !horizontal;

            if(horizontal){
                gridView.setAdapter(new GridviewAdapter(items, getContext(), "horizontal", requireActivity()));
                gridView.setNumColumns(3);
                button.setImageResource(R.drawable.icon_grid_vertical);
            }
            else{
                gridView.setAdapter(new GridviewAdapter(items, getContext(), "vertical", requireActivity()));
                gridView.setNumColumns(1);
                button.setImageResource(R.drawable.icon_grid_horizontal);
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                requireActivity().getSupportFragmentManager().popBackStack();

                Toast.makeText(getContext(), "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}