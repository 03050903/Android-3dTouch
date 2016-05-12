package lib.linhomhom.touch3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Created by Linhh on 16/4/25.
 */
public class TouchMenuHelper implements GestureDetector.OnGestureListener,View.OnTouchListener{
    private GestureDetector mGestureDetector;
    private View mView;
    private View mMenuView;
    private int mMenuWidth,mMenuHeight;
    private View.OnTouchListener mOnTouchListener;

    private PopupWindow mPopupWindow;

    private Context mContext;

    private FrameLayout mMenuRoot;

    private ImageView mTargketView;

//    public TouchMenuHelper(Context context){
//        this(context,null,null,0,0);
//    }

    public TouchMenuHelper(Context context, View view, View menuView, int menuWidth, int menuHeight){
        this(context,view,menuView,menuWidth,menuHeight,null);
    }

    public TouchMenuHelper(Context context, View view, View menuView, int menuWidth, int menuHeight, View.OnTouchListener onTouchListener){
        mGestureDetector = new GestureDetector(context, this);
        mView = view;
        mMenuView = menuView;
        mMenuHeight = menuHeight;
        mMenuWidth = menuWidth;
        mOnTouchListener = onTouchListener;
        mContext = context;
    }

    public void update(){
        if(mView == null){
//            throw new Exception("mView is null");
            return;
        }

        if(mMenuView == null){
//            throw new Exception("mMenuView is null");
            return;
        }

        Bitmap b = ((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.bg222)).getBitmap();


        mMenuRoot = (FrameLayout) View.inflate(mContext,R.layout.view_menu,null);
        mMenuRoot.addView(mMenuView, mMenuWidth, mMenuHeight);

        mTargketView = new ImageView(mContext);

        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mMenuRoot.addView(mTargketView, mView.getMeasuredWidth(),mView.getMeasuredHeight());
            }
        });

        mMenuRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(mMenuRoot, TouchMenuUtil.getScreenSize(mContext).x, TouchMenuUtil.getScreenSize(mContext).y - TouchMenuUtil.getStatusBarHeight(mContext));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(TouchMenuUtil.blur(mContext,b)));
        mPopupWindow.update();

        mView.setOnTouchListener(this);

    }

    public void setOnTouchListener(View.OnTouchListener listener){
        mOnTouchListener = listener;
    }

    public void setTarget(View view){
        mView = view;
    }

    public void setMenuView(View view){
        mMenuView = view;
    }

    public void setMenuWidth(int menuWidth){
        this.mMenuWidth = menuWidth;
    }

    public void setMenuHeight(int menuHeight){
        this.mMenuHeight = menuHeight;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        if (mView == null || mMenuView == null) {
            return;
        }

        int[] location = new int[2];
        mView.getLocationOnScreen(location);

        mView.clearFocus();//currentView表示设置的View对象
        mView.setPressed(false);
        mView.setDrawingCacheBackgroundColor(0);
        mView.setDrawingCacheEnabled(true);
        Bitmap viewBitmap = Bitmap.createBitmap(mView.getDrawingCache());
        mView.setDrawingCacheEnabled(false);

        mTargketView.setImageBitmap(viewBitmap);
//        mView.setDrawingCacheEnabled(false);
        mTargketView.setX(location[0]);
        mTargketView.setY(location[1] - TouchMenuUtil.getStatusBarHeight(mContext));

        if(mTargketView.getX() + mMenuWidth > TouchMenuUtil.getScreenSize(mContext).x){
            //如果超过了屏幕，需要从右边开始了
            mMenuView.setX(mTargketView.getX() + mView.getMeasuredWidth() - mMenuWidth);
        }else{
            mMenuView.setX(mTargketView.getX());
        }

        if(mTargketView.getY() - mMenuHeight - 20 < TouchMenuUtil.getStatusBarHeight(mContext)){
            //超过屏幕，从下方开启
            mMenuView.setY(mTargketView.getY() + mView.getMeasuredHeight() + 20);
        }else {
            mMenuView.setY(mTargketView.getY() - mMenuHeight - 20);
        }

        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(mView, Gravity.LEFT | Gravity.TOP, 0,TouchMenuUtil.getStatusBarHeight(mContext));
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mGestureDetector != null){
            mGestureDetector.onTouchEvent(event);
        }
        return mOnTouchListener == null ? false : mOnTouchListener.onTouch(v,event);
    }
}
