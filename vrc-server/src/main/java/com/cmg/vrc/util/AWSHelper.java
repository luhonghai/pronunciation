package com.cmg.vrc.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.cmg.vrc.properties.Configuration;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 5/19/15.
 */

public class AWSHelper {

    private static final Logger logger= Logger.getLogger(AWSHelper.class.getName());

    private AmazonS3 s3client;

    private String bucketName;

    public AWSHelper() {
        s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        bucketName = Configuration.getValue(Configuration.AWS_S3_BUCKET_NAME);
    }

    public boolean download(String keyName, File file) {
        try {
            System.out.println("Start download file " + keyName + " to local path: " + file.getAbsolutePath());
            s3client.getObject(new GetObjectRequest(bucketName, keyName), file);
            if (file.exists() && file.canRead()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Could not download file " + keyName + " from AWS S3. Message: " + e.getMessage());
            logger.log(Level.SEVERE, "Could not download file " + keyName + " from AWS S3", e);
        }
        return false;
    }

    public boolean downloadAndUnzip(String keyName, File targetFolder) {
        File tmp = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".zip");
        if (download(keyName, tmp)) {
            try {
                ZipFile zipFile = new ZipFile(tmp);
                zipFile.extractAll(targetFolder.getAbsolutePath());
            } catch (ZipException e) {
                logger.log(Level.SEVERE, "Could not unzip file " + keyName, e);
            }
            try {
                FileUtils.forceDelete(tmp);
            } catch (Exception e) {}
        }
        return false;
    }

    public boolean upload(String keyName, File file) {
        try {
            System.out.println("Start upload file: " + keyName + ". Local path: " + file.getAbsolutePath());
            s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
            return true;
        } catch (AmazonServiceException ase) {
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            sb.append("\n").append("Error Message:    " + ase.getMessage());
            sb.append("\n").append("HTTP Status Code: " + ase.getStatusCode());
            sb.append("\n").append("AWS Error Code:   " + ase.getErrorCode());
            sb.append("\n").append("Error Type:       " + ase.getErrorType());
            sb.append("\n").append("Request ID:       " + ase.getRequestId());
            System.out.println(sb.toString());
            logger.log(Level.SEVERE, sb.toString(), ase);
        } catch (AmazonClientException ace) {
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            sb.append("\n").append("Error Message: " + ace.getMessage());
            System.out.println(sb.toString());
            logger.log(Level.SEVERE, sb.toString(), ace);
        } catch (Exception e) {
            System.out.println("Could not upload file to S3. Message: " + e.getMessage());
            logger.log(Level.SEVERE, "Could not upload file to S3", e);
        }
        return false;
    }

    public void uploadInThread(final String keyName, File file) throws IOException {
        final File tmp = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".tmp");
        FileUtils.copyFile(file, tmp);
        if (tmp.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(keyName, tmp);
                    try {
                        FileUtils.forceDelete(tmp);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Could not delete temp file", e);
                    }

                }
            }).start();
        }
    }

    public static void main(String[] args) {
        AWSHelper awsHelper = new AWSHelper();
        System.out.println("Start uploading");
        //awsHelper.upload("sphinx-data/wsj-en-us.zip", new File("/Users/cmg/git/pronunciation/sphinx-data/wsj-en-us.zip"));
        //awsHelper.upload("sphinx-data/dict/beep-1.0", new File("/Volumes/DATA/OSX/luhonghai/Desktop/beep/beep-1.0"));
        awsHelper.upload("sphinx-data/dict/cmuphonemedict", new File("/Volumes/DATA/CMG/git/pronunciation/sphinx-data/dict/cmuphonemedict"));

        System.out.println("Complete uploading");
//        System.out.println("Start downloading & unzip");
//        awsHelper.downloadAndUnzip("sphinx-data/wsj-en-us.zip", new File("/Users/cmg/git/pronunciation/sphinx-data/tmp"));
//        System.out.println("Complete downloading & unzip");
    }
}
