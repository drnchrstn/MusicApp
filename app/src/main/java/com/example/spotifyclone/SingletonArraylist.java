package com.example.spotifyclone;

import android.content.Context;
import android.os.Environment;

import com.example.spotifyclone.objects.MusicPool;

import java.io.File;
import java.util.ArrayList;

public class SingletonArraylist {

    private static volatile SingletonArraylist instance = null;

    private ArrayList<File> songList;
    private ArrayList<File> defaultlist;
    private Context context;




    private SingletonArraylist(Context context){
        this.context = context;
        readSongs(Environment.getExternalStorageDirectory());
    }

    public static SingletonArraylist getInstance(Context context){
        if (instance == null){
            synchronized (SingletonArraylist.class){
                if (instance == null){
                    instance = new SingletonArraylist(context);
                }
            }
        }
        return instance;
    }

    public ArrayList<File> getSongList(){
        if (instance.songList == null){
            instance.songList = new ArrayList<>();
        }
        return instance.songList;
    }

    public ArrayList<File> myDefaultList(){
        if (instance.defaultlist == null){
            instance.defaultlist = new ArrayList<>();
        }
        return instance.defaultlist;
    }


    public ArrayList<File> getdefaultlist(){
        instance.songList = readSongs(Environment.getExternalStorageDirectory());
        return instance.songList;
    }


    public void getPlaylistSong(ArrayList<MusicPool> musicPool){
        ArrayList<File> arrayList = new ArrayList<File>();

        if (musicPool == null){
            return;
        }else {


            for (int x = 0; x < musicPool.size(); x++) {
                for (int i = 0; i < songList.size(); i++) {
                    String musicpoolName = musicPool.get(x).getName();
                    String songlistName = songList.get(i).getName().replace(".mp3", "");
                    if (musicpoolName.equals(songlistName)) {
                        arrayList.add(songList.get(i));
                    }
                }
            }

        }

        instance.songList = arrayList;

    }





    public ArrayList<File> readSongs(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File file : files ){
            if (file.isDirectory() && !file.isHidden()){
                arrayList.addAll(readSongs(file));
            }else{
                if (file.getName().endsWith(".mp3")){
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }
}
