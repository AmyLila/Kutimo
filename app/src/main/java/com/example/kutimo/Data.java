package com.example.kutimo;

import java.util.ArrayList;

public class Data {
    public int faithPoints;
    Save save;
    public ArrayList<String> scriptures = new ArrayList<>();


    public Data() {

        faithPoints = 0;
        faithPoints = save.retrieveFaithPoints();


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
