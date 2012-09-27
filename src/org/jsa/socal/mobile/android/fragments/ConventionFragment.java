package org.jsa.socal.mobile.android.fragments;

import java.util.ArrayList;

import org.jsa.socal.mobile.android.api.Convention;
import org.jsa.socal.mobile.android.api.AgendaTopic;
import org.socal.jsa.mobile.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ConventionFragment extends SherlockFragment implements
		OnItemClickListener {

	private AgendaAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		Convention c = (Convention) getArguments()
				.getSerializable("convention");

		getSherlockActivity().getSupportActionBar().setTitle(c.getName());

		View v = inflater.inflate(R.layout.convention, null);
		((TextView) v.findViewById(R.id.title)).setText(c.getName());
		((TextView) v.findViewById(R.id.date)).setText(c.getDateString());
		((TextView) v.findViewById(R.id.location)).setText(c.getLocation());

		if (c.getAgenda().size() == 0) {
			((LinearLayout) v).removeView(v.findViewById(R.id.agendaText));
		} else
			((TextView) v.findViewById(R.id.agendaText)).setText(Html
					.fromHtml(getString(R.string.agenda_u)));

		ListView lv = (ListView) v.findViewById(R.id.agenda);
		adapter = new AgendaAdapter(getActivity().getLayoutInflater(), c);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		return v;
	}

	private class AgendaAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ArrayList<AgendaTopic> agenda;

		private AgendaAdapter(LayoutInflater inflater, Convention c) {
			this.inflater = inflater;
			this.agenda = c.getAgenda();
		}

		@Override
		public int getCount() {
			return agenda.size();
		}

		@Override
		public AgendaTopic getItem(int position) {
			return agenda.get(position);
		}

		@Override
		public long getItemId(int position) {
			return agenda.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater.inflate(R.layout.agenda_list_item, null);
			((TextView) v.findViewById(R.id.block)).setText(Html
					.fromHtml(agenda.get(position).getBlock()));
			((TextView) v.findViewById(R.id.text)).setText(Html.fromHtml(agenda
					.get(position).getText()));
			((TextView) v.findViewById(R.id.time)).setText(Html.fromHtml(agenda
					.get(position).getTime()));
			((TextView) v.findViewById(R.id.location)).setText(Html.fromHtml(agenda
					.get(position).getLocation()));
			return v;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		AgendaTopic t = adapter.getItem(pos);
		Bundle args = new Bundle();
		args.putSerializable("topic", t);
		Fragment frag = Fragment.instantiate(getActivity(),
				AgendaTopicFragment.class.getName(), args);
		getSherlockActivity().getSupportFragmentManager().beginTransaction()
				.detach(this).add(android.R.id.content, frag)
				.addToBackStack("debate").commit();
	}

}
