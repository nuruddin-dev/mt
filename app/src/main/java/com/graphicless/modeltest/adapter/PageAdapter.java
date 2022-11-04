package com.graphicless.modeltest.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.graphicless.modeltest.fragment.ModelTestListFragmentAll;
import com.graphicless.modeltest.fragment.ModelTestListFragmentAttempted;
import com.graphicless.modeltest.fragment.ModelTestListFragmentUnattempted;

public class PageAdapter extends FragmentPagerAdapter
{
   int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Log.d("pa", "getItem: " + position);
       switch (position)
       {
           case 0 : return new ModelTestListFragmentAll();
           case 1 : return new ModelTestListFragmentAttempted();
           case 2 : return new ModelTestListFragmentUnattempted();
       }
       return null;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
