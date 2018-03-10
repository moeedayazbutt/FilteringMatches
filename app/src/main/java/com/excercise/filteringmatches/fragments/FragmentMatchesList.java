package com.excercise.filteringmatches.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.excercise.filteringmatches.R;
import com.excercise.filteringmatches.activities.ActivityMain;
import com.excercise.filteringmatches.adapters.AdapterMatches;
import com.excercise.filteringmatches.core.FragmentBase;
import com.excercise.filteringmatches.interfaces.AndroidRuntimePermissionCallBack;
import com.excercise.filteringmatches.interfaces.ClickListenerRecyclerView;
import com.excercise.filteringmatches.interfaces.RequestKnownLocationCallBack;
import com.excercise.filteringmatches.models.City;
import com.excercise.filteringmatches.models.Match;
import com.excercise.filteringmatches.utils.ManagerJson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.bendik.simplerangeview.SimpleRangeView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.excercise.filteringmatches.constants.Constants.MY_PERMISSIONS_REQUEST_GET_LOCATION;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_DATA_ROOT_KEY;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_FILE_NAME;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_AGE;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_CITY;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_COMPATIBILITY_SCORE;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_CONTACTS_EXCHANGE;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_DISPLAY_NAME;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_FAVOURITE;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_HEIGHT_IN_CM;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_JOB_TITLE;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_LAT;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_LON;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_MAIN_PHOTO;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_NAME;
import static com.excercise.filteringmatches.constants.JsonConstants.JSON_KEY_RELIGION;
import static com.excercise.filteringmatches.utils.ManagerJson.loadJSONFromAsset;

/**
 * Created by khurr on 3/4/2018.
 */

public class FragmentMatchesList extends FragmentBase implements AndroidRuntimePermissionCallBack, RequestKnownLocationCallBack{

    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Snackbar filterSnackbar;

    private ArrayList<Match> matchList = new ArrayList<>();
    private ArrayList<Match> filteredMatchList;
    private boolean isDistanceFilterEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.setTitle("Matches");

        initViews();

