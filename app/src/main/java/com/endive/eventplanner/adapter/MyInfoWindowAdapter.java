package com.endive.eventplanner.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.MapActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;

/**
 * Created by arpit.jain on 10/11/2017.
 */

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View view;
    private MapActivity ctx;
    private Hashtable<String, String> markers;

    public MyInfoWindowAdapter(MapActivity obj, Hashtable<String, String> markers) {
        this.markers = markers;
        this.ctx = obj;
        view = ctx.getLayoutInflater().inflate(R.layout.custom_info_window,
                null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (marker != null
                && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        String url = null;

        if (marker.getId() != null && markers != null && markers.size() > 0) {
            if (markers.get(marker.getId()) != null &&
                    markers.get(marker.getId()) != null) {
                url = markers.get(marker.getId());
            }
        }

        ImageView image = (ImageView) view.findViewById(R.id.event_image_map);
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            Picasso.with(ctx)
                    .load(url)
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            getInfoContents(marker);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            image.setImageResource(R.mipmap.ic_launcher);
        }

        final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view
                .findViewById(R.id.snippet));
        if (snippet != null) {
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }

        return view;
    }
}