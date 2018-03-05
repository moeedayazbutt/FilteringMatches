package com.excercise.filteringmatches.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.excercise.filteringmatches.R;
import com.excercise.filteringmatches.models.Match;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class AdapterMatches extends RecyclerView.Adapter<AdapterMatches.ViewHolder> {

    private ArrayList<Match> matchList;
    private Activity activity;

    private final RequestManager glide;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDisplayName, tvAge, tvJobTitle, tvHeight, tvCity, tvCompatibilityScore, tvContactsExchanged, tvFavourite, tvReligion;
        public ImageView ivMainPhoto;
        public ViewHolder(View listView) {
            super(listView);
            ivMainPhoto = (ImageView) listView.findViewById(R.id.ivMainPhoto);
            tvDisplayName = (TextView) listView.findViewById(R.id.tvDisplayName);
            tvAge = (TextView) listView.findViewById(R.id.tvAge);
            tvJobTitle = (TextView) listView.findViewById(R.id.tvJobTitle);
            tvHeight = (TextView) listView.findViewById(R.id.tvHeight);
            tvCity = (TextView) listView.findViewById(R.id.tvCity);
            tvCompatibilityScore = (TextView) listView.findViewById(R.id.tvCompatibilityScore);
            tvContactsExchanged = (TextView) listView.findViewById(R.id.tvContactsExchanged);
            tvFavourite = (TextView) listView.findViewById(R.id.tvFavourite);
            tvReligion = (TextView) listView.findViewById(R.id.tvReligion);
        }
    }

    public AdapterMatches(ArrayList<Match> matchList, Activity activity) {
        this.matchList = matchList;
        this.activity = activity;
        this.glide = Glide.with(activity);
    }

    public void updateData(ArrayList<Match> matchList){
        this.matchList = matchList;
        notifyDataSetChanged();
    }

    public AdapterMatches.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_matches_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(cardView);

        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        Match match = matchList.get(position);

        if(match.getMainPhoto() != null) {
            glide.load(Uri.parse(match.getMainPhoto()))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.ic_launcher_background)
                            .placeholder(R.drawable.ic_launcher_foreground)
                    )
                    .into(holder.ivMainPhoto);
        }else {
            glide.clear(holder.ivMainPhoto);
        }
        if(match.getDisplayName() != null){
            holder.tvDisplayName.setText(match.getDisplayName());
        }
        if(match.getAge() != null){
            holder.tvAge.setText(match.getAge().toString());
        }
        if(match.getJobTitle() != null){
            holder.tvJobTitle.setText(match.getJobTitle());
        }
        if(match.getHeightInCm() != null){
            holder.tvHeight.setText(match.getHeightInCm().toString());
        }
        if(match.getCity() != null && match.getCity().getName() != null){
            holder.tvCity.setText(match.getCity().getName());
        }
        if(match.getCompatibilityScore() != null){
            holder.tvCompatibilityScore.setText(match.getCompatibilityScore().toString());
        }
        if(match.getContactsExchanged() != null){
            holder.tvContactsExchanged.setText(match.getContactsExchanged().toString());
        }
        if(match.getReligion() != null){
            holder.tvReligion.setText(match.getReligion());
        }
        holder.tvFavourite.setText(match.isFavourite()?"Yes":"No");
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
