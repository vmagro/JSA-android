package org.jsa.socal.mobile.android.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ConventionsApi {

	private static final String FILE_NAME = "conventions.json";

	public static ArrayList<Convention> getConventions(Context ctx) {
		ArrayList<Convention> arr = new ArrayList<Convention>();

		boolean usingCache = false; // remember if we are using cache so we
									// don't rewrite it

		InputStream inputStream = null;
		try {
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) new URL(Api.CONVENTIONS)
					.openConnection();
			inputStream = conn.getInputStream();
		} catch (Exception ex) {
			Log.e("conventions", "error retrieving conventions");
			if (ctx.getFileStreamPath(FILE_NAME).exists()) {
				Log.i("conventions", "using cache instead");
				try {
					inputStream = ctx.openFileInput(FILE_NAME);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				usingCache = true;
			}
		}

		try {
			JSONArray jsonArr = new JSONArray(new BufferedReader(
					new InputStreamReader(inputStream)).readLine());

			if (!usingCache) {
				// cache the data for future retrieval
				ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).write(
						jsonArr.toString().getBytes());
			}

			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject json = jsonArr.getJSONObject(i);
				arr.add(new Convention(json));
			}
			return arr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
