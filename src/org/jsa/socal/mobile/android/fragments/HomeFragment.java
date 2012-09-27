package org.jsa.socal.mobile.android.fragments;

import org.socal.jsa.mobile.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class HomeFragment extends SherlockFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setTitle("SoCal JSA");
		View v = inflater.inflate(R.layout.home, null);
		return v;
	}
}
