package com.ermile.khadijehapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ermile.khadijehapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.HashMap;
import java.util.Map;

public class Web_View extends AppCompatActivity {
    Map<String, String> sernd_headers = new HashMap<>();
    private String URL = null;

    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView_object;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        URL = getIntent().getStringExtra("url");
        sernd_headers.put("x-app-request", "android");

        swipeRefreshLayout = findViewById(R.id.swipRefresh_WebView);
        webView_object = findViewById(R.id.webView_WebView);




        if (URL != null){
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @SuppressLint("NewApi")
                @Override
                public void onRefresh() {
                    webView_object.loadUrl(webView_object.getUrl(), sernd_headers);
                }
            });
            webView_object.loadUrl(URL, sernd_headers);
            webView_object.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                    finish();
                    startActivity(new Intent(getApplicationContext(),errorNet.class));
                }
                // in refresh send header
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    HashMap<String, String> headerMap = new HashMap<>();
                    headerMap.put("x-app-request", "android");
                    view.loadUrl(url, headerMap);

                    if (url.startsWith("https://khadije.com/pay/") ||
                            url.startsWith("https://khadije.com/ar/pay/") ||
                            url.startsWith("https://khadije.com/en/pay/"))
                    {
                        Intent browser = new Intent(Intent.ACTION_VIEW);
                        browser.setData(Uri.parse(url));
                        startActivity(browser);
                        finish();
                        return true;
                    }


                    return false;
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    swipeRefreshLayout.setRefreshing(false);
                }});
        }
        else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }


    }
}