package com.example.spotifyclone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.spotifyclone.objects.MusicPool;
import com.example.spotifyclone.objects.Playlist;
import com.example.spotifyclone.objects.favorite;

import java.util.ArrayList;

public class MusicHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public static final String DATABASE_NAME = "music_database";
    public static final String FAVORITE_TABLE = "favorite_table";
    public static final String PLAYLIST_TABLE = "playlist_table";
    public static final String MUSIC_POOL = "music_pool";
    public static final String COL_ID = "Id";
    public static final String COL_NAME = "Name";
    public static final String COL_PLAY_ID = "Play_id";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS";

    public MusicHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MUSIC_POOL + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Play_id INTEGER)");
        db.execSQL("CREATE TABLE " + FAVORITE_TABLE + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE)");
        db.execSQL("CREATE TABLE " + PLAYLIST_TABLE + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


        db.execSQL(DROP_TABLE + FAVORITE_TABLE);
        db.execSQL(DROP_TABLE + PLAYLIST_TABLE);
        db.execSQL(DROP_TABLE + MUSIC_POOL);

    }

    public boolean insertPlaylist(String Name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, Name);
        long result = db.insert(PLAYLIST_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean deletePlaylist(int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + PLAYLIST_TABLE + " WHERE Id = ?";
        String[] args = {String.valueOf(Id)};
        db.execSQL(sql, args);
        return true;
    }

    public boolean massDelete(int Play_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + MUSIC_POOL + " WHERE Play_id = ?";
        String[] args = {String.valueOf(Play_id)};
        db.execSQL(sql, args);
        return true;
    }

    public boolean addToMusicPool(String Name, int Id){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, Name);
        contentValues.put(COL_PLAY_ID, Id);
        long result = db.insert(MUSIC_POOL, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean checker(String Name, int Play_id){
        String sql = "Select * from " + MUSIC_POOL + " WHERE Name = ? AND Play_id = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{Name, String.valueOf(Play_id)};
        Cursor cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }


    public ArrayList<MusicPool> songFromPlaylist(int Play_id){
        String sql = "Select * from " + MUSIC_POOL + " WHERE Play_id = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MusicPool> ArrayMusicPool = new ArrayList<>();
        String[] args = {String.valueOf(Play_id)};
        Cursor cursor = db.rawQuery(sql, args);
        if (cursor.moveToNext()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String Name = cursor.getString(1);
                int Play_Id = Integer.parseInt(cursor.getString(2));

                MusicPool music = new MusicPool();
                music.setId(id);
                music.setName(Name);
                music.setPlay_Id(Play_Id);
                ArrayMusicPool.add(music);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return ArrayMusicPool;
    }

    public boolean insertFavorite(String Name){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, Name);
        long result = db.insert(FAVORITE_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteFavoriteByName(String Name){
        long result = db.delete(FAVORITE_TABLE, COL_NAME + "=?", new String[]{Name});

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteSongFromPlaylist(int Id){
        long result = db.delete(MUSIC_POOL, COL_ID + "=" + Id, null);
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<Playlist> ListOfPlaylist(){
        String sql = "Select * from " + PLAYLIST_TABLE + " ORDER BY " +COL_ID + " DESC" ;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Playlist> ArrayPlayList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            do{
                int Id = Integer.parseInt(cursor.getString(0));
                String Name = cursor.getString(1);

                Playlist playlist = new Playlist();
                playlist.setId(Id);
                playlist.setName(Name);
                ArrayPlayList.add(playlist);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return ArrayPlayList;
    }


    public ArrayList<favorite> ListOfFavorite(){
        String sql = "Select * from " + FAVORITE_TABLE + " ORDER BY " +COL_ID + " DESC" ;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<favorite> ArrayFavorite = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            do{
                int Id = Integer.parseInt(cursor.getString(0));
                String Name = cursor.getString(1);

                favorite favorite = new favorite();
                favorite.setId(Id);
                favorite.setName(Name);
                ArrayFavorite.add(favorite);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return ArrayFavorite;
    }

    public boolean isFavorite(String name){
        String sql = "Select * from " + FAVORITE_TABLE + " WHERE " + COL_NAME + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = new String[]{name};
        Cursor cursor = db.rawQuery(sql, args);

        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public boolean deleteFavorite(int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + FAVORITE_TABLE + " WHERE Id = ?";
        String[] args = {String.valueOf(Id)};
        db.execSQL(sql, args);
        return true;
    }

    public Cursor getFav(){
        String sql = "Select * from " + FAVORITE_TABLE + " ORDER BY " +COL_ID + " DESC" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;

    }



}
