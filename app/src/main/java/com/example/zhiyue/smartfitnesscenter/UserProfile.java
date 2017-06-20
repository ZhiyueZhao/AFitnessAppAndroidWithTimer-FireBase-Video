package com.example.zhiyue.smartfitnesscenter;

/**
 * Created by ZhiYue on 4/12/2017.
 */

public class UserProfile {
    private String name;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;
    private int age, heartRate;
    private double height, weight;

    public UserProfile(){

    }

    public UserProfile(String name, int age, int heartRate, double height, double weight, String gender) {
        this.name = name;
        this.age = age;
        this.heartRate = heartRate;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
