
package com.technothink.speech.pojo;

public class SpeakerItems {
	private String end_time;

	private String speaker_label;

	private String start_time;

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getSpeaker_label() {
		return speaker_label;
	}

	public void setSpeaker_label(String speaker_label) {
		this.speaker_label = speaker_label;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	@Override
	public String toString() {
		return "ClassPojo [end_time = " + end_time + ", speaker_label = " + speaker_label + ", start_time = "
				+ start_time + "]";
	}
}
