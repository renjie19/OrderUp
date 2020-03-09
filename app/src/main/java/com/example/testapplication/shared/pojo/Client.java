package com.example.testapplication.shared.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends RealmObject implements Parcelable {
    @PrimaryKey
    private String token;
    private String name;
    private String location;
    private String contactNo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.contactNo);
    }

    protected Client(Parcel in) {
        this.token = in.readString();
        this.name = in.readString();
        this.location = in.readString();
        this.contactNo = in.readString();
    }

    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public Client() {
    }
}
