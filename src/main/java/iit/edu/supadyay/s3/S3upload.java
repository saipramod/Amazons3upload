/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.edu.supadyay.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import iit.edu.supadyay.controller.Controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author supramo
 */
public class S3upload {
    private static final String access = "AKIAJ7N4X47A2YPTZ56Q";
    private static final String secret = "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"; 
    private static final Logger LOG = Logger.getLogger(S3upload.class.getName());
    
    /**
     *
     * @return
     */
    public static List listOfBuckets(){
        List bucketNames = new ArrayList();
        AWSCredentials credentials = new BasicAWSCredentials(access, secret);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        for (Bucket bucket : s3client.listBuckets()) {
            bucketNames.add(bucket.getName());
        }
        //bucketNames.addAll(s3client.listBuckets());
        bucketNames.toString();
        return bucketNames;
    }
    public static boolean upload(String bucketName, String uploadFileName, String keyName) throws IOException, InterruptedException {
        
        //access = "AKIAJ2YSLRUZR5B3F5HQ";
        //secret = "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";

        AWSCredentials credentials = new BasicAWSCredentials(access, secret);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            /*boolean bucketexists = false;
            for (Bucket bucket : s3client.listBuckets()) {
                   System.out.println(" - " + bucket.getName());
                   if (bucket.getName().equals(bucketName))
                       bucketexists = true;
            }*/
            //if (!bucketexists)
            System.out.println("I am before here\n");
            s3client.createBucket(bucketName);
            System.out.println("I am here\n");
            s3client.putObject(new PutObjectRequest(
                    bucketName, keyName, file));
            

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which "
                    + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response"
                    + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            return false;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which "
                    + "means the client encountered "
                    + "an internal error while trying to "
                    + "communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            return false;
        }
        return true;
    }

}