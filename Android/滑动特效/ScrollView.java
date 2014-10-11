package com.example.wordpk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollView extends Activity {   
    private static final String LOG_TAG = "Scrollview";   
    private LinearLayout mLayout;   
    private View mScrollView;   
    private final Handler mHandler = new Handler();   
       
    @Override  
    protected void onCreate(Bundle icicle) {   
        super.onCreate(icicle);   
        setContentView(R.layout.test);   
  
        mLayout = (LinearLayout) findViewById(R.id.layout);   
        mScrollView =findViewById(R.id.scrollview);  
  
        Button button = (Button) findViewById(R.id.add_widget);   
        button.setOnClickListener(mClickListener);   
        //改变默认焦点切换   
        button.setOnKeyListener(mAddButtonKeyListener);   
    }   
       
    private Button.OnClickListener mClickListener = new Button.OnClickListener() {   
        private int mIndex = 1;   
        @Override  
        public void onClick(View arg0) {   
            // TODO Auto-generated method stub        
            TextView textView = new TextView(ScrollView.this);   
            textView.setText("Text View " + mIndex);   
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(   
                    LinearLayout.LayoutParams.FILL_PARENT,   
                    LinearLayout.LayoutParams.WRAP_CONTENT   
            );   
            mLayout.addView(textView, p);   
  
            Button buttonView = new Button(ScrollView.this);   
            buttonView.setText("Button " + mIndex++);   
            mLayout.addView(buttonView, p);   
            //改变默认焦点切换   
            buttonView.setOnKeyListener(mNewButtonKeyListener);   
            //投递一个消息进行滚动   
            mHandler.post(mScrollToBottom);   
        }          
    };   
       
    private Runnable mScrollToBottom = new Runnable() {   
        @Override  
        public void run() {   
            // TODO Auto-generated method stub   
            Log.d(LOG_TAG, "ScrollY: " + mScrollView.getScrollY());   
            int off = mLayout.getMeasuredHeight() - mScrollView.getHeight();   
            if (off > 0) {   
                mScrollView.scrollTo(0, off);   
            }                          
        }   
    };   
      
    private View.OnKeyListener mNewButtonKeyListener = new View.OnKeyListener() {   
        public boolean onKey(View v, int keyCode, KeyEvent event) {   
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN &&   
                    event.getAction() == KeyEvent.ACTION_DOWN &&   
                    v == mLayout.getChildAt(mLayout.getChildCount() - 1)) {   
                findViewById(R.id.add_widget).requestFocus();   
                return true;   
            }   
            return false;   
        }   
    };   
       
    private View.OnKeyListener mAddButtonKeyListener = new Button.OnKeyListener() {   
        @Override  
        public boolean onKey(View v, int keyCode, KeyEvent event) {   
            // TODO Auto-generated method stub   
            Log.d(LOG_TAG, event.toString());   
               
            View viewToFoucus = null;   
            if (event.getAction() == KeyEvent.ACTION_DOWN) {   
                switch (keyCode) {   
                case KeyEvent.KEYCODE_DPAD_UP:   
                    int iCount = mLayout.getChildCount();   
                    if ( iCount > 0) {   
                        viewToFoucus = mLayout.getChildAt(iCount - 1);   
                    }   
                    break;   
                case KeyEvent.KEYCODE_DPAD_DOWN:   
                    if (mLayout.getChildCount() > 1) {   
                        viewToFoucus = mLayout.getChildAt(1);   
                    }   
                    break;   
                default:   
                    break;   
                }   
            }   
               
            if (viewToFoucus != null) {   
                viewToFoucus.requestFocus();   
                return true;   
            } else {   
                return false;   
            }   
        }   
    };   
}  