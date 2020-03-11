package com.example.testapplication.shared.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private Client client;
    private long date;
    private String status;
    private boolean sent;
    private double total;
    private RealmList<Item> items;

    public double getTotal() {
        total = 0;
        for (Item item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.client, flags);
        dest.writeLong(this.date);
        dest.writeString(this.status);
        dest.writeByte(this.sent ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.total);
        dest.writeTypedList(this.items);
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.client = in.readParcelable(Client.class.getClassLoader());
        this.date = in.readLong();
        this.status = in.readString();
        this.sent = in.readByte() != 0;
        this.total = in.readDouble();
        this.items = new RealmList<>();
        this.items.addAll(in.createTypedArrayList(Item.CREATOR));
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
