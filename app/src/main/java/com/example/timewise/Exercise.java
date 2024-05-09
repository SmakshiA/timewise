package com.example.timewise;
public class Exercise {
    private int id;
    private int imageResource;
    private String name;
    private int duration;
    private int audioResource;

    // Constructor with ID
    public Exercise(int imageResource, String name, int duration, int audioResource) {

        this.imageResource = imageResource;
        this.name = name;
        this.duration = duration;
        this.audioResource = audioResource;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getAudioResource() {
        return audioResource;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

}