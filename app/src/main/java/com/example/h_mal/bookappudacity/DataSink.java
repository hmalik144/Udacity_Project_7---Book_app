package com.example.h_mal.bookappudacity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.h_mal.bookappudacity.BooksAdapter.LOG_TAG;

/**
 * Created by h_mal on 09/03/2017.
 */

public class DataSink {

    private DataSink() {}

    private static URL createURL(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error when creating URL", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

        if (urlConnection.getResponseCode() == 200){
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }else{
            Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        }
    }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Books> extractFeatureFromJson(String booksJSON) {
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        List<Books> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);

                JSONObject properties = currentBook.getJSONObject("volumeInfo");

                if(properties.has("authors")) {
                    String author = properties.getString("authors");

                    String title = properties.getString("title");

                    String url = properties.getString("infoLink");

                    Books book = new Books(title, author, url);
                    books.add(book);
                }

            }

        } catch (JSONException e) {
            Log.e("DataSink", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    public static List<Books> fetchBookData(String requestUrl) {

        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Books> books = extractFeatureFromJson(jsonResponse);

        return books;
    }
}

