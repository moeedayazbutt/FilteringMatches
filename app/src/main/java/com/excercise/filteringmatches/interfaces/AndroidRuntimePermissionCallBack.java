package com.excercise.filteringmatches.interfaces;

/**
 * Created by khurr on 3/10/2018.
 */

public interface AndroidRuntimePermissionCallBack {
    void onRequestedPermissionGranted(String permissions[], int callBackInt);
    void onRequestedPermissionDenied(String permissions[], int callBackInt);
}
