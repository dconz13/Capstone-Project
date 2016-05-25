package com.conz13.d.strongpasswordcreator.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.conz13.d.strongpasswordcreator.TutorialPageFragment;

/**
 * Created by dillon on 5/24/16.
 *
 * https://developer.android.com/training/animation/screen-slide.html
 * for fancy screen slides
 */
public class TutorialPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final Context mContext;
    private final ViewPager mViewPager;

    public TutorialPagerAdapter(AppCompatActivity activity, ViewPager pager){
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = pager;
        pager.setAdapter(this);
        pager.addOnPageChangeListener(this);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("pageTest", position);
        TutorialPageFragment fragment = new TutorialPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getCount() {
        return 4;
    }
}
