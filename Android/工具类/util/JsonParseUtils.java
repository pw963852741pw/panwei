package com.eshare.dlna.fm.ximalaya.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.eshare.dlna.fm.ximalaya.domain.Albums;
import com.eshare.dlna.fm.ximalaya.domain.CategoryInfos;
import com.eshare.dlna.fm.ximalaya.domain.TagInfos;
import com.eshare.dlna.fm.ximalaya.domain.Tracks;

public class JsonParseUtils {
	public static List<CategoryInfos> getCategoryInfosByJson(String jsonStr){
		Log.e("panwei", jsonStr);
		List<CategoryInfos> list=new ArrayList<CategoryInfos>();
		JSONObject jsonobj=null;
		try {
			jsonobj = new JSONObject(jsonStr);
			JSONArray jsonarray=jsonobj.getJSONArray("categories");
			for(int i=0;i<jsonarray.length();i++){
			JSONObject  obj=jsonarray.getJSONObject(i);
			CategoryInfos info=new CategoryInfos
					(obj.getInt("id"), obj.getString("title"), obj.getString("cover_url"));
			list.add(info);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	public static List<TagInfos> getTagInfosByJson(String jsonStr){
		Log.e("panwei", jsonStr);
		List<TagInfos> list=new ArrayList<TagInfos>();
		JSONObject jsonobj=null;
		try {
			jsonobj = new JSONObject(jsonStr);
			int category_id=jsonobj.getInt("category_id");
			JSONArray jsonarray=jsonobj.getJSONArray("tags");
			for(int i=0;i<jsonarray.length();i++){
			JSONObject  obj=jsonarray.getJSONObject(i);
			TagInfos info=new TagInfos
					(category_id, obj.getString("name"), obj.getString("cover_url_small"), obj.getString("cover_url_large"));
			list.add(info);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	public static List<Albums> getAlbumsByJson(String jsonStr){
		Log.e("panwei", jsonStr);
		List<Albums> list=new ArrayList<Albums>();
		JSONObject jsonobj=null;
		try {
			jsonobj = new JSONObject(jsonStr);
			int category_id=jsonobj.getInt("category_id");
			JSONArray jsonarray=jsonobj.getJSONArray("albums");
			for(int i=0;i<jsonarray.length();i++){
			JSONObject  obj=jsonarray.getJSONObject(i);
			Albums info=new Albums
					(category_id, obj.getInt("id"), obj.getString("title"), obj.getString("cover_url_small"));
			list.add(info);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	public static List<Tracks> getTracksByJson(String jsonStr){
		Log.e("panwei", jsonStr);
		List<Tracks> list=new ArrayList<Tracks>();
		JSONObject jsonobj,jsonobj2=null;
		try {
			jsonobj = new JSONObject(jsonStr);
			jsonobj2=jsonobj.getJSONObject("album");
			JSONArray jsonarray=jsonobj2.getJSONArray("tracks");
			for(int i=0;i<jsonarray.length();i++){
			JSONObject  obj=jsonarray.getJSONObject(i);
			Tracks info=new Tracks
					(obj.getInt("id"), obj.getString("title"), obj.getString("cover_url_small")
							, obj.getString("cover_url_large"), obj.getString("play_url_64")
							, obj.getString("duration"));
			list.add(info);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	public static List<Tracks> getHotTracksByJson(String jsonStr){
		Log.e("panwei", jsonStr);
		List<Tracks> list=new ArrayList<Tracks>();
		JSONObject jsonobj=null;
		try {
			jsonobj = new JSONObject(jsonStr);
			JSONArray jsonarray=jsonobj.getJSONArray("tracks");
			for(int i=0;i<jsonarray.length();i++){
			JSONObject  obj=jsonarray.getJSONObject(i);
			Tracks info=new Tracks
					(obj.getInt("id"), obj.getString("title"), obj.getString("cover_url_small")
							, obj.getString("cover_url_large"), obj.getString("play_url_64")
							, obj.getString("duration"));
			list.add(info);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
}
