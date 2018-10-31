package com.texaconnect.texa.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Address implements Parcelable {
    public String id;
    public String country;
    public String lastName;
    public String firstName;
    public String line1;
    public String line2;
    public String city;
    public String locality;
    public String flatno;
    public String state;
    public String zipcode;
    public String landmark;
    public String street1;
    public String name;
    public String mobile;
    public Type type;

    public boolean isEmpty() {
        return TextUtils.isEmpty(name) || TextUtils.isEmpty(zipcode) ||
                TextUtils.isEmpty(mobile);
    }

    public enum Type {HOME,OFFICE}

    public void copyToAddress(android.location.Address address){

        this.country = address.getCountryName();
        this.line1 = address.getAddressLine(0);
       if(address.getAddressLine(0) != null) this.line2 = address.getAddressLine(1);
        this.street1 = address.getSubLocality();
        this.locality = address.getLocality();
        this.city = address.getSubAdminArea();
        this.state = address.getAdminArea();
        this.zipcode = address.getPostalCode();
    }

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", locality='" + locality + '\'' +
                ", flatno='" + flatno + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", landmark='" + landmark + '\'' +
                ", street1='" + street1 + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", type=" + type +
                '}';
    }

    public String getAddressString(){
        return ""+locality+", "+flatno+", "+city+", "+state+"\nMob: "+mobile+"\n"+type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.country);
        dest.writeString(this.lastName);
        dest.writeString(this.firstName);
        dest.writeString(this.line1);
        dest.writeString(this.line2);
        dest.writeString(this.city);
        dest.writeString(this.locality);
        dest.writeString(this.flatno);
        dest.writeString(this.state);
        dest.writeString(this.zipcode);
        dest.writeString(this.landmark);
        dest.writeString(this.street1);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected Address(Parcel in) {
        this.id = in.readString();
        this.country = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.line1 = in.readString();
        this.line2 = in.readString();
        this.city = in.readString();
        this.locality = in.readString();
        this.flatno = in.readString();
        this.state = in.readString();
        this.zipcode = in.readString();
        this.landmark = in.readString();
        this.street1 = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
