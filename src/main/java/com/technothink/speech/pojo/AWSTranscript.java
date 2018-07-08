package com.technothink.speech.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "awsTransData")
public class AWSTranscript {
	@Override
	public String toString() {
		return "AWSTranscript [accountId=" + accountId + ", results=" + results + ", status=" + status + ", jobName="
				+ jobName + ", speakers=" + speakers + "]";
	}
	
	private long Id;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	private String accountId;

	private Results results;

	private String status;

	private String jobName;
	
	private List<Speaker> speakers;
	
	

	private Sentiments sentiment;
	


	public Sentiments getSentiment() {
		return sentiment;
	}

	public void setSentiment(Sentiments sentiment) {
		this.sentiment = sentiment;
	}

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(List<Speaker> speakers) {
		this.speakers = speakers;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	
}
