package com.example.android.dermadetect;

import android.text.Html;

public class Drinfo  {

    private String mdrname ;
    private String mdrdegree;
    private int mimageid;
    private String murl;
    private String mnumber;

    public Drinfo(String name , String degree , int id, String url, String number) {
        mdrname = name;
        mdrdegree = degree;
        mimageid = id;
        murl = url;
        mnumber= number;
    }

    public String getname()
    {
        return mdrname;
    }

    public String getdegree()
    {
        return mdrdegree;
    }

    public int getimage()
    {
        return mimageid;
    }

    public String geturl()
    {
        return murl;
    }

    public String getnumber()
    {
        return mnumber;
    }
}
