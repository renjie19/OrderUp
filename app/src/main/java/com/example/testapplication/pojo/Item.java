package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject implements Parcelable {
    @PrimaryKey
    private long id;
    private String name;
    private double price;
    private int quantity;
    private String packaging;

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price2) {
        this.price = price2;
    }

    public String getPackaging() {
        return this.packaging;
    }

    public void setPackaging(String classification2) {
        this.packaging = classification2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packaging);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.quantity);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.packaging = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
        this.price = in.readDouble();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
//
//    @Override
//    public String toString() {
//       return String.format("%s - %s %s = %.2f",name,quantity,packaging,price);
//    }
}
