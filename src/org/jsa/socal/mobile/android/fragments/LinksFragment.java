package org.jsa.socal.mobile.android.fragments;

import org.socal.jsa.mobile.android.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class LinksFragment extends SherlockListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setListAdapter(new Adapter(getActivity().getLayoutInflater()));
		getSherlockActivity().getSupportActionBar().setTitle("Links");
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		if (!((Item) lv.getItemAtPosition(position)).isSeparator()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(((Item) lv.getItemAtPosition(position)).getUri());
			startActivity(intent);
		}
	}

	public static class Adapter extends BaseAdapter {

		private Item[] items = { new Item("JSA on the Web"),
				new Item("Junior State of America", "jsa.org"),
				new Item("SoCal JSA", "socal.jsa.org"),
				new Item("Pro Voice", "socal.jsa.org/pro-voice"),
				new Item("Regions"),
				new Item("Angeles Region", "socal.jsa.org/ar"),
				new Item("Channel Islands Region", "socal.jsa.org/cir"),
				new Item("Southern Empire Region", "socal.jsa.org/ser") };

		private LayoutInflater inflater;

		public Adapter(LayoutInflater inflater) {
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (items[pos].isSeparator()) {
				View v = inflater.inflate(R.layout.list_separator, null);
				((TextView) v.findViewById(R.id.title)).setText(items[pos]
						.getTitle());
				v.setClickable(false);
				return v;
			} else {
				View v = inflater.inflate(R.layout.links_list_item, null);
				((TextView) v.findViewById(R.id.title)).setText(items[pos]
						.getTitle());
				((TextView) v.findViewById(R.id.url)).setText(items[pos]
						.getUrl());
				return v;
			}
		}

	}

	public static class Item {

		private String title, url;
		private boolean separator;

		public Item(String title, String url) {
			this.title = title;
			this.separator = false;
			this.url = url;
		}

		public Item(String title) {
			this.title = title;
			this.separator = true;
		}

		public String getTitle() {
			return title;
		}

		public boolean isSeparator() {
			return separator;
		}

		public Uri getUri() {
			return Uri.parse("http://" + url);
		}

		public String getUrl() {
			return url;
		}
	}

}
