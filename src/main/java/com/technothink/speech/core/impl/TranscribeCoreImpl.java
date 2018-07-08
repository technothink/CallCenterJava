package com.technothink.speech.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.google.gson.Gson;
import com.technothink.speech.core.TranscribeCore;
import com.technothink.speech.exception.TranscribeException;
import com.technothink.speech.pojo.AWSTranscript;

public class TranscribeCoreImpl implements TranscribeCore {

	private Gson gson = new Gson();

	public AmazonTranscribe connectTrnascribeService(String region) throws TranscribeException {
		AmazonTranscribe tClient = null;
		try {

			Regions regions = Regions.fromName(region);
			tClient = AmazonTranscribeClient.builder().withRegion(regions).build();
		} catch (Throwable th) {
			throw new TranscribeException("Unable to connect transcibe service ", th);
		}

		return tClient;
	}

	public StartTranscriptionJobRequest createJobRequest(String fileUrl, String lang, int bitRate)
			throws TranscribeException {
		
		StartTranscriptionJobRequest request = null;
		try {
			LanguageCode code = LanguageCode.fromValue(lang);
			request = new StartTranscriptionJobRequest();

			request.withLanguageCode(code);

			Media media = new Media();

			media.setMediaFileUri(fileUrl);

			request.withMedia(media).withMediaSampleRateHertz(bitRate);
			Settings settings=new Settings();
			settings.setMaxSpeakerLabels(10);
			settings.setShowSpeakerLabels(true);
			request.setSettings(settings);
		} catch (Throwable th) {
			throw new TranscribeException("Unable to create transcribe request ", th);
		}

		return request;

	}

	public void stratTranscribeJob(AmazonTranscribe tClient, StartTranscriptionJobRequest request, String jobName,
			String mediaFormat) throws TranscribeException {

		try {
			String transcriptionJobName = jobName;
			request.setTranscriptionJobName(transcriptionJobName);
			request.withMediaFormat(mediaFormat);
			tClient.startTranscriptionJob(request);
			
		} catch (Throwable th) {
			throw new TranscribeException("Unable to start transcibe job", th);
		}
	}

	public AWSTranscript getTranscript(String fileUri) throws TranscribeException {
		InputStreamReader inputStream = null;
		AWSTranscript awsTranscript = null;
		try {
			URL oracle = new URL(fileUri);
			inputStream = new InputStreamReader(oracle.openStream());
			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(inputStream);

			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine);
			in.close();

			awsTranscript = gson.fromJson(sb.toString(), AWSTranscript.class);
		} catch (Throwable th) {
			throw new TranscribeException("Unable to get transcript from amazon web file ", th);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new TranscribeException(e);
				}
			}
		}
		return awsTranscript;
	}

}
