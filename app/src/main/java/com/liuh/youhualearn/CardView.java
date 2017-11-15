package com.liuh.youhualearn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huan on 2017/11/15 12:09.
 */

public class CardView extends View {
    private Bitmap[] mCards = new Bitmap[3];
    private int[] mImgIds = new int[]{R.drawable.mama, R.drawable.boy, R.drawable.girl};

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Bitmap bitmap = null;
        for (int i = 0; i < mImgIds.length; i++) {
            bitmap = BitmapFactory.decodeResource(getResources(), mImgIds[i]);
            mCards[i] = Bitmap.createScaledBitmap(bitmap, 400, 600, false);
        }
        setBackgroundColor(0xff00ff00);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(20, 120);
        for (int i = 0; i < mCards.length; i++) {
            canvas.translate(120, 0);
            canvas.save();
            if (i < mCards.length - 1) {
                canvas.clipRect(0, 0, 120, mCards[i].getHeight());
            }
            canvas.drawBitmap(mCards[i], 0, 0, null);
            canvas.restore();
        }
        canvas.restore();
    }
}
