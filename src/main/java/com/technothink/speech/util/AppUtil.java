package com.technothink.speech.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.technothink.speech.core.S3Core;
import com.technothink.speech.core.TranscribeCore;
import com.technothink.speech.core.impl.S3CoreImpl;
import com.technothink.speech.core.impl.TranscribeCoreImpl;
import com.technothink.speech.exception.S3CoreException;
import com.technothink.speech.pojo.AWSTranscript;

public class AppUtil {

	private static final Logger log = Logger.getLogger(AppUtil.class);

	private static AppUtil senseUtil = null;
	private static byte[] lock = new byte[1];
	private S3Core core;
	private TranscribeCore transcribeCore;
	private AmazonS3 s3Client = null;
	private AmazonTranscribe transcribeClient = null;

	/**
	 * Load amazon web services
	 */
	static {
		System.setProperty("aws.accessKeyId", "AKIAJLL2IID6WUEYSZYA");
		System.setProperty("aws.secretKey", "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");
	}

	private AppUtil() {

		core = new S3CoreImpl();
		transcribeCore = new TranscribeCoreImpl();
		getS3Client("us-west-2");
		getTranscribeClient("us-west-2");
	}

	public static AppUtil getInstance() {
		if (null == senseUtil) {
			synchronized (lock) {
				if (null == senseUtil) {
					senseUtil = new AppUtil();

				}
			}
		}
		return senseUtil;
	}

	private AmazonS3 getS3Client(String region) {

		try {
			s3Client = core.connect(region);
			log.info("Amazon S3 client loaded..");
		} catch (S3CoreException e) {
			log.error("get S3Connection ", e);

		}
		return s3Client;

	}

	private AmazonTranscribe getTranscribeClient(String region) {

		try {
			transcribeClient = transcribeCore.connectTrnascribeService(region);
			log.info("Amazon Transcriber service loaded..");

		} catch (Throwable th) {
			log.error("get amazon transcribe connection ", th);
		}
		return transcribeClient;
	}

	public AmazonTranscribe getTranscribeClient() {

		return transcribeClient;
	}

	public void createBucket(String bucketName) {
		try {
			core.createBucket(s3Client, bucketName);
			log.info("Bucket created " + bucketName);
		} catch (Throwable th) {
			log.error("create bucket ", th);

		}
	}

	public void deleteBucket(String bucketName) {
		try {
			core.deleteBucket(s3Client, bucketName);
			log.info("Bucket deleted from cluod "+bucketName);

		} catch (Throwable th) {
			log.error("delete bucket ", th);
		}
	}

	public void uploadFileToBucket(String filePath, String bucketName, String fileType, String fileTitle) {
		try {
			core.uploadFileToBucket(s3Client, filePath, bucketName, fileType, fileTitle);
		} catch (Throwable th) {
			log.error("upload file ", th);
		}
		
	}

	public StartTranscriptionJobRequest createJobRequest(String fileUrl, String lang, int bitRate) {
		try {
			return transcribeCore.createJobRequest(fileUrl, lang, bitRate);

		} catch (Throwable th) {
			log.error("createJobRequest ", th);
		}
		return null;
	}

	public void startTranscribeJob(StartTranscriptionJobRequest request, String jobName, String mediaFormat) {

		try {
			transcribeCore.stratTranscribeJob(transcribeClient, request, jobName, mediaFormat);
		} catch (Throwable th) {
			log.error("start transcibe job ", th);
		}

	}

	public AWSTranscript getTranscript(String fileUri) {

		try {
			return transcribeCore.getTranscript(fileUri);
		} catch (Throwable th) {
			log.error("get transcript ", th);
		}
		return null;
	}

	public List<String> getFileListByBucket(String bucketName) {
		List<String> fileList = null;
		try {
			ObjectListing objectListing = s3Client.listObjects(bucketName);
			fileList = new ArrayList<String>();
			for (S3ObjectSummary os : objectListing.getObjectSummaries()) {

				String fileUrl = s3Client.getUrl(bucketName, os.getKey()).toString();
				fileList.add(fileUrl);

			}
		} catch (Throwable th) {
			log.error("Unable to get file list from bucket  ", th);
		}
		return fileList;
	}

}
