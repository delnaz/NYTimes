package com.codepath.nytimes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.wvArticle) WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("articleDetail"));
        ButterKnife.bind(this);
        webView.setWebViewClient(new WebViewClient());
        if(article.getWebUrl() != null){
            webView.loadUrl(article.getWebUrl());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                // get reference to WebView
                WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
                // pass in the URL currently being used by the WebView
                shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
                startActivity(shareIntent);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }


}
