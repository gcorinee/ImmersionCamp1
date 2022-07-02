// Used for storing contacts

package com.example.immersioncamp1;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ContactsModal implements Parcelable {
    private String contactId;
    private String userName;
    private String phoneNumber;
    private String organization;
    private String email;
    private Uri photoUri;

    public ContactsModal (String contactId, String userName, String phoneNumber, String organization, String email, Uri photoUri) {
        this.contactId = contactId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
        this.email = email;
        this.photoUri = photoUri;
    }
    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }
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
    public Uri getPhotoUri() { return photoUri; }
    public void setPhotoUri(Uri photoUri) { this.photoUri = photoUri; }

    public ContactsModal(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.contactId = data[0];
        this.userName = data[1];
        this.phoneNumber = data[2];
        this.organization = data[3];
        this.email = data[4];
        this.photoUri = Uri.parse(data[5]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[] {
                this.contactId,
                this.userName,
                this.phoneNumber,
                this.organization,
                this.email,
                this.photoUri.toString()
        };
        dest.writeStringArray(data);
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


