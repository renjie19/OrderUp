package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Consumer extends RealmObject implements Parcelable {

    @PrimaryKey
    private long id;
    private long date;
    private String name;
    private String location;
    private RealmList<Item> orders;
    private String status;
    private double total;

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public RealmList<Item> getOrders() {
        return this.orders;
    }

    public void setOrders(RealmList<Item> orders2) {
        this.orders = orders2;
    }

    public double getTotal() {
        double total = 0;
        if (orders != null) {
            for (Item item : orders) {
                total += item.getPrice();
            }
        }
        return total;
    }

    public void setTotal(double total2) {
        this.total = total2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
//
//    @Override
//    public String toString() {
//        String consumer = String.format("%s\n%s\n%s\n%s\n%s",date,name,location,status,total);
//        if(orders != null){
//            for(Item item : orders) consumer += item.toString();
//        }
//        return consumer;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.date);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeTypedList(this.orders);
        dest.writeString(this.status);
        dest.writeDouble(this.total);
    }

    public Consumer() {
    }

    protected Consumer(Parcel in) {
        this.id = in.readLong();
        this.date = in.readLong();
        this.name = in.readString();
        this.location = in.readString();
        this.orders = new RealmList<>();
        this.orders.addAll(in.createTypedArrayList(Item.CREATOR));
        this.status = in.readString();
        this.total = in.readDouble();
    }

    public static final Parcelable.Creator<Consumer> CREATOR = new Parcelable.Creator<Consumer>() {
        @Override
        public Consumer createFromParcel(Parcel source) {
            return new Consumer(source);
        }

        @Override
        public Consumer[] newArray(int size) {
            return new Consumer[size];
        }
    };
}
