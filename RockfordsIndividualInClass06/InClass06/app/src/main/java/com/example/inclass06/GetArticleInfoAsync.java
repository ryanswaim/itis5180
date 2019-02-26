package com.example.inclass06;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

//In-Class Assignment 06
//Rockford Stoller

public class GetArticleInfoAsync extends AsyncTask<String, Integer, ArrayList<Article>> {

    ArticleInfoData articleInfoData;
    ArrayList<Article> articles = new ArrayList<>();

    public GetArticleInfoAsync(ArticleInfoData articleInfoData) {
        this.articleInfoData = articleInfoData;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        articleInfoData.updateProgress(values[0]);
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        HttpURLConnection connection = null;

        Log.d("demo","url"+strings[0]);

        try {
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                publishProgress(1);

                articles = new ArrayList<>();

                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                JSONObject root = new JSONObject(json);
                JSONArray articlesArray = root.getJSONArray("articles");

                for(int i = 0; i < articlesArray.length(); i++) {
                    JSONObject articleJson = articlesArray.getJSONObject(i);
                    Article article = new Article();


                    article.title = articleJson.getString("title");
                    article.date = articleJson.getString("publishedAt");
                    article.description = articleJson.getString("description");
                    article.imageURL = articleJson.getString("urlToImage");
                    Log.d("demo","article"+article.toString());

                    articles.add(article);
                }
            }

        } catch (SocketTimeoutException e) {
            publishProgress(-1);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            //Log.d("demo", "ERORR1: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return articles;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {

        Log.d("demo", "onPostExecute: " + articles.size());

        articleInfoData.handleArticleData(articles);
    }

    public static interface ArticleInfoData {
        public void handleArticleData(ArrayList<Article> articles);
        public void updateProgress(int progress);
    }
}
