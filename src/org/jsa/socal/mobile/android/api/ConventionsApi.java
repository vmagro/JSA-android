package org.jsa.socal.mobile.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConventionsApi {

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"MM/dd/yyyy");

	public static ArrayList<Convention> getConventions() throws MalformedURLException, IOException, JSONException {
		ArrayList<Convention> arr = new ArrayList<Convention>();

		HttpURLConnection conn = null;

		conn = (HttpURLConnection) new URL(Api.CONVENTIONS).openConnection();

		JSONArray jsonArr = new JSONArray(new BufferedReader(
				new InputStreamReader(conn.getInputStream())).readLine());
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject json = jsonArr.getJSONObject(i);
			arr.add(new Convention(json, formatter));
		}
		return arr;
	}

}
