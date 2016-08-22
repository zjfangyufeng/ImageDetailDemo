package com.ff.imagezoomdrag;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;


/**
 * Created by fangyufeng on 2016/7/18.
 */
public class DragAndZoomTouchListener {

    ZoomDragImageIV iv;
    public DragAndZoomTouchListener(ZoomDragImageIV iv) {
        this.iv = iv;
    }

    private int mode = 0;// 初始状态

    private static final int MODE_DRAG = 1;
    private static final int MODE_ZOOM = 2;
    float total_scale = 1 , current_scale;
    private float canDragToRightDistance,canDragToLeftDistance,canDragToTopDistance,canDragToBottomDistance;

    private PointF actionDownPoint = new PointF();
    private PointF dragPoint = new PointF();
    private Matrix matrixNow = new Matrix();
    private Matrix matrixBefore = new Matrix();
    private float startDis;
    /** 两个手指的中间点 */
    private PointF midPoint = new PointF(0,0);

    public boolean onTouch(MotionEvent event) {
        /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                matrixBefore.set(iv.getImageMatrix());
                matrixNow.set(iv.getImageMatrix());
                dragPoint.set(event.getX(), event.getY());
                actionDownPoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG) {
                    float dx = event.getX() - dragPoint.x;
                    float dy = event.getY() - dragPoint.y;
                    dragPoint.set(event.getX(), event.getY());
                    if(checkXDragValid(dx)){
                        canDragToRightDistance-=dx;
                        canDragToLeftDistance+=dx;
                        if(checkYDragValid(dy)){
                            canDragToBottomDistance-=dy;
                            canDragToTopDistance+=dy;
                        }else {
                            dy = 0;
                        }
                        matrixNow.postTranslate(dx, dy);
                        iv.setImageMatrix(matrixNow);
                    }else {
                        return false;
                    }
                } else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);
                    midPoint = mid(event);
                    if (endDis > 10f) {
                        current_scale = endDis / startDis;//缩放倍数
                        total_scale *= current_scale;
                        if(canDragToRightDistance<=0 && canDragToLeftDistance>0){
                            midPoint.x = 0;
                        }
                        if(canDragToLeftDistance<=0 && canDragToRightDistance>0){
                            midPoint.x = iv.getMeasuredWidth();
                        }
                        resetDragDistance(current_scale);
                        matrixNow.postScale(current_scale, current_scale,midPoint.x,midPoint.y);
                        iv.setImageMatrix(matrixNow);
                    }
                    startDis = distance(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mode == MODE_DRAG)
                    checkClick(event.getX(),event.getY(), actionDownPoint.x, actionDownPoint.y);
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                checkZoomValid();
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                startDis = distance(event);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) {
                    //记录当前ImageView的缩放倍数
                    matrixBefore.set(iv.getImageMatrix());
                    matrixNow.set(iv.getImageMatrix());
                }
                break;
        }
        return true;
    }

    private boolean checkXDragValid(float dx) {
        if(mode == MODE_DRAG){
            if(dx>0){ //向右
                return canDragToRight(dx);
            }else {//向左
                return canDragToLeft(dx);
            }
        }
        return false;
    }

    private boolean checkYDragValid(float dy) {
        if(mode == MODE_DRAG){
            if(dy>0){ //向下
                return canDragToBottom(dy);
            }else {//向上
                return canDragToTop(dy);
            }
        }
        return false;
    }

    private boolean canDragToBottom(float dy){
        if(canDragToBottomDistance > dy){
            return true;
        }
        return false;
    }

    private boolean canDragToRight(float dx){
        if(canDragToRightDistance > dx){
            return true;
        }
        return false;
    }

    private boolean canDragToTop(float dy){
        if(canDragToTopDistance > Math.abs(dy)){
            return true;
        }
        return false;
    }

    private boolean canDragToLeft(float dx){
        if(canDragToLeftDistance > Math.abs(dx)){
            return true;
        }
        return false;
    }

    public void resetDragDistance(float scale){
        canDragToRightDistance = (midPoint.x + canDragToRightDistance) * scale - midPoint.x;
        canDragToLeftDistance = (iv.getMeasuredWidth()-midPoint.x+canDragToLeftDistance) * scale - (iv.getMeasuredWidth()-midPoint.x);
        if(total_scale*iv.getInitializationBitmapHeight()>iv.getMeasuredHeight()){
            canDragToBottomDistance = (midPoint.y + canDragToBottomDistance) * scale - midPoint.y;
            canDragToTopDistance = (iv.getMeasuredHeight()-midPoint.y+canDragToTopDistance) * scale - (iv.getMeasuredHeight()-midPoint.y);
        }else {
            canDragToBottomDistance = canDragToTopDistance = 0;
        }
    }

    public void resetToMinStatus(){
        total_scale = 1;
        canDragToRightDistance = 0;
        canDragToBottomDistance = 0;
        canDragToLeftDistance = 0;
        canDragToTopDistance = 0;
        resetDragDistance(total_scale);
    }

    public void resetToMaxStatus(){
        total_scale = 2;
        canDragToRightDistance = 0;
        canDragToBottomDistance = 0;
        canDragToLeftDistance = 0;
        canDragToTopDistance = 0;
        resetDragDistance(total_scale);
    }

    private boolean checkZoomValid() {
        if(mode == MODE_ZOOM){
            if(total_scale>2){
                resetToMaxStatus();
                matrixNow.set(iv.initializationMatrix);
                matrixNow.postScale(2, 2,midPoint.x,midPoint.y);
                iv.setImageMatrix(matrixNow);
                return false;
            }else if(total_scale<1){
                resetToMinStatus();
                matrixNow.set(iv.initializationMatrix);
                iv.setImageMatrix(matrixNow);
                return false;
            }
        }
        return true;
    }

    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    boolean checkClick(float last_x,float last_y,float now_x,float now_y){
        float x_d = Math.abs(last_x - now_x);
        float y_d = Math.abs(last_y - now_y);
        if(x_d<10 && y_d<10){
            Activity activity = (Activity) iv.getContext();
            activity.finish();
            return true;
        }
        return false;
    }

}
