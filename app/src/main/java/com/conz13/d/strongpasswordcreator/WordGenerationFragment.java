package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dillon on 4/18/16.
 */
public class WordGenerationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_generation_fragment_layout, container, false);

        return rootView;
    }
}
