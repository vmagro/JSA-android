package org.jsa.socal.mobile.android.fragments;

import java.util.ArrayList;

import org.jsa.socal.mobile.android.api.AgendaApi;
import org.jsa.socal.mobile.android.api.AgendaTopic;
import org.jsa.socal.mobile.android.api.AgendaTopic.Comment;
import org.socal.jsa.mobile.android.MainActivity;
import org.socal.jsa.mobile.android.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class AgendaTopicFragment extends SherlockFragment implements
		OnClickListener {
	
	private int topicId;
	private ListView commentsLv;

	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		AgendaTopic t = (AgendaTopic) getArguments().getSerializable(
				"topic");
		topicId = t.getId();
		getSherlockActivity().getSupportActionBar().setTitle(t.getBlock());
		View v = inflater.inflate(R.layout.agenda, null);
		((TextView) v.findViewById(R.id.block)).setText(Html.fromHtml(t
				.getBlock()));
		((TextView) v.findViewById(R.id.text)).setText(Html.fromHtml(t
				.getText()+"<br>"+t.getLongText()));
		((TextView) v.findViewById(R.id.time)).setText(Html.fromHtml(t
				.getTime()));
		((TextView) v.findViewById(R.id.location)).setText(Html.fromHtml(t
				.getLocation()));

		commentsLv = (ListView) v
				.findViewById(R.id.comments);

		loadComments();
		
		v.findViewById(R.id.leaveAComment).setOnClickListener(this);
		v.findViewById(R.id.vote).setOnClickListener(this);

		return v;
	}

	private void loadComments() {
		new AsyncTask<Void, Void, ArrayList<Comment>>() {

			@Override
			protected ArrayList<Comment> doInBackground(Void... params) {
				try {
					return AgendaApi.getComments(topicId);
				} catch (Exception ex) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(ArrayList<Comment> result) {
				if (result != null && getActivity() != null)
					commentsLv.setAdapter(new CommentsAdapter(result,
							getActivity().getLayoutInflater()));
			}

		}.execute();
	}

	@Override
	public void onClick(View clicked) {
		if (clicked.getId() == R.id.leaveAComment) {
			((MainActivity) AgendaTopicFragment.this.getActivity()).authFacebook();
			final View v = getActivity().getLayoutInflater().inflate(
					R.layout.leave_a_comment, null);
			new AlertDialog.Builder(getActivity())
					.setView(v)
					.setTitle(R.string.leave_a_comment)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String text = ((EditText) v.findViewById(R.id.text)).getText().toString();
							String author = ((MainActivity) AgendaTopicFragment.this.getActivity()).getFacebookName();
							AgendaApi.sendCommentAsync(author, text, topicId);
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							loadComments();
						}
					})
					.show();
		}
		else if (clicked.getId() == R.id.vote){
			((MainActivity) AgendaTopicFragment.this.getActivity()).authFacebook();
			final View v = getActivity().getLayoutInflater().inflate(
					R.layout.vote, null);
			new AlertDialog.Builder(getActivity())
					.setView(v)
					.setTitle(R.string.vote)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}

							})
					.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String speaker = ((EditText) v.findViewById(R.id.best_speaker)).getText().toString();
							String user = ((MainActivity) AgendaTopicFragment.this.getActivity()).getFacebookName();
							AgendaApi.sendVoteAsync(user, speaker, topicId);
						}
					})
					.show();
		}
	}

	private static class CommentsAdapter extends BaseAdapter {

		private ArrayList<Comment> comments = new ArrayList<Comment>();
		private LayoutInflater inflater;

		public CommentsAdapter(ArrayList<Comment> comments,
				LayoutInflater inflater) {
			this.comments = comments;
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return comments.size();
		}

		@Override
		public Comment getItem(int position) {
			return comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return comments.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater.inflate(R.layout.comment_list_item, null);
			Comment c = getItem(position);
			((TextView) v.findViewById(R.id.name)).setText(Html.fromHtml(c
					.getAuthor()));
			((TextView) v.findViewById(R.id.time)).setText(Html.fromHtml(c
					.getTime()));
			((TextView) v.findViewById(R.id.text)).setText(Html.fromHtml(c
					.getText()));
			return v;
		}

	}
}
