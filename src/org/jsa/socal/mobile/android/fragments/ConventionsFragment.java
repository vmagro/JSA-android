package org.jsa.socal.mobile.android.fragments;

import java.util.ArrayList;

import org.jsa.socal.mobile.android.api.Convention;
import org.jsa.socal.mobile.android.api.ConventionsApi;
import org.socal.jsa.mobile.android.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class ConventionsFragment extends SherlockListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle("Conferences");
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,
				new String[] { "Loading..." }));
		new Adapter(getActivity().getLayoutInflater());
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		if (lv.getItemAtPosition(position) instanceof Convention) {
			Convention c = (Convention) lv.getItemAtPosition(position);
			Bundle b = new Bundle();
			b.putSerializable("convention", c);
			Fragment f = SherlockFragment.instantiate(getActivity(),
					ConventionFragment.class.getName(), b);
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().detach(this)
					.add(android.R.id.content, f).addToBackStack("convention")
					.commit();
		}
	}

	private class Adapter extends BaseAdapter {

		private ArrayList<Convention> conventions = new ArrayList<Convention>();

		private LayoutInflater inflater;

		public Adapter(LayoutInflater inflater) {
			this.inflater = inflater;

			new AsyncTask<Void, Void, ArrayList<Convention>>() {

				@Override
				protected ArrayList<Convention> doInBackground(Void... params) {
					try {
						return ConventionsApi.getConventions();
					} catch (Exception e) {
						Log.e("conventions", "no network access");
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<Convention> result) {
					if (result != null) {
						Adapter.this.conventions = result;
						ConventionsFragment.this.setListAdapter(Adapter.this);
					} else {
						ConventionsFragment.this
								.setListAdapter(new ArrayAdapter<String>(
										getActivity(),
										android.R.layout.simple_list_item_1,
										new String[] { "Error..." }));
					}
				}

			}.execute();
		}

		@Override
		public int getCount() {
			return conventions.size();
		}

		@Override
		public Object getItem(int position) {
			return conventions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return conventions.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Convention c = conventions.get(position);

			View v = inflater.inflate(R.layout.convention_list_item, null);
			((TextView) v.findViewById(R.id.name)).setText(c.getName());
			((TextView) v.findViewById(R.id.location)).setText(c.getLocation());
			((TextView) v.findViewById(R.id.date)).setText(c.getDateString());
			return v;
		}

	}

}
