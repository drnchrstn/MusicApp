package com.example.spotifyclone.objects;

public class MusicPool {
    int Id, play_Id;
    String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPlay_Id() {
        return play_Id;
    }

    public void setPlay_Id(int play_Id) {
        this.play_Id = play_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
