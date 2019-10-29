package com.example.android.news;

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

public final class NewsNetworkingActivity {
    public static final String LOGTAG = NewsNetworkingActivity.class.getName();

    private NewsNetworkingActivity() {
    }

    private static URL createURl(String requestedURl) {
        URL url = null;
        try {
            url = new URL(requestedURl);
        } catch (MalformedURLException e) {
            Log.e(LOGTAG, "Error creating the url" + e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String chars = bufferedReader.readLine();
            while (chars != null) {
                output.append(chars);
                chars = bufferedReader.readLine();
            }

        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setReadTimeout(1500);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOGTAG, "Error Making the connection" + e);
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return jsonResponse;
    }

    private static ArrayList<NewsInfoActivity> extractJsonFeatures(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse))
            return null;
        ArrayList<NewsInfoActivity> news = new ArrayList<>();

        try {
            JSONObject rootJson = new JSONObject(jsonResponse);
            JSONObject response = rootJson.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentobject = results.getJSONObject(i);
                String sectionName = currentobject.getString("sectionName");
                String title = currentobject.getString("webTitle");
                String date = currentobject.getString("webPublicationDate");
                String url=currentobject.getString("webUrl");
                news.add(new NewsInfoActivity(sectionName, title, date,url));
            }

        } catch (JSONException e) {
            Log.e(LOGTAG, "error parsing json" + e);
        }
        return news;
    }

    public static List<NewsInfoActivity> fetchData(String requestUrl) {
        String jsonResponse = null;
        URL url = createURl(requestUrl);
        try {

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOGTAG, "Error fetching Data" + e);
        }
        ArrayList<NewsInfoActivity> news = extractJsonFeatures(jsonResponse);
        return news;
    }
}
