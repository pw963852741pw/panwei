package com.eshare.dlna.fm.ximalaya.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eshare.dlna.fm.ximalaya.domain.Albums;
import com.eshare.dlna.fm.ximalaya.domain.CategoryInfos;
import com.eshare.dlna.fm.ximalaya.domain.TagInfos;
import com.eshare.dlna.fm.ximalaya.domain.Tracks;

public class XiMaLaYaDatabase extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ximalayadb.sqlite";
	private static final int DB_VERSION = 7;
	
	public XiMaLaYaDatabase(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE categories (id INTEGER primary key, "
				+ " title TEXT DEFAULT '', "
				+ " cover_url TEXT DEFAULT '')");
		db.execSQL("CREATE TABLE tags (id integer primary key AUTOINCREMENT, "
				+ " category_id INTEGER not null, "
				+ " name TEXT DEFAULT '', "
				+ " cover_url_small TEXT DEFAULT '', "
				+ " cover_url_large TEXT DEFAULT '')");
		db.execSQL("CREATE TABLE albums (id_key integer primary key AUTOINCREMENT, "
				+ " category_id integer not null, "
				+ " id integer not null, "
				+ " title TEXT DEFAULT '', "
				+ " cover_url_small TEXT DEFAULT '')");
		db.execSQL("CREATE TABLE tracks (id_key integer primary key AUTOINCREMENT, "
				+ " id integer not null, "
				+ " title TEXT DEFAULT '', "
				+ " cover_url_small TEXT DEFAULT '', "
				+ " cover_url_large TEXT DEFAULT '', "
				+ " play_url_64 TEXT DEFAULT '', "
				+ " duration TEXT DEFAULT '')");
		db.execSQL("CREATE TABLE love_tracks (id_key integer primary key AUTOINCREMENT, "
				+ " id integer not null, "
				+ " title TEXT DEFAULT '', "
				+ " cover_url_small TEXT DEFAULT '', "
				+ " cover_url_large TEXT DEFAULT '', "
				+ " play_url_64 TEXT DEFAULT '', "
				+ " duration TEXT DEFAULT '')");
		db.execSQL("CREATE TABLE love_albums (id_key integer primary key AUTOINCREMENT, "
				+ " category_id integer not null, "
				+ " id integer not null, "
				+ " title TEXT DEFAULT '', "
				+ " cover_url_small TEXT DEFAULT '')");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS categories");
		db.execSQL("DROP TABLE IF EXISTS tags");
		db.execSQL("DROP TABLE IF EXISTS albums");
		db.execSQL("DROP TABLE IF EXISTS tracks");
		db.execSQL("DROP TABLE IF EXISTS love_tracks");
		db.execSQL("DROP TABLE IF EXISTS love_albums");
		onCreate(db);
	}
	
