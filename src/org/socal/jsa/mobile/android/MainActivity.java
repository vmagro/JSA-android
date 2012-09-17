package org.socal.jsa.mobile.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

	// private static final String BASE = "http://192.168.0.19:8888/";
	private static final String BASE = "http://socal-jsa.appspot.com/";

	private WebView wv;
	private TextView loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		wv = (WebView) findViewById(R.id.webview);
		
		loading = (TextView) findViewById(R.id.loading);
		wv.setVisibility(View.INVISIBLE); // hide until loading

		wv.loadUrl(BASE);
		WebSettings settings = wv.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUserAgentString("socal-jsa-web-app");
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mailto:")) {
					MailTo mt = MailTo.parse(url);
					Intent i = newEmailIntent(MainActivity.this, mt.getTo(),
							mt.getSubject(), mt.getBody(), mt.getCc());
					startActivity(i);
					view.reload();
					return true;
				}

				else {
					view.loadUrl(url);
				}
				return true;
			}
			

			public void onPageStarted(WebView view, String url) {
				wv.setVisibility(View.INVISIBLE);
				loading.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				onPageStarted(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d("jsa", "finished page");
				wv.setVisibility(View.VISIBLE);
				loading.setVisibility(View.INVISIBLE);
			}
		});
		wv.setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.e("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}
		});
		wv.addJavascriptInterface(new JavaScriptInterface(), "Android");

		ActionBar ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabListener listener = new TabListener();

		Tab homeTab = ab.newTab().setText("Home").setTabListener(listener)
				.setTag("");
		ab.addTab(homeTab);

		Tab conferencesTab = ab.newTab().setText("Conferences")
				.setTabListener(listener).setTag("conventions");
		ab.addTab(conferencesTab);

		Tab linksTab = ab.newTab().setText("Links").setTabListener(listener)
				.setTag("links");
		ab.addTab(linksTab);
	}

	@Override
	public void onBackPressed() {
		wv.goBack();
	}

	public static Intent newEmailIntent(Context context, String address,
			String subject, String body, String cc) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_CC, cc);
		intent.setType("message/rfc822");
		return intent;
	}

	public class TabListener implements ActionBar.TabListener {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			String url = BASE + tab.getTag().toString();
			wv.loadUrl(url);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {

		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}

	}
}
