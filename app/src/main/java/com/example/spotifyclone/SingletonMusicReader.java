package com.example.spotifyclone;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class SingletonMusicReader {

    private static volatile SingletonMusicReader instance = null;
    private ArrayList<File> defaultlist;
    private Context context;


    private SingletonMusicReader(Context context){
        this.context = context;
    }

    public static SingletonMusicReader getInstance(Context context){
        if (instance == null){
            synchronized (SingletonArraylist.class){
                if (instance == null){
                    instance = new SingletonMusicReader(context);
                }
            }
        }
        return instance;
    }


    public ArrayList<File> getDefaultlist(){
        if (instance.defaultlist == null){
            instance.defaultlist = new ArrayList<>();
        }
        return instance.defaultlist;
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
