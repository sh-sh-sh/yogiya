package com.yeogiya.web.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    public static final String CLOUD_FRONT_DOMAIN_NAME = "dh4i8qj952q7.cloudfront.net";
    private final String imageDirName = "image";

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(MultipartFile multipartFile, String convertedFileName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, convertedFileName);
    }

    private String upload(File uploadFile, String convertedFileName) {
        String fileName = imageDirName + "/" + convertedFileName;
        putS3(uploadFile, fileName);
        String uploadImageUrl = "https://" + CLOUD_FRONT_DOMAIN_NAME + "/" + fileName;
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }


    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String deleteFile) {
        amazonS3Client.deleteObject(bucket, deleteFile);
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("local File delete success");
            return;
        }
        log.info("local File delete fail");
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}