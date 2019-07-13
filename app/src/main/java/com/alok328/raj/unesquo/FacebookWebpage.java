package com.alok328.raj.unesquo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dmax.dialog.SpotsDialog;

public class FacebookWebpage extends AppCompatActivity {

    WebView webView;
    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_webpage);

        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();
        dialog.show();

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });


        try{
            webView.loadUrl("https://m.facebook.com/Unesquo/");
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
