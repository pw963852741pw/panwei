package com.example.wordpk;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;


public class ScrollLayout extends ViewGroup {

    private boolean type;
    
    /** 娌縔杞存鏂瑰悜鐪嬶紝鏁板�煎噺1鏃跺姩鐢婚�嗘椂閽堟棆杞�� */
    public static final boolean ROTATE_DECREASE = true;
    /** 娌縔杞存鏂瑰悜鐪嬶紝鏁板�煎噺1鏃跺姩鐢婚『鏃堕拡鏃嬭浆銆� */
    public static final boolean ROTATE_INCREASE = false;
    
    /** 鍊间负true鏃跺彲鏄庣‘鏌ョ湅鍔ㄧ敾鐨勬棆杞柟鍚戙�� */
    public static final boolean DEBUG = false;
    
    private static final String TAG = "ScrollLayout";
    // 鐢ㄤ簬婊戝姩鐨勭被
    private Scroller mScroller;
    // 鐢ㄦ潵璺熻釜瑙︽懜閫熷害鐨勭被
    private VelocityTracker mVelocityTracker;
    // 褰撳墠鐨勫睆骞曡鍥�
    private int mCurScreen;
    // 榛樿鐨勬樉绀鸿鍥�
    private int mDefaultScreen = 1;
    // 鏃犱簨浠剁殑鐘舵��
    private static final int TOUCH_STATE_REST = 0;
    // 澶勪簬鎷栧姩鐨勭姸鎬�
    private static final int TOUCH_STATE_SCROLLING = 1;
    // 婊戝姩鐨勯�熷害
    private static final int SNAP_VELOCITY = 600;

    private static int mNum;
    
    private int mTouchState = TOUCH_STATE_REST;
    //鑳藉杩涜鎵嬪娍婊戝姩鐨勮窛绂�

    private int mTouchSlop;
    private float mLastMotionX;
    // 鐢ㄦ潵澶勭悊绔嬩綋鏁堟灉鐨勭被
    private Camera mCamera;
    private Matrix mMatrix;
    /**鏃嬭浆鐨勮搴�*/
    private float angle = 180;

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    // 鍦ㄦ瀯閫犲櫒涓垵濮嬪寲
    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mScroller = new Scroller(context);

        mCurScreen = mDefaultScreen;
        //鑾峰緱鑳藉杩涜鎵嬪娍婊戝姩鐨勮窛绂�
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    /*
     * 
     * 涓哄瓙View鎸囧畾浣嶇疆
     */
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onLayout");

        if (changed) {
            int childLeft = 0;
            final int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    final int childWidth = childView.getMeasuredWidth();
                    childView.layout(childLeft, 0, childLeft + childWidth,
                            childView.getMeasuredHeight());

                    childLeft += childWidth;
                }
            }
        }
    }

    // 閲嶅啓姝ゆ柟娉曠敤鏉ヨ绠楅珮搴﹀拰瀹藉害
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // Exactly锛歸idth浠ｈ〃鐨勬槸绮剧‘鐨勫昂瀵�
        // AT_MOST锛歸idth浠ｈ〃鐨勬槸鏈�澶у彲鑾峰緱鐨勭┖闂�
