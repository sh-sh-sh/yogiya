package com.yeogiya.web.diary.dto.response;

import com.yeogiya.entity.diary.Diary;
import com.yeogiya.entity.diary.DiaryHashtag;
import com.yeogiya.entity.diary.DiaryImage;
import com.yeogiya.entity.diary.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiariesResponseDTO {

    private Long diaryId;
    private Long memberId;
    private String placeName;
    private String content;
    private String openYn;
    private Double star;
    private List<String> diaryImages;
    private List<String> hashtags;

    public DiariesResponseDTO(Diary diary){
        this.diaryId = diary.getId();
        this.content=diary.getContent();
        this.openYn=diary.getOpenYn().toString();
        this.memberId = diary.getMember().getMemberId();
        this.star = diary.getStar();

        this.hashtags = diary.getDiaryHashtags().stream()
                .map(DiaryHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());

        this.diaryImages = diary.getDiaryImages().stream()
                .map(DiaryImage::getPath)
                .collect(Collectors.toList());

        this.placeName = diary.getPlace().getName();


    }
}

