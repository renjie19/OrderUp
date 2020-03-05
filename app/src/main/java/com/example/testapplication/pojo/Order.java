package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends RealmObject implements Parcelable {
    private Client client;
    private long date;
    private String status;
    private boolean sent;
    private double total;
    private RealmList<Item> items;

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.client, flags);
        dest.writeLong(this.date);
        dest.writeString(this.status);
        dest.writeByte(this.sent ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.total);
        dest.writeTypedList(this.items);
    }

    protected Order(Parcel in) {
        this.client = in.readParcelable(Client.class.getClassLoader());
        this.date = in.readLong();
        this.status = in.readString();
        this.sent = in.readByte() != 0;
        this.total = in.readDouble();
        this.items = new RealmList<>();
        this.items.addAll(in.createTypedArrayList(Item.CREATOR));
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
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
