package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Consumer extends RealmObject implements Parcelable {

    private String token;
    private String name;
    private String location;
    private long date;
    private RealmList<Item> items;
    private String status;
    private double total;
    private boolean sent;

    public double getTotal() {
        double total = 0;
        if (items != null) {
            for (Item item : items) {
                total += item.getPrice();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        String consumer = String.format("%s\n%s\n%s\n%s\n%s\n",date,name,location,status,total);
        if(items != null){
            for(Item item : items) consumer += item.toString();
        }
        return consumer;
    }

    public Consumer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeLong(this.date);
        dest.writeTypedList(this.items);
        dest.writeString(this.status);
        dest.writeDouble(this.total);
        dest.writeByte(this.sent ? (byte) 1 : (byte) 0);
    }

    protected Consumer(Parcel in) {
        this.token = in.readString();
        this.name = in.readString();
        this.location = in.readString();
        this.date = in.readLong();
        this.items = new RealmList<>();
        this.items.addAll(in.createTypedArrayList(Item.CREATOR));
        this.status = in.readString();
        this.total = in.readDouble();
        this.sent = in.readByte() != 0;
    }

    public static final Creator<Consumer> CREATOR = new Creator<Consumer>() {
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
