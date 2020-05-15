package com.example.testapplication.shared.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Order extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private Client client;
    private long date;
    private String status;
    private boolean forPayment;
    private double total;
    private RealmList<Item> items;
    private boolean isPriceEditable;

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
        dest.writeByte(this.forPayment ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.total);
        dest.writeTypedList(this.items);
        dest.writeByte(this.isPriceEditable ? (byte) 1 : (byte) 0);
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.client = in.readParcelable(Client.class.getClassLoader());
        this.date = in.readLong();
        this.status = in.readString();
        this.forPayment = in.readByte() != 0;
        this.total = in.readDouble();
        this.items = new RealmList<>();
        this.items.addAll(in.createTypedArrayList(Item.CREATOR));
        this.isPriceEditable = in.readByte() != 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return date == order.date &&
                forPayment == order.forPayment &&
                Double.compare(order.total, total) == 0 &&
                isPriceEditable == order.isPriceEditable &&
                Objects.equals(id, order.id) &&
                Objects.equals(client, order.client) &&
                Objects.equals(status, order.status) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, date, status, forPayment, total, items, isPriceEditable);
    }
}
