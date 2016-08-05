package com.xcyo.live.activity.web_base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by TDJ on 2016/6/27.
 */
public class ChromeWebActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView mWeb = new WebView(this);
        setContentView(mWeb);
        configChrome(mWeb);
    }

    private void configChrome(WebView mWeb){
        mWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        mWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                view.loadUrl(url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setBlockNetworkImage(true);
        mWeb.addJavascriptInterface(new WebTermination(), "termination");
    }


    public static final class WebTermination {

        @JavascriptInterface
        private void onTermination(String result){

        }
    }
}
