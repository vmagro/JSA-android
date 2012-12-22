package org.jsa.socal.mobile.android.fragments;

import java.util.ArrayList;

import org.jsa.socal.mobile.android.api.AgendaTopic;
import org.jsa.socal.mobile.android.api.Convention;
import org.socal.jsa.mobile.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.mobsandgeeks.adapters.Sectionizer;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;

public class ConventionFragment extends SherlockListFragment{

	private SimpleSectionAdapter<AgendaTopic> adapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Convention c = (Convention) getArguments()
				.getSerializable("convention");
		
		Log.i("convention", c.getName());

		getSherlockActivity().getSupportActionBar().setTitle(c.getName());

		adapter = new SimpleSectionAdapter<AgendaTopic>(getActivity(), 
				new AgendaAdapter(getActivity().getLayoutInflater(), c), R.layout.section_header, R.id.title, 
		        new AgendaSectionizer());
		
		this.setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		AgendaTopic t = (AgendaTopic) adapter.getItem(position);
		Bundle args = new Bundle();
		args.putSerializable("topic", t);
		Fragment frag = Fragment.instantiate(getActivity(),
				AgendaTopicFragment.class.getName(), args);
		getSherlockActivity().getSupportFragmentManager().beginTransaction()
				.detach(this).add(android.R.id.content, frag)
				.addToBackStack("debate").commit();
	}

	private class AgendaAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ArrayList<AgendaTopic> agenda;

		private AgendaAdapter(LayoutInflater inflater, Convention c){
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
			
			if(agenda.size() <= position)
				return v;
			
			((TextView) v.findViewById(R.id.text)).setText(Html.fromHtml(agenda
					.get(position).getText()));
			
			if(agenda.get(position).getText().equals(""))
				((LinearLayout) v).removeView(v.findViewById(R.id.text)); //remove the view for description if there is no description
			
			((TextView) v.findViewById(R.id.location)).setText(Html.fromHtml(agenda
					.get(position).getLocation()));
			return v;
		}

	}
	
	private static class AgendaSectionizer implements Sectionizer<AgendaTopic>{

		@Override
		public String getSectionTitleForItem(AgendaTopic topic) {
			return topic.getBlock();
		}
		
		@Override
		public String getTime(AgendaTopic topic){
			return topic.getTime();
		}
		
	}

}
