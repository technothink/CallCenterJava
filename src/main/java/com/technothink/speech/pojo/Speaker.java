package com.technothink.speech.pojo;

public class Speaker {

	@Override
	public String toString() {
		return "Speaker [content=" + content + ", speakerLabel=" + speakerLabel + "]";
	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String speakerLabel;

	public String getSpeakerLabel() {
		return speakerLabel;
	}

	public void setSpeakerLabel(String speakerLabel) {
		this.speakerLabel = speakerLabel;
	}
}
