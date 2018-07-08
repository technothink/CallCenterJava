
package com.technothink.speech.pojo;

import java.util.Arrays;

public class Results {

	private Transcripts[] transcripts;

	@Override
	public String toString() {
		return "Results [transcripts=" + Arrays.toString(transcripts) + ", items=" + Arrays.toString(items)
				+ ", speaker_labels=" + speaker_labels + "]";
	}

	private Items[] items;
	private Speaker_labels speaker_labels;

	public Items[] getItems() {
		return items;
	}

	public void setItems(Items[] items) {
		this.items = items;
	}

	public Transcripts[] getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(Transcripts[] transcripts) {
		this.transcripts = transcripts;
	}

	public Speaker_labels getSpeaker_labels() {
		return speaker_labels;
	}

	public void setSpeaker_labels(Speaker_labels speaker_labels) {
		this.speaker_labels = speaker_labels;
	}

}
