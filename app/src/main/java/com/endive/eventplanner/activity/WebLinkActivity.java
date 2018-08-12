package com.endive.eventplanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.endive.eventplanner.R;

/**
 * Created by upasna.mishra on 11/10/2017.
 */

public class WebLinkActivity extends BaseActivity {

    private WebView webview;
    private Intent intent;
    private String web_link, header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_web_url);
        intent = getIntent();
        header = intent.getStringExtra("header");
        setHeader(header);
        initialize();
    }

    private void initialize() {
        web_link = intent.getStringExtra("web_url");

        webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(web_link);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });
    }
}
