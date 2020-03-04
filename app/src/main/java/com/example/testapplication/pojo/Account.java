package com.example.testapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Data;

@Data
public class Account extends RealmObject implements Parcelable {
    private String firstName;
    private String lastName;
    private String location;
    private String contactNumber;
    private String token;
    private RealmList<Consumer> consumers;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.location);
        dest.writeString(this.contactNumber);
        dest.writeString(this.token);
        dest.writeTypedList(this.consumers);
    }

    protected Account(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.location = in.readString();
        this.contactNumber = in.readString();
        this.token = in.readString();
        this.consumers = new RealmList<>();
        this.consumers.addAll(in.createTypedArrayList(Consumer.CREATOR));
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public Account() {
    }
}
