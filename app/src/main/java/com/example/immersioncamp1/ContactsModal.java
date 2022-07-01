// Used for storing contacts

package com.example.immersioncamp1;

import android.renderscript.ScriptGroup;

import java.io.InputStream;

public class ContactsModal {
    private String userName;
    private String phoneNumber;
    private String organization;
    private String email;
    private InputStream photo;

    public ContactsModal (String userName, String phoneNumber, String organization, String email, InputStream photo) {
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
    public InputStream getPhoto() { return photo; }
    public void setPhoto(InputStream photo) { this.photo = photo; }
}
