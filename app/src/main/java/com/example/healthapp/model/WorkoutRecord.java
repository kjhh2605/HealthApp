package com.example.healthapp.model;

public class WorkoutRecord {
    private String workout;
    private String machine;
    private String part;
    private String etc;
    private double weight;
    private int count;

    private long timestamp;

    public WorkoutRecord(String workout, String machine, String part, String etc, double weight, int count, long timestamp) {
        this.workout = workout;
        this.machine = machine;
        this.part = part;
        this.etc = etc;
        this.weight = weight;
        this.count = count;
        this.timestamp = timestamp;
    }

    public WorkoutRecord() {
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
