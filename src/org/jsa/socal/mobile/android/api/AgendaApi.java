package org.jsa.socal.mobile.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsa.socal.mobile.android.api.AgendaTopic.Comment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AgendaApi {

	public static ArrayList<Comment> getComments(int id)
			throws MalformedURLException, IOException, JSONException {
		ArrayList<Comment> arr = new ArrayList<Comment>();

		HttpURLConnection conn = null;

		conn = (HttpURLConnection) new URL(Api.AGENDA_TOPICS + "?id=" + id)
				.openConnection();

		JSONArray jsonArr = new JSONArray(new BufferedReader(
				new InputStreamReader(conn.getInputStream())).readLine());
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject json = jsonArr.getJSONObject(i);
			arr.add(new Comment(json));
		}

		return arr;
	}

	public static void sendCommentAsync(final String name, final String text,
			final int topicId) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			public Void doInBackground(Void... params) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Api.AGENDA_TOPICS);

				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("id", String
							.valueOf(topicId)));
					nameValuePairs.add(new BasicNameValuePair("name", name));
					nameValuePairs.add(new BasicNameValuePair("text", text));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

		}.execute();
	}

	public static void voteForSpeakerAsync(final Context ctx, final String userId,
			final String speaker, final int agendaId){
		new AsyncTask<Void, Void, String>() {

			@Override
			public String doInBackground(Void... params) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Api.VOTE);

				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("user", userId));
					nameValuePairs.add(new BasicNameValuePair("speaker",
							speaker));
					nameValuePairs.add(new BasicNameValuePair("id", String
							.valueOf(agendaId)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					httpclient.execute(httppost);
					
					return "success";
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			public void onPostExecute(String result){
				if(result == null || !result.equals("success"))
					Toast.makeText(ctx, "Error submitting vote", Toast.LENGTH_LONG).show();
			}

		}.execute();
	}

}
