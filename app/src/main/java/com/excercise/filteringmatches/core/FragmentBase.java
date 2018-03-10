package com.excercise.filteringmatches.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.excercise.filteringmatches.R;
import com.excercise.filteringmatches.activities.ActivityMain;


/**
 * Created by Khurram Shehzad on 6/24/2016.
 */
public class FragmentBase extends Fragment {
    protected Activity activity;
    protected View contentView;
    protected FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        contentView = activity.findViewById(R.id.container_fragment_activity_main);
        floatingActionButton = ((ActivityMain)activity).getFloatingActionButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected boolean addFragmentOnTop(Fragment fragment){
        return ((ActivityBase)activity).addFragmentOnTop(fragment);
    }

    protected void onBackPressed(){
        activity.onBackPressed();
    }
}
