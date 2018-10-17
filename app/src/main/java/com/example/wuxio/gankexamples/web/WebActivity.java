package com.example.wuxio.gankexamples.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.wuxio.gankexamples.R;

/**
 * @author wuxio
 */
public class WebActivity extends AppCompatActivity {

      private static final String TAG = "WebActivity";

      protected TextView    mTitle;
      protected Toolbar     mToolbar;
      protected ProgressBar mProgressBar;
      protected WebView     mWebView;

      private static final String KEY_URL   = "Url";
      private static final String KEY_TITLE = "title";
      private              String mUrl;

      public static void start ( Context context, String url, String title ) {

            Intent starter = new Intent( context, WebActivity.class );
            starter.putExtra( KEY_URL, url );
            starter.putExtra( KEY_TITLE, title );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            super.setContentView( R.layout.activity_web );
            initView();

            mUrl = getIntent().getStringExtra( KEY_URL );
            mWebView.loadUrl( mUrl );

            String title = getIntent().getStringExtra( KEY_TITLE );
            mTitle.setText( title );
      }

      private void initView ( ) {

            mTitle = findViewById( R.id.title );
            mToolbar = findViewById( R.id.toolbar );
            mProgressBar = findViewById( R.id.loadingView );
            mWebView = findViewById( R.id.webView );

            mToolbar.setNavigationOnClickListener(
                v -> {
                      finish();
                }
            );
            mToolbar.inflateMenu( R.menu.activity_web_menu );
            mToolbar.setOnMenuItemClickListener( item -> {

                  Log.e( TAG, "onMenuItemClick : " + item.getTitle() );

                  Intent intent = new Intent();
                  intent.setAction( Intent.ACTION_SEND );
                  intent.setType( "text/plain" );
                  intent.putExtra( Intent.EXTRA_SUBJECT, "分享" );
                  intent.putExtra( Intent.EXTRA_TEXT, ":" + mUrl );
                  intent = Intent.createChooser( intent, "分享" );
                  startActivity( intent );

                  return true;
            } );

            initWebView();
      }

      public void initWebView ( ) {

            WebSettings settings = mWebView.getSettings();
            settings.setLoadWithOverviewMode( true );
            settings.setJavaScriptEnabled( true );
            settings.setAppCacheEnabled( true );
            settings.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
            settings.setSupportZoom( true );

            mWebView.setWebChromeClient( new MyWebChrome() );
            mWebView.setWebViewClient( new MyWebClient() );
      }

      @Override
      public void onBackPressed ( ) {

            if( mWebView.canGoBack() ) {
                  mWebView.goBack();
            } else {
                  finish();
            }
      }

      private class MyWebChrome extends WebChromeClient {

            @Override
            public void onProgressChanged ( WebView view, int newProgress ) {

                  mProgressBar.setVisibility( View.VISIBLE );
                  mProgressBar.setProgress( newProgress );
            }
      }

      private class MyWebClient extends WebViewClient {

            @Override
            public void onPageFinished ( WebView view, String url ) {

                  mProgressBar.setVisibility( View.GONE );
            }
      }
}
