package com.example.testapplication.shared.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.price, price) == 0 &&
                quantity == item.quantity &&
                Objects.equals(name, item.name) &&
                Objects.equals(packaging, item.packaging);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity, packaging);
    }
}