//        if (widthMode != MeasureSpec.EXACTLY) {
//            throw new IllegalStateException(
//                    "ScrollLayout only canmCurScreen run at EXACTLY mode!");
//        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        if (heightMode != MeasureSpec.EXACTLY) {
//            throw new IllegalStateException(
//                    "ScrollLayout only can run at EXACTLY mode!");
//        }

        // The children are given the same width and height as the scrollLayout
        // 寰楀埌澶氬皯椤�(瀛怴iew)骞惰缃粬浠殑瀹藉拰楂�
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        // Log.e(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurScreen * width, 0);
    }
    
    
    
    /**
     * 褰撹繘琛孷iew婊戝姩鏃讹紝浼氬鑷村綋鍓嶇殑View鏃犳晥锛岃鍑芥暟鐨勪綔鐢ㄦ槸瀵筕iew杩涜閲嶆柊缁樺埗 璋冪敤drawScreen鍑芥暟
     */
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        final long drawingTime = getDrawingTime();
        final int count = getChildCount();
        //Log.i("HHJ", "125"+"   drawingTime:"+drawingTime+"           count:"+count);
        for (int i = 0; i < count; i++) {
            drawScreenCube(canvas, i, drawingTime,5);//mNum
        }
    }
    
    class BigStone {
        //鍥剧墖
        Bitmap bitmap;
        //瑙掑害
        int angle;
        //x鍧愭爣
        float x;
        //y鍧愭爣
        float y;
        //鏄惁鍙
        String text;
        boolean isVisible = true;
    }
    
    /**
     * 璁＄畻姣忎釜鐐圭殑鍧愭爣
     */
    private void computeCoordinates() {
        BigStone stone;
        for(int index=0; index<STONE_COUNT; index++) {
            stone = mStones[index];
            stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/90));//stone.angle*Math.PI/180(寮у害=瑙掑害*3.14)
            stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/90));
        }
    }
    //stone鍒楄〃
    private BigStone[] mStones;
    //鏁扮洰
    private int STONE_COUNT ;
    
    /**鍦嗗績鍧愭爣*/
    private int mPointX=0, mPointY=0;
    /**鍗婂緞*/
    private int mRadius = 120;
    /**姣忎袱涓偣闂撮殧鐨勮搴�*/
    private int mDegreeDelta;
    private Context context;
    
    private Paint mPaint = new Paint();
