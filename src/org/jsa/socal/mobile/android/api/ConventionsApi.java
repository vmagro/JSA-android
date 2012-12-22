package org.jsa.socal.mobile.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ConventionsApi {

	private static final String FILE_NAME = "conventions.json";

	public static ArrayList<Convention> getCachedConventions(Context ctx)
			throws IOException {
		ArrayList<Convention> arr = new ArrayList<Convention>();

		InputStream inputStream = ctx.openFileInput(FILE_NAME);
		try {
			JSONArray jsonArr = new JSONArray(new BufferedReader(
					new InputStreamReader(inputStream)).readLine());

			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject json = jsonArr.getJSONObject(i);
				arr.add(new Convention(json));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return arr;
	}

	public static ArrayList<Convention> getOnlineConventions(Context ctx)
			throws IOException {
		ArrayList<Convention> arr = new ArrayList<Convention>();

		HttpURLConnection conn = null;
		conn = (HttpURLConnection) new URL(Api.CONVENTIONS).openConnection();
		InputStream inputStream = conn.getInputStream();
		try {
			JSONArray jsonArr = new JSONArray(new BufferedReader(
					new InputStreamReader(inputStream)).readLine());

			// cache the data for future retrieval
			ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).write(
					jsonArr.toString().getBytes());

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
