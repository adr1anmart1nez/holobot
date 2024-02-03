package dev.zawarudo.holo.modules.jikan.model;

import com.google.gson.annotations.SerializedName;

public class Released {
	@SerializedName("from")
	public String from;
	@SerializedName("to")
	public String to;
	@SerializedName("prop")
	public Prop prop;
	@SerializedName("string")
	public String string;	

	public static class Prop {
		@SerializedName("from")
		public From from;
		@SerializedName("to")
		public To to;
		
		public static class From {
			@SerializedName("day")
			public int day;
			@SerializedName("month")
			public int month;
			@SerializedName("year")
			public int year;
		}
		
		public static class To {
			@SerializedName("day")
			public int day;
			@SerializedName("month")
			public int month;
			@SerializedName("year")
			public int year;
		}
	}
}