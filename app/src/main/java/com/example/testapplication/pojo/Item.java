package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Item extends RealmObject implements Parcelable {
    private String name;
    private double price;
    private int quantity;
    private String packaging;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packaging);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.quantity);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.packaging = in.readString();
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

    @Override
    public String toString() {
       return String.format("%s - %s %s = %.2f",name,quantity,packaging,price);
    }
}
