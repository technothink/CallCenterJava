import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.google.gson.Gson;
import com.technothink.speech.pojo.AWSTranscript;
import com.technothink.speech.pojo.Alternatives;
import com.technothink.speech.pojo.Items;
import com.technothink.speech.pojo.Results;
import com.technothink.speech.pojo.Segments;
import com.technothink.speech.pojo.Sentiments;
import com.technothink.speech.pojo.Speaker;
import com.technothink.speech.pojo.SpeakerItems;
import com.technothink.speech.pojo.Speaker_labels;
import com.technothink.speech.pojo.Transcripts;

public class TestReadingFile {

	static {
		System.setProperty("aws.accessKeyId", "AKIAJLL2IID6WUEYSZYA");
		System.setProperty("aws.secretKey", "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");
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("SpringConfig.xml");

		// String fileUrl =
		// "https://s3.us-west-2.amazonaws.com/aws-transcribe-us-west-2-prod/441703088660/a6_job/asrOutput.json?X-Amz-Security-Token=FQoDYXdzEJ3%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaDIdgc%2FQbSsw0vcao9CK3A8hdgNH%2FN4YHy%2Fkr10WbPpumzCFuLvBXgUum42EpjDCKFC%2FNbDE%2FP5aQ03yVnkyTXjhxDlVQe2M5HvKymKnE9BXz2LG3T%2B6cQChXDDJpP8PLwocF%2FShBsLtv7kWeu4lGcy7Zy4dsPi1lVfS%2BoKgQzUwSz%2FjgbpGjsUyInACSREYvnFdADwJL9kYCf8Ged%2Bf4A4IRVdwBKQRvN%2FeHKf2rN50sMyYR%2BNsh%2F%2BMKi5KoVcik5PfLRE8FGhSsi6%2BIu2PtBI16YcApY6fqJ792Mg43TCNt98%2FAwiTN3iZCLttemNArDIiJtcVHjFY3QTpZvJD%2FkN5mQQK35Hm9zm1DiBeTzUVxb25OsS4M1MIkLttPKrn4ahtfQVE4cAHn82ovTdpBPjCQP5avHj9rxqs0NPPldkIJsMQpA143Xlfgewr3pil6gpF3OCikWP%2FjoeKQtchyiTn5hPzOiMpntKfGiVbAxLQs10ME58RDzlUPDqKTjhYB0lm4FwX3N7TAObbBK0jkN29lVzgBEiWCVoEpIY9hdQeIxd5cgpJr%2FUiHuCLMDN5pCTkr3hT4p7kJnFr12Pesg8PBJDiXSwQo8qnd2AU%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180606T034030Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=ASIAJT2EYDNX73PNT47Q%2F20180606%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=93fcf53fdc6e4cdc619035146205a1bf59d7477755129de00dd2b9605436667e\r\n";
		// URL oracle = new URL(fileUrl);
		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader("asrOutput.json");
		StringBuilder sb = new StringBuilder();
		BufferedReader in = new BufferedReader(fileReader);

		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();

		// System.out.println(sb.toString());
		Gson gson = new Gson();
		AWSTranscript awsTranscript = gson.fromJson(sb.toString(), AWSTranscript.class);
		awsTranscript.setId(System.currentTimeMillis());
		// System.out.println(awsTranscript);

		// String str = gson.toJson(awsTranscript);

		Results results = awsTranscript.getResults();
		for (Transcripts result : results.getTranscripts()) {

			// System.out.println(result.getTranscript());
			awsTranscript.setSentiment(detectSentiment(result.getTranscript()));
		}

		List<Speaker> speakers = printSpeakerData(awsTranscript);

		awsTranscript.setSpeakers(speakers);

		System.out.println(awsTranscript.toString());

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		mongoOperation.save(awsTranscript);

		System.out.println("Saved in database ");

	}

	static List<Speaker> printSpeakerData(AWSTranscript awsTranscript) {
		List<Speaker> speakers = new ArrayList<Speaker>();

		Speaker_labels labels = awsTranscript.getResults().getSpeaker_labels();
		String speaker = labels.getSpeakers();
		// System.out.println("No of speaker found " + speaker);
		Items[] items = awsTranscript.getResults().getItems();
		Segments[] segments = labels.getSegments();
		for (Segments spseg : segments) {
			String speakerLabel = spseg.getSpeaker_label();
			// System.out.println(speakerLabel);
			SpeakerItems[] spItems = spseg.getItems();
			String speakerTalk = "";
			for (SpeakerItems speakerItem : spItems) {
				String startTime = speakerItem.getStart_time();
				String endTime = speakerItem.getEnd_time();
				speakerTalk += " " + processItems(items, startTime, endTime);

			}
			// System.out.println(speakerTalk);
			Speaker sp = new Speaker();

			sp.setSpeakerLabel(speakerLabel.replace("spk_", "Speaker "));
			sp.setContent(speakerTalk);
			// System.out.println(sp.toString());
			speakers.add(sp);

		}
		return speakers;

	}

	static String processItems(Items[] items, String startTime, String endTime) {
		for (Items item : items) {
			if (null != item) {
				if (null != item.getStart_time() && null != item.getEnd_time()) {
					if (item.getStart_time().equals(startTime) && item.getEnd_time().equals(endTime)) {
						Alternatives[] alternatives = item.getAlternatives();
						for (Alternatives alt : alternatives) {

							return alt.getContent();
						}
					}

				}
			}
		}

		return null;
	}

	static Sentiments detectSentiment(String text) {

		Sentiments sentiments=new Sentiments();
		System.setProperty("aws.accessKeyId", "AKIAJLL2IID6WUEYSZYA");
		System.setProperty("aws.secretKey", "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");

		// Create credentials using a provider chain. For more information, see
		// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
		AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

		AmazonComprehend comprehendClient = AmazonComprehendClientBuilder.standard().withCredentials(awsCreds)
				.withRegion(Regions.US_WEST_2).build();

		System.out.println("Calling DetectSentiment");
		DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(text)
				.withLanguageCode("en");
		DetectSentimentResult detectSentimentResult = comprehendClient.detectSentiment(detectSentimentRequest);
		System.out.println(detectSentimentResult);
		System.out.println("End of DetectSentiment\n");
		System.out.println("Done");
		System.out.println(detectSentimentResult.getSentiment());
		sentiments.setScroe(detectSentimentResult.getSentimentScore().toString());
		sentiments.setSentiment(detectSentimentResult.getSentiment());
		return sentiments;
		
	}
}
