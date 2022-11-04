package com.graphicless.modeltest.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.graphicless.modeltest.OnSwipeTouchListener;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.adapter.PageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModelTestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModelTestListFragment extends Fragment {
    
    public static final String TAG = "mtlf";

    TabLayout tabLayout;
    TabItem tabItem1,tabItem2,tabItem3;
    ViewPager viewPager;
    PageAdapter pageAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModelTestListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ModelTestListFragment: ");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModelTestListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModelTestListFragment newInstance(String param1, String param2) {
        ModelTestListFragment fragment = new ModelTestListFragment();
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
        View view =  inflater.inflate(R.layout.fragment_model_test_list, container, false);

        tabLayout = view.findViewById(R.id.tablayout1);
        tabItem1 = view.findViewById(R.id.tab1);
        tabItem2 = view.findViewById(R.id.tab2);
        tabItem3 = view.findViewById(R.id.tab3);
        viewPager = view.findViewById(R.id.vpager);


        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pageAdapter);

//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected: ");
            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Log.d(TAG, "onSwipeRight: ");
                requireActivity().getSupportFragmentManager().popBackStack();

                Toast.makeText(getContext(), "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}