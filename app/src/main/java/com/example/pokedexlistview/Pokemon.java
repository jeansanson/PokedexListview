package com.example.pokedexlistview;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pokemon implements Serializable {
    private int id;
    private String name;
    private String img;
    private String height;
    private String weight;
    private Bitmap imgBitmap;
    private List<String> type = new ArrayList<>();
    private List<String> weakness = new ArrayList<>();

    public List<String> getWeakness() {
        return weakness;
    }

    public void addFraqueza(String fraqueza){
        this.weakness.add(fraqueza);
    }

    public Pokemon() {
    }

    public List<String> getType() {
        return type;
    }

    public Pokemon(int id, String name, String img, String height, String weight) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.height = height;
        this.weight = weight;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }


    public void addTipo(String tipo) {
        this.type.add(tipo);
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String toString() {
        return name;
    }
}