/*    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            firstDegree =mStones[0].angle;
        }
        return true;
    }*/
    /**
     * 绔嬩綋鏁堟灉鐨勫疄鐜板嚱鏁� ,screen涓哄摢涓�涓瓙View  绔嬫柟浣撴晥鏋�
     * 
     */
    public void drawScreenCube(Canvas canvas, int screen, long drawingTime ,int select) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        
        if(select==1){//鍦嗗湀

           super.dispatchDraw(canvas);
        }else if(select==2){//灞傚彔閫忔槑鍙樹寒
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child;// = getChildAt(screen); 
            
            float scaleX = 1f;/*璁剧疆鐭╅樀澶у皬*/
            float scaleY = 1f;
            
            float centerX= (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; //涓績浣嶇疆
            float centerY=getHeight()/2;
            
            if(screen==mCurScreen+1 && getChildAt(mCurScreen+1)!=null){
                child = getChildAt(mCurScreen+1);
                Log.i("HHJ", "child:"+child.toString());
                scaleX = (float) (((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
                centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth-getMeasuredWidth()/2; 
                centerY = getHeight()/2; 
            }else if(screen==mCurScreen-1 && getChildAt(mCurScreen-1)!=null){
                child = getChildAt(mCurScreen-1);
            }else {
                child = getChildAt(mCurScreen);
                if(scrollX<mCurScreen*getMeasuredWidth()){
                    scaleX = (float) (Math.abs(getScrollX()-mCurScreen*getMeasuredWidth()) * (1.0 / getMeasuredWidth())-1);
                    scaleY = scaleX;
                    centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth-getMeasuredWidth()/2; 
                    centerY = getHeight()/2; 
                }
            }
            
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-0);
            
            camera.getMatrix(matrix); 
            camera.restore(); 
            
            matrix.preScale(Math.abs(scaleX),Math.abs(scaleY));//澶у皬
            //matrix.postScale(scaleX, scaleY);
            
            matrix.preTranslate(-centerX, -centerY); //涓績浣嶇疆
            matrix.postTranslate(centerX, centerY); 
            
            canvas.concat(matrix);
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if(select==3){//娉㈡氮鎯呭喌
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child;// = getChildAt(screen); 
            float scaleX = 1f;
            float scaleY = 1f;
            if(screen==mCurScreen+1 && getChildAt(mCurScreen+1)!=null){
                child = getChildAt(mCurScreen+1);
                scaleX = (float) ((float) ((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
            }else if(screen==mCurScreen-1 && getChildAt(mCurScreen-1)!=null){
                child = getChildAt(mCurScreen-1);
                scaleX = (float) ((float) ((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
            }else {
                child = getChildAt(mCurScreen);
            }
            
            final float centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; 
            final float centerY = getHeight()/2; 
            
            
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-0);
            
            camera.getMatrix(matrix); 
            camera.restore(); 
            
            matrix.preScale(Math.abs(scaleX), Math.abs(scaleY));
            //matrix.postScale(scaleX, scaleY);
            
            matrix.preTranslate(-centerX, -centerY); 
            matrix.postTranslate(centerX, centerY); 
            
            canvas.concat(matrix); 
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if(select==4){//绔嬩綋缈昏浆
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child = getChildAt(screen); 
            final int faceIndex = screen; 
            final float faceDegree = (this.getScrollX() - faceIndex *480f)*0.1875f;
            final float currentDegree = getScrollX() * (angle / getMeasuredWidth());
            if(faceDegree > 90 || faceDegree < -90) { 
                return; 
            }         
            final float centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; 
            final float centerY = getHeight()/2; 
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-faceDegree); 
            camera.getMatrix(matrix); 
            camera.restore(); 
            matrix.preTranslate(-centerX, -centerY); 
            matrix.postTranslate(centerX, centerY); 
            canvas.concat(matrix); 
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if (select==5){
            // 寰楀埌褰撳墠瀛怴iew鐨勫搴�
            final int width = getWidth();
            
            /**褰撳墠婊戝姩鍒皏iew瑙嗗浘鍦╲iewgroup閲岄潰鍒颁綅缃�   scrollWidth = 灞忔暟*璇iew鍒板搴�  */
            final int scrollWidth = screen * width;
            /**View姘村钩鏂瑰悜鐨勫亸绉婚噺锛堝儚绱狅級*/
            final int scrollX = this.getScrollX();
            if (scrollWidth > scrollX + width || scrollWidth + width < scrollX) {
                return;
            }
            final View child = getChildAt(screen);
            final int faceIndex = screen;
            
            /**
             * 1.瑙ｉ攣涓嬭繖閲屽埌鎰忔��  鐢变簬褰撳墠涓�灞忔墍鍏呮弧灞忓箷瀹藉害锛堝亣濡傛墍480*800锛�480    
           * 2. 180/getMeasuredWidth() 褰撳墠灞忔�诲叡鍒嗕负180搴�  姣斾笂 褰撳墠灞忔�诲搴� 鎰忔�濊灞忔瘡涓�涓儚绱犲埌婊戝姩鍗犲灏戣搴�    鎰忔�濇墍璇�180/480=0.375 姣忕Щ鍔ㄤ竴涓儚绱犲氨鎵�瑕佽浆鍔�0.375涓搴� 
           * 3. getScrollX() 鐢变簬杩欎釜鎵�涓�涓獀iewgroup 涓婃粦鍔ㄥ埌鍋忕Щ閲� ,鎵�鏈夌瓑鏈夋墍灏唙iewgroup缈昏浆浜嗗緢澶氫釜180
             *    绛変簬璇存墍绗竴灞忔墍0锝�180搴�   绗簩灞忔墍 180 锝� 360 搴�   绗笁灞忔墍 360锝�540搴� 渚濇灏唙iewgroup缈昏浆鍒拌搴﹁�屽凡
             *  */
            final float currentDegree = getScrollX() * (angle / getMeasuredWidth());
            /**
             * 1.褰撳墠缈昏浆鍒拌搴�   currentDegree-褰撳墠灞忔暟*180
             * 2.杩欓噷杩欐牱涓�鍑忓綋鍓嶅埌婊戝睆搴︽暟灏辨帶鍒跺湪-180锝�0搴︿箣闂翠簡
             * 3.璁╁悗鍦ㄥ姞涓婁笅闈竴涓猧f鍒ゆ柇锛屾槸灏嗗綋鍓嶄竴灞忎笌灞忔垚涔濆崄搴﹀埌鏃跺�欎互鍚庣殑婊戝姩灏辨敞閿�鎺夎��
             * 
             **/
            final float faceDegree = currentDegree - faceIndex * angle;
            if (faceDegree > 90 || faceDegree < -90) {
                return;
            }
            
            //鏃嬭浆鐨剎杞翠腑蹇冧綅缃�
            final float centerX = (scrollWidth < scrollX) ? scrollWidth + width: scrollWidth;
            Log.i("HHJ", "centerX:"+centerX+"    scrollWidth:"+scrollWidth+"   scrollX:"+scrollX);
            //鏃嬭浆鐨剏杞翠腑蹇冧綅缃�
            final float centerY = getHeight() / 2;
            final Camera camera = mCamera;
            final Matrix matrixX = mMatrix;
            canvas.save();
            camera.save();
            camera.rotateY(-faceDegree);
            camera.getMatrix(matrixX);
            camera.restore();
            matrixX.preTranslate(-centerX, -centerY);//鐗规晥澶勭悊鐨勪腑蹇�
            matrixX.postTranslate(centerX, centerY);
            canvas.concat(matrixX);
            drawChild(canvas, child, drawingTime);
            canvas.restore();
        }else {
            super.dispatchDraw(canvas);
        }

    }
    
    /**
     * 鎶婁腑蹇冪偣鏀惧埌涓績澶�
     * @param canvas
     * @param bitmap
     * @param left
     * @param top
     */
    void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,String text) {
        canvas.drawPoint(left, top, mPaint);
        Log.i("HHJ","bitmap==null:"+(bitmap==null));
        canvas.drawBitmap(bitmap, left-bitmap.getWidth()/2, top-bitmap.getHeight()/2, null);
        canvas.drawText(text,left-bitmap.getWidth()/2+2, top+bitmap.getHeight()/2+8, mPaint);
        canvas.restore(); 
    }

    /**
     * 鏍规嵁鐩墠鐨勪綅缃粴鍔ㄥ埌涓嬩竴涓鍥句綅缃�.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        // 鏍规嵁View鐨勫搴︿互鍙婃粦鍔ㄧ殑鍊兼潵鍒ゆ柇鏄摢涓猇iew
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        // get the valid layout page
//        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        whichScreen=whichScreen<0?whichScreen+getChildCount()*2:whichScreen;
        whichScreen%=getChildCount();
        if (getScrollX() != (whichScreen * getWidth())) {

            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0,
                    Math.abs(delta) * 2);
            mCurScreen = whichScreen;
            invalidate(); // 閲嶆柊甯冨眬
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth(), 0);
    }

    public int getCurScreen() {
        return mCurScreen;
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            //Log.i("HHJ", "209"+mScroller.computeScrollOffset());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    
    
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (mVelocityTracker == null) {
            // 浣跨敤obtain鏂规硶寰楀埌VelocityTracker鐨勪竴涓璞�
            mVelocityTracker = VelocityTracker.obtain();
        }
        // 灏嗗綋鍓嶇殑瑙︽懜浜嬩欢浼犻�掔粰VelocityTracker瀵硅薄
        mVelocityTracker.addMovement(event);
        // 寰楀埌瑙︽懜浜嬩欢鐨勭被鍨�
        final int action = event.getAction();
        final float x = event.getX();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            Log.e(TAG, "event down!");
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mLastMotionX = x;
            break;

        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) (mLastMotionX - x);
            mLastMotionX = x;

            scrollBy(deltaX, 0);
            break;

        case MotionEvent.ACTION_UP:
            Log.e(TAG, "event : up");
            // if (mTouchState == TOUCH_STATE_SCROLLING) {
            final VelocityTracker velocityTracker = mVelocityTracker;
            // 璁＄畻褰撳墠鐨勯�熷害
            velocityTracker.computeCurrentVelocity(1000);
            // 鑾峰緱褰撳墠鐨勯�熷害
            int velocityX = (int) velocityTracker.getXVelocity();

            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                // Fling enough to move left
                snapToScreen(mCurScreen - 1);
            } else if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
                // Fling enough to move right
                snapToScreen(mCurScreen + 1);
            } else {
                snapToDestination();
            }

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            // }
            mTouchState = TOUCH_STATE_REST;
            break;
        case MotionEvent.ACTION_CANCEL:
            mTouchState = TOUCH_STATE_REST;
            break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action) {
        case MotionEvent.ACTION_MOVE:
            final int xDiff = (int) Math.abs(mLastMotionX - x);
            if (xDiff > mTouchSlop) {
                mTouchState = TOUCH_STATE_SCROLLING;

            }
            break;

        case MotionEvent.ACTION_DOWN:
            
            mNum = new Random().nextInt(10);
            mLastMotionX = x;
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                    : TOUCH_STATE_SCROLLING;
            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            mTouchState = TOUCH_STATE_REST;
            break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }


}