        new LoadJsonData().execute("");
    }

    private void initViews(){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rvMatches);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ClickListenerRecyclerView.addTo(mRecyclerView).setOnItemClickListener(new ClickListenerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

        ClickListenerRecyclerView.addTo(mRecyclerView).setOnItemLongClickListener(new ClickListenerRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                return true;
            }
        });

        if(floatingActionButton != null){
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(clickListenerFloatingActionButton);
        }
        initFilterView();
    }

    private View.OnClickListener clickListenerFloatingActionButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            floatingActionButton.setVisibility(View.GONE);
            filterSnackbar.show();
        }
    };

    private View.OnClickListener clickListenerCancelFilterButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            filterSnackbar.dismiss();
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener clickListenerResetFilterButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            swtPhoto.setChecked(false);
            swtContact.setChecked(false);
            swtFavourite.setChecked(false);

            cbPhoto.setChecked(false);
            cbContact.setChecked(false);
            cbFavourite.setChecked(false);
            cbAge.setChecked(false);
            cbCompatibilityScore.setChecked(false);
            cbHeight.setChecked(false);
            cbDistance.setChecked(false);

            rngHeight.setStart(0);
            rngHeight.setEnd(75);
            rngAge.setStart(0);
            rngAge.setEnd(77);
            rngCompatibilityScore.setStart(0);
            rngCompatibilityScore.setEnd(98);
            npDistance.setValue(30);

            ((AdapterMatches) mAdapter).updateData(matchList);
            btnCancelFilter.callOnClick();
        }
    };

    private View.OnClickListener clickListenerApplyFilterButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressDialog.show();
            filteredMatchList = new ArrayList<>();
            filteredMatchList.addAll(matchList);

            if(cbContact.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if (swtContact.isChecked()) {
                            if (match.getContactsExchanged() == null || match.getContactsExchanged() <= 0) {
                                tempFilteredMatchList.remove(match);
                            }
                        }else {
                            if (match.getContactsExchanged() != null && match.getContactsExchanged() > 0) {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbFavourite.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if (swtFavourite.isChecked()) {
                            if (!match.isFavourite()) {
                                tempFilteredMatchList.remove(match);
                            }
                        }else {
                            if (match.isFavourite()) {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbPhoto.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if (swtPhoto.isChecked()) {
                            if (match.getMainPhoto() == null) {
                                tempFilteredMatchList.remove(match);
                            }
                        }else {
                            if (match.getMainPhoto() != null) {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbCompatibilityScore.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                double startRange = rngCompatibilityScore.getStart() + 1;
                double endRange = rngCompatibilityScore.getEnd() + 1;
                startRange = startRange / 100;
                endRange = endRange / 100;

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if(match.getCompatibilityScore() != null){
                            if(match.getCompatibilityScore() >= startRange && match.getCompatibilityScore() <= endRange){

                            }
                            else {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                        else {
                            tempFilteredMatchList.remove(match);
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbAge.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                double startRange = rngAge.getStart() + 18;
                double endRange = rngAge.getEnd() + 18;

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if(match.getAge() != null){
                            if(match.getAge() >= startRange && match.getAge() <= endRange){

                            }
                            else {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                        else {
                            tempFilteredMatchList.remove(match);
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbHeight.isChecked()){
                ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
                tempFilteredMatchList.addAll(filteredMatchList);

                double startRange = rngHeight.getStart() + 135;
                double endRange = rngHeight.getEnd() + 135;

                for (int index = 0; index < filteredMatchList.size(); index++) {
                    Match match = filteredMatchList.get(index);
                    if (match != null) {
                        if(match.getHeightInCm() != null){
                            if(match.getHeightInCm() >= startRange && match.getHeightInCm() <= endRange){

                            }
                            else {
                                tempFilteredMatchList.remove(match);
                            }
                        }
                        else {
                            tempFilteredMatchList.remove(match);
                        }
                    }
                }

                filteredMatchList = new ArrayList<>();
                filteredMatchList.addAll(tempFilteredMatchList);
            }
            if(cbDistance.isChecked()){
                calculateDistanceInKM(((ActivityMain)activity).isPermissionGranted(ACCESS_FINE_LOCATION));
            }
            else {
                ((AdapterMatches) mAdapter).updateData(filteredMatchList);
                btnCancelFilter.callOnClick();
                progressDialog.dismiss();
            }
        }
    };

    private View snackView;
    private NumberPicker npDistance;
    private Button btnCancelFilter, btnApplyFilter, btnResetFilter;
    private CheckBox cbFavourite, cbContact, cbPhoto, cbCompatibilityScore, cbAge, cbHeight, cbDistance;
    private Switch swtFavourite, swtContact, swtPhoto;
    private SimpleRangeView rngCompatibilityScore, rngAge, rngHeight;
    private TextView tvStartRangeCompatibilityScore, tvEndRangeCompatibilityScore, tvStartRangeAge, tvEndRangeAge, tvStartRangeHeight, tvEndRangeHeight;

    private void initFilterView(){
        filterSnackbar = Snackbar.make(contentView, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) filterSnackbar.getView();

        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        snackView = activity.getLayoutInflater().inflate(R.layout.layout_filters, null);

        npDistance = (NumberPicker) snackView.findViewById(R.id.npDistance);

        rngCompatibilityScore = (SimpleRangeView) snackView.findViewById(R.id.rngCompatibilityScore);
        rngAge = (SimpleRangeView) snackView.findViewById(R.id.rngAge);
        rngHeight = (SimpleRangeView) snackView.findViewById(R.id.rngHeight);

        tvStartRangeCompatibilityScore = (TextView) snackView.findViewById(R.id.tvStartRangeCompatibilityScore);
        tvEndRangeCompatibilityScore = (TextView) snackView.findViewById(R.id.tvEndRangeCompatibilityScore);
        tvStartRangeAge = (TextView) snackView.findViewById(R.id.tvStartRangeAge);
        tvEndRangeAge = (TextView) snackView.findViewById(R.id.tvEndRangeAge);
        tvStartRangeHeight = (TextView) snackView.findViewById(R.id.tvStartRangeHeight);
        tvEndRangeHeight = (TextView) snackView.findViewById(R.id.tvEndRangeHeight);

        cbFavourite = (CheckBox) snackView.findViewById(R.id.cbFavourite);
        cbContact = (CheckBox) snackView.findViewById(R.id.cbContact);
        cbPhoto = (CheckBox) snackView.findViewById(R.id.cbPhoto);
        cbCompatibilityScore = (CheckBox) snackView.findViewById(R.id.cbCompatibilityScore);
        cbAge = (CheckBox) snackView.findViewById(R.id.cbAge);
        cbHeight = (CheckBox) snackView.findViewById(R.id.cbHeight);
        cbDistance = (CheckBox) snackView.findViewById(R.id.cbDistance);

        swtFavourite = (Switch) snackView.findViewById(R.id.swtFavourite);
        swtContact = (Switch) snackView.findViewById(R.id.swtContact);
        swtPhoto = (Switch) snackView.findViewById(R.id.swtPhoto);

        btnCancelFilter = (Button) snackView.findViewById(R.id.btnCancelFilter);
        btnApplyFilter = (Button) snackView.findViewById(R.id.btnApplyFilter);
        btnResetFilter = (Button) snackView.findViewById(R.id.btnResetFilter);

        btnCancelFilter.setOnClickListener(clickListenerCancelFilterButton);
        btnApplyFilter.setOnClickListener(clickListenerApplyFilterButton);
        btnResetFilter.setOnClickListener(clickListenerResetFilterButton);

        npDistance.setMinValue(30);
        npDistance.setMaxValue(300);

        cbDistance.setOnCheckedChangeListener(cbDistanceChangeListener);

        layout.addView(snackView, 0);

        rngCompatibilityScore.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                tvStartRangeCompatibilityScore.setText(String.valueOf(start + 1));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                tvEndRangeCompatibilityScore.setText(String.valueOf(end + 1));
            }
        });
        rngAge.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                tvStartRangeAge.setText(String.valueOf(start + 18));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                tvEndRangeAge.setText(String.valueOf(end + 18));
            }
        });
        rngHeight.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                tvStartRangeHeight.setText(String.valueOf(start + 135));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                tvEndRangeHeight.setText(String.valueOf(end + 135));
            }
        });
    }

    @Override
    public void onRequestedPermissionGranted(String[] permissions, int callBackInt) {

    }

    @Override
    public void onRequestedPermissionDenied(String[] permissions, int callBackInt) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(isDistanceFilterEnabled) {
            ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
            tempFilteredMatchList.addAll(filteredMatchList);

            int distanceUserValue = npDistance.getValue();

            for (int index = 0; index < filteredMatchList.size(); index++) {
                Match match = filteredMatchList.get(index);
                if (match != null && match.getCity() != null && match.getCity().getLat() != null && match.getCity().getLon() != null) {
                    int distance = distanceBetweenPoints(location.getLatitude(), location.getLongitude(), match.getCity().getLat(), match.getCity().getLon());
                    if (distanceUserValue == distance) {

                    } else {
                        tempFilteredMatchList.remove(match);
                    }
                }
            }

            filteredMatchList = new ArrayList<>();
            filteredMatchList.addAll(tempFilteredMatchList);

            ((AdapterMatches) mAdapter).updateData(filteredMatchList);
            btnCancelFilter.callOnClick();
            progressDialog.dismiss();

            isDistanceFilterEnabled = false;
        }
    }

    private int distanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat_a);
        locationA.setLongitude(lng_a);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat_b);
        locationB.setLongitude(lng_b);

        return (int) (locationA.distanceTo(locationB) / 1000);
    }

    private class LoadJsonData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject matches_jObj = new JSONObject(loadJSONFromAsset(activity.getAssets(), JSON_FILE_NAME));
                JSONArray matches_jArray = matches_jObj.getJSONArray(JSON_DATA_ROOT_KEY);

                for (int i = 0; i < matches_jArray.length(); i++) {
                    JSONObject match_jObj = matches_jArray.getJSONObject(i);

                    String displayName = ManagerJson.getString(match_jObj, JSON_KEY_DISPLAY_NAME);
                    Integer age = ManagerJson.getInt(match_jObj, JSON_KEY_AGE);
                    String jobTitle = ManagerJson.getString(match_jObj, JSON_KEY_JOB_TITLE);
                    Integer heightInCM = ManagerJson.getInt(match_jObj, JSON_KEY_HEIGHT_IN_CM);
                    String mainPhoto = ManagerJson.getString(match_jObj, JSON_KEY_MAIN_PHOTO);
                    Double compatibilityScore = ManagerJson.getDouble(match_jObj, JSON_KEY_COMPATIBILITY_SCORE);
                    Integer contactsExchange = ManagerJson.getInt(match_jObj, JSON_KEY_CONTACTS_EXCHANGE);
                    boolean favourites = ManagerJson.getBoolean(match_jObj, JSON_KEY_FAVOURITE);
                    String religion = ManagerJson.getString(match_jObj, JSON_KEY_RELIGION);

                    JSONObject city_jObj = match_jObj.getJSONObject(JSON_KEY_CITY);

                    String name = ManagerJson.getString(city_jObj, JSON_KEY_NAME);
                    Double lat = ManagerJson.getDouble(city_jObj, JSON_KEY_LAT);
                    Double lon = ManagerJson.getDouble(city_jObj, JSON_KEY_LON);

                    matchList.add(new Match(displayName, age, jobTitle, heightInCM, mainPhoto, compatibilityScore, contactsExchange, favourites, religion, new City(name, lat, lon)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mAdapter = new AdapterMatches(matchList, activity);
            mRecyclerView.setAdapter(mAdapter);
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private CompoundButton.OnCheckedChangeListener cbDistanceChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                ActivityMain activityMain = (ActivityMain) activity;
                if (activityMain != null) {
                    if (activityMain.isPermissionGranted(ACCESS_FINE_LOCATION)) {
                        Toast.makeText(activityMain, "Permission already Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        activityMain.setPermissionCallBack(FragmentMatchesList.this);
                        activityMain.requestRuntimePermission(ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_GET_LOCATION);
                    }
                }
            }
        }
    };

    private void calculateDistanceInKM(boolean isPermissionGranted){
        if(isPermissionGranted){
            ActivityMain activityMain = (ActivityMain) activity;
            if (activityMain != null) {
                activityMain.setLocationCallBack(FragmentMatchesList.this);
                activityMain.requestCurrentKnownLocation();
                isDistanceFilterEnabled = true;
            }
        }
        else {
            Match assumedLoginUser = matchList.get(0);
            ArrayList<Match> tempFilteredMatchList = new ArrayList<>();
            tempFilteredMatchList.addAll(filteredMatchList);

            int distanceUserValue = npDistance.getValue();

            for (int index = 0; index < filteredMatchList.size(); index++) {
                Match match = filteredMatchList.get(index);
                if (match != null && match.getCity() != null && match.getCity().getLat() != null && match.getCity().getLon() != null) {
                    int distance = distanceBetweenPoints(assumedLoginUser.getCity().getLat(), assumedLoginUser.getCity().getLon(), match.getCity().getLat(), match.getCity().getLon());
                    if (distanceUserValue == distance) {

                    } else {
                        tempFilteredMatchList.remove(match);
                    }
                }
            }

            filteredMatchList = new ArrayList<>();
            filteredMatchList.addAll(tempFilteredMatchList);

            ((AdapterMatches) mAdapter).updateData(filteredMatchList);
            btnCancelFilter.callOnClick();
            progressDialog.dismiss();
        }
    }
}
