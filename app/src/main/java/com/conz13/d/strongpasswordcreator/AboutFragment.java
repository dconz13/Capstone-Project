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

/**
 * Created by dillon on 6/9/16.
 */

public class AboutFragment extends Fragment {

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
