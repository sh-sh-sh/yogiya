package com.yeogiya.web.diary.service;

import com.yeogiya.entity.diary.Diary;
import com.yeogiya.entity.diary.Place;
import com.yeogiya.repository.PlaceRepository;
import com.yeogiya.web.diary.dto.request.PlaceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryPlaceService {
    private final PlaceRepository placeRepository;

    private Place getPlace(PlaceRequestDTO dto) {
        return placeRepository.findByKakaoId(dto.getKakaoId()).orElseGet(() -> placeRepository.save(
                Place.builder()
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .kakaoId(dto.getKakaoId())
                        .build()));
    }

    @Transactional
    public void setPlace(Diary diary, PlaceRequestDTO placeDto) {
        diary.setPlace(getPlace(placeDto));
    }

}