//清除数据
	public void clearCategory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from categories");
		db.close();
	}
	public void clearTag() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from tags");
		db.close();
	}
	public void clearAlbums() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from albums");
		db.close();
	}
	public void clearTracks() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from tracks");
		db.close();
	}	
	public void clearLove_tracks() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from love_tracks");
		db.close();
	}
	public void clearLove_albums() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from love_albums");
		db.close();
	}


	

	//存储分类
	public int saveCategory(CategoryInfos info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id",info.getId());
		values.put("title",info.getTitle());
		values.put("cover_url",info.getCover_url());
	
		long res = db.insertOrThrow("categories", null, values);
		if (res == -1) {			
			return db.update("categories", values, "id=?", 
					new String[]{ String.valueOf(info.getId())});
		} else {
			db.close();
			return 1;
		}
	}
	//存储标签
	public int saveTag(TagInfos info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category_id",info.getCategory_id());
		values.put("name",info.getName());
		values.put("cover_url_small",info.getCover_url_small());
		values.put("cover_url_large",info.getCover_url_large());
		long res = db.insertOrThrow("tags", null, values);
//		Log.e("panwei", res+"   in db");

		if (res == -1) {
			return db.update("tags", values, "name=?", 
					new String[]{ info.getName()});
		} else {
			db.close();
			return 1;
		}
	}
	//存储专辑
	public int saveAlbums(Albums info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category_id",info.getCategory_id());
		values.put("id",info.getId());
		values.put("title",info.getTitle());
		values.put("cover_url_small",info.getCover_url_small());
		long res = db.insertOrThrow("albums", null, values);
//		Log.e("panwei", res+"   in db");

		if (res == -1) {			
			return db.update("albums", values, "title=?", 
					new String[]{ info.getTitle()});
		} else {
			db.close();
			return 1;
		}
	}
	//存储专辑下声音信息
	public int saveTracks(Tracks info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id",info.getId());
		values.put("title",info.getTitle());
		values.put("cover_url_small",info.getCover_url_small());
		values.put("cover_url_large",info.getCover_url_large());
		values.put("play_url_64",info.getPlay_url_64());
		values.put("duration",info.getDuration());

		long res = db.insertOrThrow("tracks", null, values);
//		Log.e("panwei", res+"   in db");

		if (res == -1) {			
			return db.update("tracks", values, "title=?", 
					new String[]{ info.getTitle()});
		} else {
			db.close();
			return 1;
		}
	}
	//存储love声音信息
	public int saveLove_tracks(Tracks info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id",info.getId());
		values.put("title",info.getTitle());
		values.put("cover_url_small",info.getCover_url_small());
		values.put("cover_url_large",info.getCover_url_large());
		values.put("play_url_64",info.getPlay_url_64());
		values.put("duration",info.getDuration());

		long res = db.insertOrThrow("love_tracks", null, values);
//		Log.e("panwei", res+"   in db");

		if (res == -1) {			
			return db.update("love_tracks", values, "title=?", 
					new String[]{ info.getTitle()});
		} else {
			db.close();
			return 1;
		}
	}
	//存储love专辑信息
	public int saveLove_albums(Albums info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category_id",info.getCategory_id());
		values.put("id",info.getId());
		values.put("title",info.getTitle());
		values.put("cover_url_small",info.getCover_url_small());
		long res = db.insertOrThrow("love_albums", null, values);
//		Log.e("panwei", res+"   in db");

		if (res == -1) {		
			return db.update("love_albums", values, "title=?", 
					new String[]{ info.getTitle()});
		} else {
			db.close();
			return 1;
		}
	}
	
	//获取分类信息
	public List<CategoryInfos>  getCategory() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<CategoryInfos> list = new ArrayList<CategoryInfos>();
		Cursor cursor = db.rawQuery("select * from categories order by id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				CategoryInfos info = new CategoryInfos(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	//获取分类信息
	public List<TagInfos>  getTag() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<TagInfos> list = new ArrayList<TagInfos>();
		Cursor cursor = db.rawQuery("select * from tags order by category_id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				TagInfos info = new TagInfos(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	public List<Albums>  getAlbums() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Albums> list = new ArrayList<Albums>();
		Cursor cursor = db.rawQuery("select * from albums order by id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				Albums info = new Albums(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	public List<Tracks>  getTracks() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Tracks> list = new ArrayList<Tracks>();
		Cursor cursor = db.rawQuery("select * from tracks order by id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				Tracks info = new Tracks(cursor.getInt(1), cursor.getString(2)
						, cursor.getString(3), cursor.getString(4), cursor.getString(5)
							, cursor.getString(6));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	public List<Tracks>  getLove_tracks() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Tracks> list = new ArrayList<Tracks>();
		Cursor cursor = db.rawQuery("select * from love_tracks order by id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				Tracks info = new Tracks(cursor.getInt(1), cursor.getString(2)
						, cursor.getString(3), cursor.getString(4), cursor.getString(5)
							, cursor.getString(6));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	public List<Albums>  getLove_albums() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Albums> list = new ArrayList<Albums>();
		Cursor cursor = db.rawQuery("select * from love_albums order by id asc", null);
		if (cursor.moveToFirst()) 
		{
			do {
				Albums info = new Albums(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
				list.add(info);
			} while(cursor.moveToNext());
			cursor.close();
			db.close();
			return list;
		}
		else return null;
	}
	public boolean find_tracks(Tracks info){
		boolean result=false;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from love_tracks where title=?"
				, new String[]{info.getTitle()});
		if(cursor.moveToFirst()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
		
	}
	public boolean delete_tracks(Tracks info){
		boolean result=false;
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("delete from love_tracks where title=?", new String[]{info.getTitle()});
		result=!find_tracks(info);
		db.close();
		return result;
	}
	public boolean find_albums(Albums info){
		boolean result=false;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from love_albums where title=?"
				, new String[]{info.getTitle()});
		if(cursor.moveToFirst()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
		
	}
	public boolean delete_albums(Albums info){
		boolean result=false;
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("delete from love_albums where title=?", new String[]{info.getTitle()});
		result=!find_albums(info);
		db.close();
		return result;
	}
}
