package org.redquark.demo.core.servlets;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.day.cq.dam.api.Asset;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.redquark.demo.core.constants.AppConstants.*;

/**
 * @author Anirudh Sharma
 * <p>
 * This servlet uploads the binary file to the S3 bucket
 */
@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=S3 File Uploader servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/demo/s3uploader"
        }
)
public class S3FileUploaderServlet extends SlingSafeMethodsServlet {

    // Serial version UID
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(S3FileUploaderServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        try {
            String filePath = request.getParameter("filePath");
            LOGGER.info("File path: {}", filePath);

            Resource resource = request.getResourceResolver().getResource(filePath);
            Asset asset = Objects.requireNonNull(resource).adaptTo(Asset.class);

            InputStream inputStream = Objects.requireNonNull(asset).getOriginal().getStream();
            String assetName = asset.getName();

            Regions clientRegion = Regions.AP_SOUTH_1;
            LOGGER.info("AWS Region: {}", clientRegion.getName());

            AWSCredentials awsCredentials = new BasicAWSCredentials(S3_ACCESS_KEY, S3_SECRET_KEY);

            AmazonS3 s3Client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(clientRegion)
                    .build();

            File file = File.createTempFile("temp", null);

            try (FileOutputStream outputStream = new FileOutputStream(file)) {

                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                PutObjectRequest putObjectRequest = new PutObjectRequest(S3_BUCKET_NAME, "test", file);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.addUserMetadata("title", assetName);
                putObjectRequest.setMetadata(objectMetadata);
                s3Client.putObject(putObjectRequest);

                response.getWriter().println("File uploaded!!");
            }
        } catch (IOException e) {
            LOGGER.error("Exception occurred: {}", e.getMessage());
        }
    }
}
