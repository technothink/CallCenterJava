package com.technothink.speech.core;

import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.technothink.speech.exception.TranscribeException;
import com.technothink.speech.pojo.AWSTranscript;

public interface TranscribeCore {

	AmazonTranscribe connectTrnascribeService(String region) throws TranscribeException;

	StartTranscriptionJobRequest createJobRequest(String fileUrl, String lang, int bitRate)
			throws TranscribeException;

	void stratTranscribeJob(AmazonTranscribe tClient, StartTranscriptionJobRequest request, String jobName,
			String mediaFormat) throws TranscribeException;

	AWSTranscript getTranscript(String fileUri) throws TranscribeException;

}
