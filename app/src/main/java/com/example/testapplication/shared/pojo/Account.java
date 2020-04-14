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
public class Account extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String token;
    private String firstName;
    private String lastName;
    private String location;
    private String contactNumber;
    private String email;
    private RealmList<Client> clients;

    public Account() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.location);
        dest.writeString(this.contactNumber);
        dest.writeString(this.email);
        dest.writeString(this.token);
        dest.writeTypedList(this.clients);
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.location = in.readString();
        this.contactNumber = in.readString();
        this.email = in.readString();
        this.token = in.readString();
        this.clients = new RealmList<>();
        this.clients.addAll(in.createTypedArrayList(Client.CREATOR));
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
