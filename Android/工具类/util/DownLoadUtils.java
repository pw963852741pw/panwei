package com.eshare.dlna.fm.ximalaya.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.eshare.dlna.dmc.gui.activity.MainActivity;
import com.eshare.emusic.client.R;

public class DownLoadUtils {
	/**
	 * 从path获取json字符串
	 * @param path   url地址
	 * @return       返回json字符串
	 */
	public static String getJsonString(String path){
		try {
			URL url=new URL(path);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			int code=connection.getResponseCode();
			if(code==200){
				String responseStr = readStream(connection.getInputStream());
				Log.e("getJsonString", responseStr);
				return responseStr;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("panwei", "ioerro");
		}
		return "";
	}

	private static String readStream(InputStream inputStream) {
		
		String jsonString="";
		ByteArrayOutputStream outPutStream=new ByteArrayOutputStream();
		byte[] data=new byte[1024];
		int len=0;
		try {
			while((len=inputStream.read(data))!=-1){
				outPutStream.write(data, 0, len);
			}
			jsonString=new String(outPutStream.toByteArray());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("panwei", "ioerro");

		}
		return jsonString;
	}
	
	/**
	 * 加载图标
	 * @param path 图片路径
	 * @param icon	图标放置位置
	 */
	public static void loadIcon(final String path,final ImageView icon) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(path);
					final Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					MainActivity.INSTANCE.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							icon.setImageBitmap(bm);
							icon.invalidate();
						}
					});

				} catch (Exception ex) {

					MainActivity.INSTANCE.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							icon.setImageResource(R.drawable.ic_radio);
							icon.invalidate();
						}
					});
				}
			}
		}).start();
	}


}
