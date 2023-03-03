package com.example.musica;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String path, title, duration;
    public AudioModel(String path,String title, String duration)
    {
        this.path = path;
        this.title = title;
        this.duration = duration;

    }
    public String getPath()
    {
     return path;
    }
    public void setPath(String Path)
    {
    this.path = Path;
    }
    public String getTitle()
    {
        return title;
    }
    public String getDuration()
    {
        return duration;
    }

}
