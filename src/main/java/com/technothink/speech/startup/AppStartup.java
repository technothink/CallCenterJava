package com.technothink.speech.startup;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.data.mongodb.core.MongoOperations;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.technothink.speech.pojo.AWSTranscript;
import com.technothink.speech.util.AppContext;
import com.technothink.speech.util.AppUtil;

/**
 * 
 * @author BAL VIKAS NIRALA
 *
 */
public class AppStartup {

	private static final Logger log = Logger.getLogger(AppStartup.class);
	private AppUtil appUtil = AppUtil.getInstance();

	/**
	 * Main method to start process
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String bucketName = "technobucket";
		String jobName = "t_job" + Math.random();
		String filePath = "D:\\Media\\myfile.wav";
		String fileType = "audio/wav";
		String fileTitle = "inbound";
		String mediaFormat = "wav";
		String fileName="myfile.wav";
		
		DOMConfigurator.configureAndWatch("log4j.xml");

		AppStartup startup = new AppStartup();

		startup.uploadFile(bucketName, filePath, fileType, fileTitle);
		startup.startJob(bucketName, jobName, mediaFormat,fileName);
		startup.monitorTranscribeResult(jobName);

	}

	void monitorTranscribeResult(String jobName) {

		try {

			GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();
			jobRequest.setTranscriptionJobName(jobName);
			TranscriptionJob transcriptionJob;

			while (true) {
				log.info("Checking for transcribe result ");
				transcriptionJob = appUtil.getTranscribeClient().getTranscriptionJob(jobRequest).getTranscriptionJob();

				if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {
					String genrateFileUri = transcriptionJob.getTranscript().getTranscriptFileUri();
					log.info("Transcribe result url returned " + genrateFileUri);
					saveinDB(genrateFileUri);
					log.info("Transcript Saved in database ");
					break;

				} else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
					log.info(transcriptionJob.toString());
					break;
				}

				synchronized (this) {
					this.wait(60000);
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	void saveinDB(String genrateFileUri) {

		AWSTranscript awsTranscript = appUtil.getTranscript(genrateFileUri);
		log.debug("AWSTranscript :: " + awsTranscript);

		MongoOperations mongoOperation = (MongoOperations) AppContext.getInstance().getContext()
				.getBean("mongoTemplate");

		mongoOperation.save(awsTranscript);

	}

	void startJob(String bucketName, String jobName, String mediaFormat, String fileName) {

		try {

			List<String> fileList = appUtil.getFileListByBucket(bucketName);

			for (String fileUrl : fileList) {

				log.info("File Location is " + fileUrl);
				if (fileUrl.contains(fileName)) {

					log.info("File name matched with file url so starting job request");
					
					StartTranscriptionJobRequest request = appUtil.createJobRequest(fileUrl, "en-US", 16000);
					appUtil.startTranscribeJob(request, jobName, mediaFormat);
					log.info("Transcribe job started ");
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	void uploadFile(String bucketName, String filePath, String fileType, String fileTitle) {

		try {

			// Create Bucket
			appUtil.createBucket(bucketName);

			// Upload file to bucket

			appUtil.uploadFileToBucket(filePath, bucketName, fileType, fileTitle);
			log.info("File uploaded to the bucket ");

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
