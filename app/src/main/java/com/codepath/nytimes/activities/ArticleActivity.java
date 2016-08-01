package com.codepath.nytimes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

}
