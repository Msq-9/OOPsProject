package com.example.msq.oopsproject;

public class reqinfo {
    public String location;
    public int quantity;
    public String bType;
    public long contact;

    reqinfo()
    {

    }
    reqinfo( String l,int a, String b, long c)
    {
        location = l;
        quantity = a;
        bType = b;
        contact = c;
    }
}
