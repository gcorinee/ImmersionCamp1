// Used for storing contacts

package com.example.immersioncamp1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ContactsModal implements Parcelable {
    private String userName;
    private String phoneNumber;
    private String organization;
    private String email;
    private Bitmap photo;

    public ContactsModal (String userName, String phoneNumber, String organization, String email, Bitmap photo) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
        this.email = email;
        this.photo = photo;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Bitmap getPhoto() { return photo; }
    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public ContactsModal(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.userName = data[0];
        this.phoneNumber = data[1];
        this.organization = data[2];
        this.email = data[3];
        this.photo = in.readParcelable(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[] {
                this.userName,
                this.phoneNumber,
                this.organization,
                this.email,
        };
        dest.writeStringArray(data);
        dest.writeParcelable(this.photo, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ContactsModal createFromParcel(Parcel in) {
            return new ContactsModal(in);
        }
        public ContactsModal[] newArray(int size) {
            return new ContactsModal[size];
        }
    };
}


