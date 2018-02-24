package com.example.h_mal.bookappudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by h_mal on 10/03/2017.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    public String mURL;

    public BooksLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {

        if (mURL == null) {
            return null;
        }

        List<Books> result = DataSink.fetchBookData(mURL);
        return result;
    }
}
