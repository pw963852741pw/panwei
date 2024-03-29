package com.test;
//Download by http://ww.codefans.net
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	/* 声明 Button、ImageView对象 */
	private ImageView mImageView01;
	private ImageView mImageView02;
	private Button mButton01;
	private Button mButton02;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 取得 Button、ImageView对象 */
		mImageView01 = (ImageView) findViewById(R.id.myImageView1);
		mImageView02 = (ImageView) findViewById(R.id.myImageView2);
		mButton01 = (Button) findViewById(R.id.myButton1);
		mButton02 = (Button) findViewById(R.id.myButton2);

		/* 设置ImageView背景图 */
		mImageView01.setImageDrawable(getResources().getDrawable(
				R.drawable.right));
		mImageView02
				.setImageDrawable(getResources().getDrawable(R.drawable.oa));

		/* 用OnClickListener事件来启动 */
		mButton01.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/* 当启动后，ImageView立刻换背景图 */
				mImageView01.setImageDrawable(getResources().getDrawable(
						R.drawable.right));
			}
		});

		mButton02.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mImageView01.setImageDrawable(getResources().getDrawable(
						R.drawable.left));
			}
		});
	}
}