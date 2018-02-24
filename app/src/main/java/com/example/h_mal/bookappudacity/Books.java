package com.example.h_mal.bookappudacity;

/**
 * Created by h_mal on 08/03/2017.
 */

public class Books {

    private String mTitle;

    private String mAuthor;

    private String mURLaddress;

    public Books (String title, String author, String URLaddress){

        mTitle = title;
        mAuthor = author;
        mURLaddress = URLaddress;
    }

    public String getTitle() { return mTitle;}

    public String getAuthor() {return mAuthor;}

    public String getURLaddress() {return mURLaddress;}
}
