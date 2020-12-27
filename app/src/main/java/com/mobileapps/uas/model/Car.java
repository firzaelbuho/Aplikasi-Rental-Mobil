package com.mobileapps.uas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {

    private String id;
    private String name;
    private int year;
    private int cost;
    private String imgUrl;
    private boolean isAvailable;

    public Car(){

    }

    public Car(String id, String name, int year, int cost, String imgUrl, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.cost = cost;
        this.imgUrl = imgUrl;
        this.isAvailable = isAvailable;
    }

    protected Car(Parcel in) {
        id = in.readString();
        name = in.readString();
        year = in.readInt();
        cost = in.readInt();
        imgUrl = in.readString();
        isAvailable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(year);
        dest.writeInt(cost);
        dest.writeString(imgUrl);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
