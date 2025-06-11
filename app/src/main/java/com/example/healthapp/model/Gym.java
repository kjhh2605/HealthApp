package com.example.healthapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Gym implements Serializable {
    private String name;
    private String region;
    private ArrayList<String> machineList;
    private HashMap<String, ArrayList<String>> trainerList;
    private int price;

    public Gym() {
        this.machineList = new ArrayList<>();
        this.trainerList = new HashMap<>();
    }

    public String getName() { return name; }
    public String getRegion() { return region; }
    public ArrayList<String> getMachineList() { return machineList; }
    public HashMap<String, ArrayList<String>> getTrainerList() { return trainerList; }
    public int getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setRegion(String region) { this.region = region; }
    public void setMachineList(ArrayList<String> machineList) { this.machineList = machineList; }
    public void setTrainerList(HashMap<String, ArrayList<String>> trainerList) { this.trainerList = trainerList; }
    public void setPrice(int price) { this.price = price; }
}

