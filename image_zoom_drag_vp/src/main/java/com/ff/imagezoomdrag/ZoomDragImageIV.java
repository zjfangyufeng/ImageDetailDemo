package com.ff.imagezoomdrag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by fangyufeng on 2016/7/22.
 */
public class ZoomDragImageIV extends ImageView {

    private Matrix matrix = new Matrix();
    public Matrix initializationMatrix = new Matrix();
    public DragAndZoomTouchListener dragAndZoomTouchListener;
    Bitmap bitmap;
    private float scale;

    public ZoomDragImageIV(Context context) {
        super(context);
    }

    public ZoomDragImageIV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomDragImageIV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap!=null)
            canvas.drawBitmap(bitmap,matrix,null);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        this.matrix = matrix;
        invalidate();
    }

    public void resetImageMatrix() {
        this.matrix = initializationMatrix;
        dragAndZoomTouchListener.resetToMinStatus();
        invalidate();
    }

    @Override
    public Matrix getImageMatrix() {
        return matrix;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        initMatrix(bm);
        super.setImageBitmap(bm);
    }

    private void initMatrix(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            scale = getWidth() * 1f / bitmap.getWidth();
            matrix.reset();
            matrix.setScale(scale, scale);
            float scaledHeight = bitmap.getHeight() * scale;
            if(scaledHeight<getHeight()){
                float dy = getHeight() - scaledHeight;
                matrix.postTranslate(0,dy/2);
            }
            initializationMatrix.set(matrix);
        }
    }

    public float getInitializationBitmapHeight(){
        return bitmap==null?0:bitmap.getHeight()*scale;
    }

}
