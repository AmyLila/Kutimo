package com.example.kutimo;

import java.util.ArrayList;

public class Data {
    public int faithPoints;
    public ArrayList<String> scriptures = new ArrayList<>();

    public Data() {
        faithPoints = 5;


        scriptures.add("Hello");
        scriptures.add("Hola");
        scriptures.add("Bonjour");
        scriptures.add("Ciao");
    }



    @Override
    public String toString() {
        return "Data{" +
                "faithPoints=" + faithPoints +
                ", scriptures=" + scriptures +
                '}';
    }

}
