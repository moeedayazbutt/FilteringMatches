package com.excercise.filteringmatches.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.excercise.filteringmatches.R;
import com.excercise.filteringmatches.core.ActivityBase;
import com.excercise.filteringmatches.fragments.FragmentMatchesList;

/**
 */

public class ActivityMain extends ActivityBase{

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        showMainContent();
    }

    private void showMainContent(){
        addFragment(new FragmentMatchesList());
    }

    public FloatingActionButton getFloatingActionButton(){
        return this.fab;
    }
}
