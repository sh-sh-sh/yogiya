package com.yeogiya.web.image;

import com.yeogiya.web.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageUploadService {

    private final S3Uploader s3Uploader;

    @Transactional
    public String upload(MultipartFile multipartFile) {
        try {
            return s3Uploader.upload(multipartFile, multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: 추후 커스텀 에러로 변경
        }
    }
}
