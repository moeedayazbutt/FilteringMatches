package com.excercise.filteringmatches.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.excercise.filteringmatches.R;
import com.excercise.filteringmatches.core.ActivityBase;
import com.excercise.filteringmatches.fragments.FragmentMatchesList;
import com.excercise.filteringmatches.interfaces.AndroidRuntimePermissionCallBack;
import com.excercise.filteringmatches.interfaces.RequestKnownLocationCallBack;

import static com.excercise.filteringmatches.constants.Constants.MY_PERMISSIONS_REQUEST_GET_LOCATION;

/**
 * Created by khurr on 3/3/2018.
 */

public class ActivityMain extends ActivityBase implements LocationListener {

    private FloatingActionButton fab;
    private AndroidRuntimePermissionCallBack permissionCallBack;
    private RequestKnownLocationCallBack locationCallBack;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        showMainContent();
    }

    private void showMainContent() {
        addFragment(new FragmentMatchesList());
    }

    public FloatingActionButton getFloatingActionButton() {
        return this.fab;
    }

    public boolean isPermissionGranted(String permissionType) {
        if (ContextCompat.checkSelfPermission(ActivityMain.this, permissionType)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void requestRuntimePermission(final String permissionType, final int callBackInt) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,
                permissionType)) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_request_permission)
                    .setMessage(R.string.text_request_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(ActivityMain.this,
                                    new String[]{permissionType},
                                    callBackInt);
                        }
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(ActivityMain.this,
                    new String[]{permissionType},
                    callBackInt);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (permissionCallBack != null) {
                        permissionCallBack.onRequestedPermissionGranted(permissions, requestCode);
                    }

                } else {

                    if (permissionCallBack != null) {
                        permissionCallBack.onRequestedPermissionDenied(permissions, requestCode);
                    }
                }
                return;
            }
        }
    }

    public void setPermissionCallBack(AndroidRuntimePermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(locationCallBack != null){
            locationCallBack.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void requestCurrentKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void setLocationCallBack(RequestKnownLocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
    }
}
