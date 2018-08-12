package com.endive.eventplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.EventDetailsActivity;
import com.endive.eventplanner.util.ViewAnimationUtils;


public class DetailFragment extends Fragment {

    private EventDetailsActivity ctx;
    private LinearLayout desc_layout, desc, tnc_layout, tnc;
    private ImageView desc_down, tnc_down;
    private TextView desc_data, tnc_data;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        ctx = (EventDetailsActivity) getActivity();
        initialize();
        setListeners();

        return view;
    }

    private void initialize() {
        desc_layout = (LinearLayout) view.findViewById(R.id.desc_layout);
        desc = (LinearLayout) view.findViewById(R.id.desc);
        tnc_layout = (LinearLayout) view.findViewById(R.id.tnc_layout);
        tnc = (LinearLayout) view.findViewById(R.id.tnc);
        desc_down = (ImageView) view.findViewById(R.id.desc_down);
        tnc_down = (ImageView) view.findViewById(R.id.tnc_down);
        desc_data = (TextView) view.findViewById(R.id.desc_data);
        tnc_data = (TextView) view.findViewById(R.id.tnc_data);

    }

    private void setListeners() {
        desc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descLayoutClick();
            }
        });

        tnc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tncLayoutClick();
            }
        });

        if (ctx.data != null) {
            tnc_data.setText(Html.fromHtml(ctx.data.getTerms_condition()));
            desc_data.setText(Html.fromHtml(ctx.data.getDescription()));
            desc_layout.performClick();
        }
    }

    private void descLayoutClick() {
        if (desc.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(desc);
            setArrow(desc_down, true);
        } else {
            ViewAnimationUtils.collapse(desc);
            setArrow(desc_down, false);
        }
    }

    private void tncLayoutClick() {
        if (tnc.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(tnc);
            setArrow(tnc_down, true);
        } else {
            ViewAnimationUtils.collapse(tnc);
            setArrow(tnc_down, false);
        }
    }

    private void setArrow(ImageView image, boolean check) {
        if (check)
            image.setImageResource(R.mipmap.down_arrow_black);
        else
            image.setImageResource(R.mipmap.right_arrow_black);
    }
}
