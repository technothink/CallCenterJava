package com.technothink.speech.pojo;

import java.util.Arrays;

public class Items {

	

	@Override
	public String toString() {
		return "Items [start_time=" + start_time + ", end_time=" + end_time + ", type=" + type + ", alternatives="
				+ Arrays.toString(alternatives) + "]";
	}

	private String start_time;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Alternatives[] getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(Alternatives[] alternatives) {
		this.alternatives = alternatives;
	}

	private String end_time;
	private String type;
	private Alternatives []alternatives;
}
