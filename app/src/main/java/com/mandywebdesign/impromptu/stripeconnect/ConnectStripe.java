package com.mandywebdesign.impromptu.stripeconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

public class ConnectStripe extends AppCompatActivity {

    Intent intent;
    String loginUserId;
    WebView webView;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_stripe);
        webView = findViewById(R.id.webView);
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        intent = getIntent();
        loginUserId = intent.getStringExtra("loginUserId");
        String urlWithId = "https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_GvO1s7dJvTeIkAjkndR3q3cve13FJPuC&scope=read_write&state="+loginUserId;

        //https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_GvO1s7dJvTeIkAjkndR3q3cve13FJPuC&scope=read_write&state=159
        //webview.setListener(Main2Activity.this,this);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                /*Intent intent = new Intent(ConnectStripe.this, BookEventActivity.class);
                intent.putExtra("stripeStatus","1");
                intent.putExtra("isFrom","stripepage");
                startActivity(intent);*/
//                finish();
            }
        });
        Log.d("+++++++++","++ urlWithId ++"+urlWithId);
        webView.loadUrl(urlWithId);
    }
}
