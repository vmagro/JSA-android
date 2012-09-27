package org.jsa.socal.mobile.android.api;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Convention implements Serializable{
	
	private String name, dateString, location;
	private int id;
	private ArrayList<AgendaTopic> agenda = new ArrayList<AgendaTopic>();
	
	protected Convention(int id, String name, String location, long date, JSONArray agenda, SimpleDateFormat fmt){
		this.id = id;
		this.name = name;
		this.location = location;
		this.dateString = fmt.format(date);
		
		for(int i=0; i<agenda.length(); i++){
			this.agenda.add(new AgendaTopic(agenda.optJSONObject(i)));
		}
	}
	
	protected Convention(JSONObject json, SimpleDateFormat fmt){
		this(json.optInt("id"), json.optString("title"), json.optString("loc"), json.optLong("date"), json.optJSONArray("agenda"), fmt);
	}
	
	public int getId(){
		return id;
	}
	
	public String getDateString(){
		return dateString;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLocation(){
		return location;
	}

	public ArrayList<AgendaTopic> getAgenda(){
		return agenda;
	}
	
}
