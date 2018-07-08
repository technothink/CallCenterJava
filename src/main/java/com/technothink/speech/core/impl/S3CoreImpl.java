package com.technothink.speech.core.impl;

import java.io.File;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.technothink.speech.core.S3Core;
import com.technothink.speech.exception.S3CoreException;

/**
 * 
 * @author BAL VIKAS NIRALA
 *
 */
public class S3CoreImpl implements S3Core {

	/**
	 * Connecting with amazon web service
	 */
	public AmazonS3 connect(String region) throws S3CoreException {

		AmazonS3 s3client = null;
		try {
			Regions awsRegion = Regions.fromName(region);

			s3client = AmazonS3ClientBuilder.standard().withRegion(awsRegion)
					.withClientConfiguration(new ClientConfiguration())
					.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
		} catch (Exception e) {
			throw new S3CoreException("Unable to connect with amazon web service for region " + region, e);
		}

		return s3client;
	}

	public void createBucket(AmazonS3 s3client, String bucketName) throws S3CoreException {

		try {
			if (s3client.doesBucketExistV2(bucketName)) {

				throw new S3CoreException("Bucket already exist in s3 cloud service " + bucketName);
			}

			s3client.createBucket(bucketName);
		} catch (Exception e) {
			throw new S3CoreException("Unable to create bucket  " + bucketName, e);
		}
	}

	public void deleteBucket(AmazonS3 s3client, String bucketName) throws S3CoreException {

		try {
			s3client.deleteBucket(bucketName);

		} catch (Exception e) {
			throw new S3CoreException("Unable to delete bucket " + bucketName);
		}

	}

	public void uploadFileToBucket(AmazonS3 s3client, String filePath, String bucketName, String fileType,
			String fileTitle) throws S3CoreException {

		try {
			
			File file = new File(filePath);
			PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), new File(filePath));
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(fileType);
			metadata.addUserMetadata("x-amz-meta-title", fileTitle);
			request.setMetadata(metadata);
			s3client.putObject(request);

		} catch (Exception e) {
			throw new S3CoreException("File is not uploaded to aws cloud", e);

		}

	}
}
