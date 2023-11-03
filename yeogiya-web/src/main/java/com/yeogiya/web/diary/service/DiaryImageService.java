package com.yeogiya.web.diary.service;


import com.yeogiya.entity.diary.Diary;
import com.yeogiya.entity.diary.DiaryImage;
import com.yeogiya.repository.DiaryImageRepository;
import com.yeogiya.web.diary.dto.request.DiaryImageRequestDTO;
import com.yeogiya.web.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryImageService {

    private final DiaryImageRepository diaryImageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public DiaryImage upload(MultipartFile multipartFile, Diary diary) throws IOException {


        String originalName = multipartFile.getOriginalFilename();
        String savedName = UUID.randomUUID() + originalName;
        String path = s3Uploader.upload(multipartFile, savedName);

        DiaryImageRequestDTO diaryImageRequestDto = new DiaryImageRequestDTO(originalName, savedName, path);

        DiaryImage diaryImage = diaryImageRepository.save(diaryImageRequestDto.toEntity(diary));

        return diaryImage;

    }
}