package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

/**
 * Obtained from https://github.com/maarek/android-wheel and modified
 */
public class SlotMachineAdapter extends AbstractWheelAdapter {
    // Image size
    final int IMAGE_WIDTH;
    final int IMAGE_HEIGHT;
    // Layout params for image view
    final ViewGroup.LayoutParams params;
    // Cached images
    private List<SoftReference<Bitmap>> images;

    // Layout inflater
    private Context context;

    // Slot machine symbols
    private final int items[] = new int[] {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six
    };


    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Constructor
     */
    public SlotMachineAdapter(Context context) {
        this.context = context;
        this.IMAGE_HEIGHT = dp2px(50f);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width;
        if(display.getRotation() == Surface.ROTATION_0 || display.getRotation() == Surface.ROTATION_180) {
            width = size.x;
        }
        else {
            width = size.y;
        }

        this.IMAGE_WIDTH = width / 5 - (int) context.getResources().getDimension(R.dimen.default_margin); //xhdpi
        params = new ViewGroup.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

        images = new ArrayList<SoftReference<Bitmap>>(items.length);
        for (int id : items) {
            images.add(new SoftReference<Bitmap>(loadImage(id)));
        }
    }

    /**
     * Loads image from resources
     */
    private Bitmap loadImage(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
        bitmap.recycle();
        return scaled;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        ImageView img;
        if (cachedView != null) {
            img = (ImageView) cachedView;
        } else {
            img = new ImageView(context);
        }
        img.setLayoutParams(params);
        SoftReference<Bitmap> bitmapRef = images.get(index);
        Bitmap bitmap = bitmapRef.get();
        if (bitmap == null) {
            bitmap = loadImage(items[index]);
            images.set(index, new SoftReference<Bitmap>(bitmap));
        }
        img.setImageBitmap(bitmap);

        return img;
    }
}
