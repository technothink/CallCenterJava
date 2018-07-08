package com.technothink.speech.core;

import com.amazonaws.services.s3.AmazonS3;
import com.technothink.speech.exception.S3CoreException;

/**
 * 
 * @author BAL VIKAS NIRALA
 * 
 */
public interface S3Core {

	AmazonS3 connect(String region) throws S3CoreException;

	void createBucket(AmazonS3 s3client,String bucketName) throws S3CoreException;

	void deleteBucket(AmazonS3 s3client,String bucketName) throws S3CoreException;

	void uploadFileToBucket(AmazonS3 s3client,String filePath, String bucketName,
			String fileType, String fileTitle) throws S3CoreException;

}
