package com.eshare.dlna.dmc.gui.customview;

import java.io.File;
import java.util.ArrayList;

import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.item.MusicTrack;

import com.eshare.dlna.dmc.gui.activity.AppPreference;
import com.eshare.dlna.dmc.http.HTTPRequestHandler;
import com.eshare.dlna.dmc.utility.Utility;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

public abstract class LocalMusicUtils {
	public static ArrayList<MusicTrack> readLocalMusic(Context mcontext){
		Cursor cursor = mcontext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, 
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		ArrayList<MusicTrack> localMusics = null;
		while(cursor.moveToNext()){
	        //歌曲编号
	        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));  
	        //歌曲标题
	        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  
	        //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
	        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  
	        //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
	        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));  
	        //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
	        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    
	        //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
	        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));    
	        //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
	        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
	       
	        if(size>1024*800){//大于800K
	        	File subFile=new File(url);
	        	Log.e("LocalMusicUtils", url);
	    		String fileName = subFile.getName();	    		
	    		int dotPos = subFile.getName().lastIndexOf(".");
	    		String fileExtension = dotPos != -1 ? fileName.substring(dotPos) : null;
	    		if (fileExtension != null) {
	    			fileExtension = fileExtension.toLowerCase().replace(".", "");
	    		}
	    		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

				MusicTrack musicTrack = new MusicTrack("0/1/" + title, "0/1", title,
						AppPreference.getLocalServerName(), "", "", new Res(new ProtocolInfo("http-get:*:" + mimeType + ":"
								+ HTTPRequestHandler.getDLNAHeaderValue(mimeType)), size,
								Utility.createLink(subFile)));
				localMusics.add(musicTrack);
	        }
		}
		return localMusics;
	}
}
