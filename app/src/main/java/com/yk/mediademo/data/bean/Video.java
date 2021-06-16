package com.yk.mediademo.data.bean;

import java.io.Serializable;

public class Video implements Serializable {
    private String name;
    private String path;

    public Video(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LocalImage{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
