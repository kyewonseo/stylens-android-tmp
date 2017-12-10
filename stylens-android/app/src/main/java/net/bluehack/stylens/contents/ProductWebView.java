package net.bluehack.stylens.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import net.bluehack.stylens_android.R;

import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ProductWebView extends AppCompatActivity {

    private static final String TAG = makeLogTag(ProductWebView.class);
    private Context context;
    private WebView webView;
    private LinearLayout progress_ll;
    private ImageView progress_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_product);

        setLayout();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        Intent intent = getIntent();
        String productUrl = intent.getExtras().getString("productUrl");
        webView.loadUrl(productUrl);
    }


    private void setLayout(){

        progress_ll = (LinearLayout) findViewById(R.id.progress_ll);
        progress_iv = (ImageView) findViewById(R.id.progress_iv);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progress_iv);
        Glide.with(this).load(R.raw.img_loading_gif).into(imageViewTarget);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                progress_ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progress_ll.setVisibility(View.GONE);
            }
        });
    }
}
