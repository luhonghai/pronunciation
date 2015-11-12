package com.cmg.vrc.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.actions.ElasticBeanstalkActions;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.elasticbeanstalk.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.properties.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 5/19/15.
 */

public class AWSHelper {

    private static final String BBC_ACCENT_APPLICATION_NAME = "AccentEasyTomcatServer";

    private static final String BBC_ACCENT_CONFIGURATION_TEMPLATE = "ami-linux-sphinx-tools-ffmpeg";

    private static final boolean ENABLE_AWS = true;

    private static final Logger logger= Logger.getLogger(AWSHelper.class.getName());

    private AmazonS3 s3client;

    private AWSElasticBeanstalkClient beanstalkClient;

    private String bucketName;

    public AWSHelper() {
        if (ENABLE_AWS) {
            BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAIW47GXTESF26RHZQ", "o+o1yyloDI4yRfwx0ffTAmk5nEc7iU7Bi32ur/gy");
            s3client = new AmazonS3Client(credentials);
            beanstalkClient = new AWSElasticBeanstalkClient(credentials);
            beanstalkClient.configureRegion(Regions.AP_SOUTHEAST_1);
            bucketName = Configuration.getValue(Configuration.AWS_S3_BUCKET_NAME);
        }
    }

    public String generatePresignedUrl(String keyName) {
        try {
            URL url = s3client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, keyName));
            return url.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public String generateUrl(String keyName) {
        try {
            java.util.Date expiration = new java.util.Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += 1000 * 60 * 60 * 24 * 3650 ; // Add 10 years.
            expiration.setTime(milliSeconds);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, keyName);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);
            URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (Exception e) {
            return "";
        }
    }
    public S3Object getS3Object(String keyName) {
        try {
            return s3client.getObject(bucketName, keyName);
        } catch (AmazonS3Exception e) {
            return null;
        }
    }

    public InputStream openInputStream(String keyName) {
        return s3client.getObject(bucketName, keyName).getObjectContent();
    }

    public boolean download(String keyName, File file) {
        if (!ENABLE_AWS) return false;
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
        if (!ENABLE_AWS) return false;
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

    /**
     *
     * @param keyName
     * @param file
     * @return url
     */
    public String uploadAndGenerateURL(String keyName, File file) {
        if (!ENABLE_AWS) return null;
        try {
            System.out.println("Start upload file: " + keyName + ". Local path: " + file.getAbsolutePath());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            FileInputStream stream = new FileInputStream(file);
            s3client.putObject(new PutObjectRequest(bucketName, keyName, stream,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
            return generateUrl(keyName);
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
        return null;
    }


    public boolean upload(String keyName, File file) {
        if (!ENABLE_AWS) return false;
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
        if (!ENABLE_AWS) return;
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


    public List<EnvironmentDescription> getEnvironments() {
        DescribeEnvironmentsResult describeEnvironmentsResult = beanstalkClient.describeEnvironments();
        return describeEnvironmentsResult.getEnvironments();
    }

    public void restartBeanstalkApp(String envName) {
        beanstalkClient.restartAppServer(new RestartAppServerRequest().withEnvironmentName(envName));
    }

    public void rebuildEnvironment(String envName) {
        beanstalkClient.rebuildEnvironment(new RebuildEnvironmentRequest().withEnvironmentName(envName));
    }

    public void terminateEnvironment(String envName) {
        beanstalkClient.terminateEnvironment(new TerminateEnvironmentRequest().withEnvironmentName(envName));
    }

    public String generateFeedbackImageUrl(String account, String fileName) {
        return generatePresignedUrl(Constant.FOLDER_FEEDBACK + "/" + account+ "/" + fileName);
    }

    public void createEnvironment(String envName, String envPrefix) {
        DescribeApplicationVersionsResult result = beanstalkClient.describeApplicationVersions();
        String vLabel = "";
        for (int i = 0; i < result.getApplicationVersions().size(); i++) {
            ApplicationVersionDescription v = result.getApplicationVersions().get(i);
            vLabel = v.getVersionLabel();
            System.out.println(vLabel);
            if (vLabel.toLowerCase().startsWith(envPrefix.toLowerCase())) {
                break;
            }

        }
        beanstalkClient.createEnvironment(new CreateEnvironmentRequest()
                .withApplicationName(BBC_ACCENT_APPLICATION_NAME)
                .withEnvironmentName(envName)
                .withTemplateName(BBC_ACCENT_CONFIGURATION_TEMPLATE)
                .withVersionLabel(vLabel)
                .withCNAMEPrefix(envName.toLowerCase()))
        ;
    }

    public void publicObject(String key) {
        s3client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
    }

    public void updateContentType(String key, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        final CopyObjectRequest request = new CopyObjectRequest(bucketName, key, bucketName, key)
                .withSourceBucketName( bucketName )
                .withSourceKey(key)
                .withNewObjectMetadata(metadata);
        s3client.copyObject(request);
    }

    public static void main(String[] args) {
        AWSHelper awsHelper = new AWSHelper();
        //awsHelper.terminateEnvironment("accenteasytomcat-SAT");
//        awsHelper.createEnvironment("accenteasytomcat-SAT", "SAT");
        //awsHelper.createEnvironment("accenteasytomcat-PRD", "PROD");
        //System.out.print(awsHelper.generateFeedbackImageUrl("hai.lu@c-mg.com", "2015-06-01-10-20-06.png"));
        //awsHelper.terminateEnvironment("accenteasytomcat-PRD-1");
        //awsHelper.getEnvironmentInfo("accenteasytomcat-PRD-1");
        //awsHelper.restartBeanstalkApp("e-axt4pi3kkm");
       // System.out.println("Start uploading");
        //awsHelper.upload("sphinx-data/wsj-en-us.zip", new File("/Volumes/DATA/CMG/git/pronunciation/sphinx-data/wsj-en-us.zip"));
       // awsHelper.upload("sphinx-data/dict/brit-a-z.txt", new File("/Volumes/DATA/CMG/git/pronunciation/sphinx-data/words/british/brit-a-z.txt"));
        //awsHelper.upload("sphinx-data/dict/beep-1.0", new File("/Volumes/DATA/OSX/luhonghai/Desktop/beep/beep-1.0"));
        //awsHelper.upload("sphinx-data/dict/cmuphonemedict", new File("/Volumes/DATA/CMG/git/pronunciation/sphinx-data/dict/cmuphonemedict"));

        //System.out.println("Complete uploading");
//        System.out.println("Start downloading & unzip");
        //awsHelper.downloadAndUnzip("sphinx-data/wsj-en-us.zip", new File("/Users/cmg/git/pronunciation/sphinx-data/tmp"));
        //awsHelper.download("", new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID()));
//        System.out.println("
// downloading & unzip");
        File input = new File("/Users/cmg/Desktop/File path audio.txt");
        File output = new File("/Users/cmg/Desktop/File path audio url.txt");

        try {
            if (output.exists()) FileUtils.forceDelete(output);
            List<String> strings = FileUtils.readLines(input);
            for (String line : strings) {
                line = line.trim();
                if (line.length() > 0) {
                    String key = line.split(" ")[0] + ".mp3";
                    System.out.println(key);
                    awsHelper.updateContentType(key, "audio/mpeg");
                    awsHelper.publicObject(key);
                    //String url = awsHelper.generateUrl(key);
                    //url = url.substring(0, url.lastIndexOf("?"));
                    //FileUtils.write(output, line + " " + url + "\n", "UTF-8", true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
