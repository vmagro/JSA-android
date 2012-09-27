package org.socal.jsa.mobile.android;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsa.socal.mobile.android.fragments.ConventionsFragment;
import org.jsa.socal.mobile.android.fragments.HomeFragment;
import org.jsa.socal.mobile.android.fragments.LinksFragment;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MainActivity extends SherlockFragmentActivity {

	private static final String FACEBOOK_APP_ID = "434281589955303";

	private Facebook facebook = new Facebook(FACEBOOK_APP_ID);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab homeTab = ab
				.newTab()
				.setText("Home")
				.setTabListener(
						new TabListener<HomeFragment>(this, "home",
								HomeFragment.class));
		ab.addTab(homeTab);

		Tab conferencesTab = ab
				.newTab()
				.setText("Conferences")
				.setTabListener(
						new TabListener<ConventionsFragment>(this,
								"conventions", ConventionsFragment.class));
		ab.addTab(conferencesTab);

		Tab linksTab = ab
				.newTab()
				.setText("Links")
				.setTabListener(
						new TabListener<LinksFragment>(this, "home",
								LinksFragment.class));
		ab.addTab(linksTab);

	}
	
	public void onResume(){
		super.onResume();
	}

	public void authFacebook() {
		/*
		 * Get existing access_token if any
		 */
		final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
		
		if(mPrefs.contains("name")) //do nothing if we have already obtained the user's information
			return;
		
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		if (!facebook.isSessionValid()) {
			Log.e("fb", "session not valid");
			facebook.authorize(this, new String[] {}, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					
					new AsyncTask<Void, Void, String>(){

						@Override
						protected String doInBackground(Void... params) {
							try {
								return new JSONObject(facebook.request("me")).getString("name");
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							return null;
						}
						
						@Override
						public void onPostExecute(String result){
							mPrefs.edit()
								.putString("name", result)
								.commit();
						}
						
					}.execute();
					
					
					editor.commit();
				}

				@Override
				public void onFacebookError(FacebookError error) {
				}

				@Override
				public void onError(DialogError e) {
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}
	
	public String getFacebookName(){
		return this.getPreferences(MODE_PRIVATE).getString("name", "null");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(getSupportFragmentManager().findFragmentById(android.R.id.content) != null){
				ft.detach(getSupportFragmentManager().findFragmentById(android.R.id.content));
			}
			
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.replace(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

}
