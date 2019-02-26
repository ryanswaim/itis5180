package com.example.inclass06;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetArticleInfoAsync.ArticleInfoData {

    String[] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
    Integer currentArticle = 0;
    ArrayList<Article> articles = new ArrayList<>();
    ImageView articleImageView;
    ImageView prevButtonImageView;
    ImageView nextButtonImageView;
    TextView titleTextView;
    TextView dateTextView;
    TextView contentTextView;
    TextView articleNumberTextView;
    TextView noImageFoundTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.activity1_title_text);

        final TextView searchCategoryTextView = findViewById(R.id.category_choice_textView);
        titleTextView = findViewById(R.id.article_title_textView);
        dateTextView = findViewById(R.id.date_published_textView);
        contentTextView = findViewById(R.id.article_content_textView);
        articleImageView = findViewById(R.id.article_image_imageView);
        prevButtonImageView = findViewById(R.id.prev_imageView);
        nextButtonImageView = findViewById(R.id.next_imageView);
        articleNumberTextView = findViewById(R.id.article_number_textView);
        noImageFoundTextView = findViewById(R.id.no_image_textView);

        prevButtonImageView.setEnabled(false);
        nextButtonImageView.setEnabled(false);

        //go button
        findViewById(R.id.go_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected() && categories != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(R.string.choose_category_title)
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestParams url;
                                    searchCategoryTextView.setText(categories[which]);
                                    url = new RequestParams()
                                            .addParameter("country", "us")
                                            .addParameter("apiKey", "192a2dc8984443af9f310b12a7fb1e62")
                                            .addParameter("category", categories[which]);
                                    String urlStr = url.getEncodedUrl("https://newsapi.org/v2/top-headlines");
                                    new GetArticleInfoAsync(MainActivity.this).execute(urlStr);
                                }
                            });

                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //next button
        //region
        nextButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentArticle == articles.size() - 1) {
                    currentArticle = 0;
                } else {
                    currentArticle++;
                }

                setArticle(articles.get(currentArticle));
            }
        });
        //endregion

        //previous button
        //region
        prevButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentArticle == 0) {
                    currentArticle = articles.size() - 1;
                } else {
                    currentArticle--;
                }

                setArticle(articles.get(currentArticle));
            }
        });
        //endregion
    }

    //is connected
    //region
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
    //endregion

    //set article
    //region
    private void setArticle(Article article) {
        //debugging
        //region
        //Log.d("demo", "Title: " + article.title);
        //Log.d("demo", "Date: " + article.date);
        //Log.d("demo", "Content: " + article.description);
        //Log.d("demo", "imageURL: " + article.imageURL);
        //endregion
        titleTextView.setText(article.title);
        dateTextView.setText(article.date);
        contentTextView.setText(article.description);
        if(article.imageURL.equals("null")) {
            articleImageView.setImageBitmap(null);
            noImageFoundTextView.setVisibility(View.VISIBLE);
        } else {
            Picasso.get().load(article.imageURL).placeholder(R.drawable.loading).into(articleImageView);
            noImageFoundTextView.setVisibility(View.INVISIBLE);
        }

        articleNumberTextView.setText((currentArticle + 1) + " out of " + articles.size());
    }
    //endregion

    //GetArticleInfoAsync required functions
    //region
    @Override
    public void handleArticleData(ArrayList<Article> articles) {

        this.articles = new ArrayList<>();

        if(articles.size() > 0) {
            this.articles.addAll(articles);

            currentArticle = 0;
            setArticle(this.articles.get(currentArticle));

            if(articles.size() > 1) {
                nextButtonImageView.setEnabled(true);
                prevButtonImageView.setEnabled(true);
            } else {
                nextButtonImageView.setEnabled(false);
                prevButtonImageView.setEnabled(false);
            }
        } else {
            Toast.makeText(MainActivity.this, "No News Found", Toast.LENGTH_SHORT).show();
            articleImageView.setImageBitmap(null);
            nextButtonImageView.setEnabled(false);
            prevButtonImageView.setEnabled(false);
        }
    }

    @Override
    public void updateProgress(int progress) {

    }
    //endregion
}
