package com.example.msq.oopsproject;

public class homePagePost {
    public String bType;
    public long contact;
    public int quantity;
    public String location;
    //public String Name;

    public homePagePost()
    {

    }
    public homePagePost(String bType, long contact, String location, int quantity) {
        this.bType = bType;
        this.contact = contact;
        this.quantity = quantity;
        this.location = location;
      //  Name = name;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }*/
}
