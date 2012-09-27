package org.jsa.socal.mobile.android.api;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class AgendaTopic implements Serializable{
	
	private String block, text, time, location;
	private int id;
	
	protected AgendaTopic(String block, String text, String time, String location, int id){
		this.block = block;
		this.text = text;
		this.time = time;
		this.location = location;
		this.id = id;
	}
	
	protected AgendaTopic(JSONObject json){
		this(json.optString("block"), json.optString("text"), json.optString("time"), json.optString("loc"), json.optInt("id"));
	}
	
	public String getBlock(){
		return block;
	}
	
	public String getText(){
		return text;
	}
	
	public int getId(){
		return id;
	}
	
	public String getTime(){
		return time;
	}
	
	public String getLocation(){
		return location;
	}
	
	public static class Comment{
		
		private String author, text, time;
		
		public Comment(String author, String text, String time){
			this.author = author;
			this.text = text;
			this.time = time;
		}
		
		public Comment(JSONObject json){
			this(json.optString("name"), json.optString("text"), json.optString("time"));
		}
		
		public String getAuthor(){
			return author;
		}
		
		public String getText(){
			return text;
		}
		
		public String getTime(){
			return time;
		}
	}

}
