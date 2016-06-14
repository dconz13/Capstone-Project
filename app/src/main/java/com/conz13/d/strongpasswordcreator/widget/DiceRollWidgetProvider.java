package com.conz13.d.strongpasswordcreator.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.conz13.d.strongpasswordcreator.R;

/**
 * Created by dillon on 4/17/16.
 */
public class DiceRollWidgetProvider extends AppWidgetProvider {
    // Do initial setup here
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i<appWidgetIds.length; i++){
            configureWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void configureWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        // Create an Intent to launch service on click
        Intent intent = new Intent(context, RollDiceIntentService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent =  PendingIntent.getService(context, appWidgetId, intent, 0);
        // Get the layout for the App Widget and attach an on-click listener
        int layoutId = getLayoutId(context, appWidgetManager, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
        Bitmap bitmap;
        switch(layoutId) {
            case R.layout.widget_layout:
                views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one);
                views.setImageViewBitmap(R.id.widget_slot_1, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_2, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_3, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_4, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_5, bitmap);

                break;
            case R.layout.widget_layout_3_by_1:
                views.setOnClickPendingIntent(R.id.widget_dice_holder_3, pendingIntent);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one);
                views.setImageViewBitmap(R.id.widget_slot_3_1, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_3_2, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_3_3, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_3_4, bitmap);
                views.setImageViewBitmap(R.id.widget_slot_3_5, bitmap);

                break;
            case R.layout.widget_layout_1_by_1:
                views.setOnClickPendingIntent(R.id.widget_dice_holder_1, pendingIntent);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one);
                views.setImageViewBitmap(R.id.widget_slot_1_1, bitmap);

                break;
            default: break;
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //Called on resize
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        configureWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private int getLayoutId(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        int widgetWidth = getWidgetWidthFromOptions(context, appWidgetManager, appWidgetId);
        int widgetHeight = getWidgetHeightFromOptions(context, appWidgetManager, appWidgetId);
        int one_by_one = context.getResources().getDimensionPixelSize(R.dimen.widget_size_2);
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
            if(widgetWidth <=one_by_one) {
                layoutId = R.layout.widget_layout_1_by_1;
            }
            else {
                layoutId = R.layout.widget_layout;
            }
        }
        return layoutId;
    }

    // Method to get the widget width is from the sunshine app widget implementation
    private int getWidgetWidthFromOptions(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  context.getResources().getDimensionPixelSize(R.dimen.widget_default_width);
    }

    // Method to get the widget height is referenced from the sunshine app widget implementation
    private int getWidgetHeightFromOptions(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)) {
            int minHeightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            // The height returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minHeightDp,
                    displayMetrics);
        }
        return  context.getResources().getDimensionPixelSize(R.dimen.widget_default_height);
    }
}
