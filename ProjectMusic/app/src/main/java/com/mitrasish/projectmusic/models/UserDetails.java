package com.mitrasish.projectmusic.models;

public class UserDetails {
    private String FullName;
    private String MobileNumber;
    private String Address;

    public UserDetails(String fullName, String mobileNumber, String address) {
        FullName = fullName;
        MobileNumber = mobileNumber;
        Address = address;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
