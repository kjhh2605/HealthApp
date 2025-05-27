package com.example.healthapp.model;

public class Gym {
    private String name;
    private String region;
//    private List<Machine> machineList;
    //private List<Trainer> trainerList;
    private int price;
    public Gym() {}
    public Gym(String name, String region, int price) {
        this.name = name;
        this.region = region;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

//    public List<Machine> getMachineList() {
//        return machineList;
//    }

//    public List<Trainer> getTrainerList() {
//        return trainerList;
//    }

    public int getPrice() {
        return price;
    }
}
