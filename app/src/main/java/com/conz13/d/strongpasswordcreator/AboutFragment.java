package com.conz13.d.strongpasswordcreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by dillon on 6/9/16.
 */

public class AboutFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getContext(), getString(R.string.banner_ad_test_unit_id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        ImageButton feedbackButton = (ImageButton) rootView.findViewById(R.id.feedback_button);
        LinearLayout feedbackLayout = (LinearLayout) rootView.findViewById(R.id.feedback_linear_layout);
        feedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOnClick();
            }
        });

        AdView adView = (AdView) rootView.findViewById(R.id.about_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return rootView;
    }

    private void imageOnClick(){
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/Email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "dconnolly.business@gmail.com"});
        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        startActivity(Intent.createChooser(Email, "Send Feedback:"));
    }
}
