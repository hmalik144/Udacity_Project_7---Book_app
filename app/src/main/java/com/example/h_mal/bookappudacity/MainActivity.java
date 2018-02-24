package com.example.h_mal.bookappudacity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    private static final int BOOK_LOADER_ID = 1;

    private static final String LOG_TAG = MainActivity.class.getName();

    private static String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=40";

    private BooksAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "initiate loader");

        ListView booksListView = (ListView) findViewById(R.id.book_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new BooksAdapter(this, new ArrayList<Books>());
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Books currentBook = mAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getURLaddress());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }else{
            mEmptyStateTextView.setText("no internet connection");
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }

        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_BOOKS_URL);
    }

    public void ButtonClick(View view){
        EditText enteredText = (EditText) findViewById(R.id.edit_text);
        String dataStructure = enteredText.getText().toString();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https://www.googleapis.com/books/v1/volumes?q=").appendPath(dataStructure).fragment("&maxResults=40");
        String newUrl = builder.build().toString();
        GOOGLE_BOOKS_URL = newUrl;
        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_BOOKS_URL);
    }

    public void ButtonClear(View view){

        EditText enteredText = (EditText) findViewById(R.id.edit_text);
        enteredText.setText("");
        GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=40";
        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_BOOKS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> books) {
        mEmptyStateTextView.setText("no books found");
        mAdapter.clear();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "Loader Created");
        return new BooksLoader(this, GOOGLE_BOOKS_URL);
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Books>> {

        @Override
        protected List<Books> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Books> result = DataSink.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Books> data) {

            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

}
