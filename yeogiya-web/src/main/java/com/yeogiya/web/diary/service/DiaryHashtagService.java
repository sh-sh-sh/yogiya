package com.yeogiya.web.diary.service;

import com.yeogiya.entity.diary.*;
import com.yeogiya.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryHashtagService {

    private final HashtagRepository hashtagRepository;

    private Hashtag getHashTag(String tagName) {
        return hashtagRepository.findByName(tagName)
                .orElseGet(() -> hashtagRepository.save(
                        Hashtag.builder()
                                .name(tagName)
                                .build()));
    }

    @Transactional
    public void setTags(Diary diary, List<String> tagNames) {
        diary.setDiaryHashtags(new ArrayList<>());
        if (tagNames != null) {
            for (String tagName : tagNames) {
                diary.addHashTag(getHashTag(tagName));
            }
        }
    }

}
