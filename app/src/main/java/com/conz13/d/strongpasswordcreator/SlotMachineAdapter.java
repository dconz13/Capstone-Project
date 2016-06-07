package com.conz13.d.strongpasswordcreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

/**
 * Referenced from sample code provided at https://github.com/maarek/android-wheel and modified for my use case
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

    /**
     * Constructor
     */
    public SlotMachineAdapter(Context context) {
        this.context = context;
        this.IMAGE_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.IMAGE_WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;

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
        return BitmapFactory.decodeResource(context.getResources(), id);
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
        img.setAdjustViewBounds(true);
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
