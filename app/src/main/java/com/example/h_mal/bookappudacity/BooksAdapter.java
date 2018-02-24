package com.example.h_mal.bookappudacity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by h_mal on 08/03/2017.
 */

public class BooksAdapter extends ArrayAdapter<Books>{

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String LOCATION_SEPARATOR = ",";

    public BooksAdapter(Activity context, ArrayList<Books> books){
    super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Books currentBook = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());

        String authors = currentBook.getAuthor();
        authors = authors.replace("[","");
        authors = authors.replace("]","");

        String primaryAuthor;
        String secondaryAuthor;

        if (authors.contains(LOCATION_SEPARATOR)) {
            String[] parts = authors.split(LOCATION_SEPARATOR);
            primaryAuthor = parts[0] + "";
            secondaryAuthor = parts[1];
        } else {
            secondaryAuthor = null;
            primaryAuthor = authors;
        }

        TextView primaryAuthorTextView = (TextView) listItemView.findViewById(R.id.author);
        primaryAuthorTextView.setText(primaryAuthor);

        TextView secondaryAuthorTextView = (TextView) listItemView.findViewById(R.id.author_secondary);
        secondaryAuthorTextView.setText(secondaryAuthor);

        return listItemView;
    }
}
