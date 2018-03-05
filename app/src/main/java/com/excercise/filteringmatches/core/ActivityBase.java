package com.excercise.filteringmatches.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.excercise.filteringmatches.R;

/**
 */
public class ActivityBase extends AppCompatActivity {

    protected boolean addFragmentOnTop(Fragment fragment){
        boolean returnValue = false;
        if(fragment != null){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_fade_out,R.anim.anim_fade_in, R.anim.anim_exit_to_right);
            transaction.replace(R.id.container_fragment_activity_main, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            returnValue = true;
        }
        return returnValue;
    }

    protected boolean addFragment(Fragment fragment){
        boolean returnValue = false;
        if(fragment != null){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
            transaction.replace(R.id.container_fragment_activity_main, fragment);
            transaction.commit();

            returnValue = true;
        }
        return returnValue;
    }

    protected void clearFragmentBackStack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        int fragmentsCount = fragmentManager.getBackStackEntryCount();
        if(fragmentsCount > 0){
            for(int i = 0; i < fragmentsCount; ++i) {
                fragmentManager.popBackStack();
            }
        }
    }
}
