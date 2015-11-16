package com.mathheals.euvou.controller.ShowPlaceRanking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mathheals.euvou.R;

import java.util.ArrayList;
import java.util.List;

import model.Place;

/**
 * Created by geovanni on 16/11/15.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {
    public PlaceAdapter(Context context, List<Place> places) {
        super(context, 0,places);
    }
    private static class ViewHolder {
        TextView placeName;
        TextView placeEvaluation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Place place = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_show_place_ranking, parent, false);
            viewHolder.placeName = (TextView) convertView.findViewById(R.id.placeName);
            viewHolder.placeEvaluation = (TextView) convertView.findViewById(R.id.placeEvaluation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.placeName.setText(place.getName());
        viewHolder.placeEvaluation.setText(place.getEvaluate().toString());

        return convertView;
    }
}
