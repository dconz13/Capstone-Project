package com.conz13.d.strongpasswordcreator.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.conz13.d.strongpasswordcreator.R;
import com.conz13.d.strongpasswordcreator.Utility;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by dillon on 6/13/16.
 */
public class RollDiceIntentService extends IntentService {

    public RollDiceIntentService(){
        super("RollDiceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, DiceRollWidgetProvider.class));
        Bundle extras = intent.getExtras();
        int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);

        for(int appWidgetId : appWidgetIds) {

            if(appWidgetId == id) {
                int[] roll = Utility.getDiceRoll();
                String number = Utility.convertIntArrayToString(roll);
                String language = Utility.getLanguage(this);
                String word = Utility.getPropertyValue(this, number, language);

                int layoutId = getLayoutId(appWidgetManager, appWidgetId);
                RemoteViews views = new RemoteViews(getPackageName(), layoutId);
                ArrayList<Bitmap> diceHolder = new ArrayList<>(roll.length);

                for (int i = 0; i < 5; i++) {
                    diceHolder.add(BitmapFactory.decodeResource(getResources(), Utility.getDiceImage(roll[i])));
                }

                switch (layoutId) {
                    case R.layout.widget_layout:
                        views.setImageViewBitmap(R.id.widget_slot_1, diceHolder.get(0));
                        views.setImageViewBitmap(R.id.widget_slot_2, diceHolder.get(1));
                        views.setImageViewBitmap(R.id.widget_slot_3, diceHolder.get(2));
                        views.setImageViewBitmap(R.id.widget_slot_4, diceHolder.get(3));
                        views.setImageViewBitmap(R.id.widget_slot_5, diceHolder.get(4));
                        views.setTextViewText(R.id.widget_text_view, word);
                        break;
                    case R.layout.widget_layout_3_by_1:
                        views.setImageViewBitmap(R.id.widget_slot_3_1, diceHolder.get(0));
                        views.setImageViewBitmap(R.id.widget_slot_3_2, diceHolder.get(1));
                        views.setImageViewBitmap(R.id.widget_slot_3_3, diceHolder.get(2));
                        views.setImageViewBitmap(R.id.widget_slot_3_4, diceHolder.get(3));
                        views.setImageViewBitmap(R.id.widget_slot_3_5, diceHolder.get(4));
                        break;
                    case R.layout.widget_layout_1_by_1:
                        views.setImageViewBitmap(R.id.widget_slot_1_1, diceHolder.get(0));
                        break;
                    default:
                        break;
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }

    }

    private int getLayoutId(AppWidgetManager appWidgetManager, int appWidgetId){
        int widgetWidth = getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
        int widgetHeight = getWidgetHeightFromOptions(appWidgetManager, appWidgetId);
        int one_by_one = getResources().getDimensionPixelSize(R.dimen.widget_size_2);
        int layoutId = 0;
        if(widgetHeight <= one_by_one){
            if(widgetWidth <=one_by_one) {
                layoutId = R.layout.widget_layout_1_by_1;
            }
            else {
                layoutId = R.layout.widget_layout_3_by_1;
            }
        }
        else {
            layoutId = R.layout.widget_layout;
        }
        return layoutId;
    }

    // Method to get the widget width is from the sunshine app widget implementation
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_default_width);
    }

    // Method to get the widget height is referenced from the sunshine app widget implementation
    private int getWidgetHeightFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)) {
            int minHeightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            // The height returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minHeightDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_default_height);
    }

}
