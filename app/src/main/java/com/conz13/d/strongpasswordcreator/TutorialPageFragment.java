package com.conz13.d.strongpasswordcreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dillon on 5/24/16.
 */
public class TutorialPageFragment extends Fragment{
    private int mPageNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int layout = getLayoutResource(args.getInt("pageTest", 0));

        View rootView = inflater.inflate(layout, container, false);

        return rootView;
    }

    private int getLayoutResource(int pageNumber){
        switch (pageNumber){
            case 0: {
                return R.layout.tutorial_page_1;
            }
            case 1: {
                return R.layout.tutorial_page_2;
            }
            case 2: {
                return R.layout.tutorial_page_3;
            }
            case 3: {
                return R.layout.tutorial_page_4;
            }
            default:
                return R.layout.tutorial_page_1;
        }
    }
}
