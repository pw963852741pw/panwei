package com.bvin.study.progress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;




public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    ProgressBar pb ;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            pb.setProgress(msg.what);
        }
    
        
        
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    
    void init(){
        pb = (ProgressBar)findViewById(R.id.pb);
        Button bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
                pb.incrementProgressBy(10);
                
                
            }
        });
    }
}