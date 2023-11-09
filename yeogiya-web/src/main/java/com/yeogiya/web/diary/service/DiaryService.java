package com.yeogiya.web.diary.service;

import com.yeogiya.entity.diary.*;
import com.yeogiya.entity.member.Member;
import com.yeogiya.enumerable.EnumErrorCode;
import com.yeogiya.exception.ClientException;
import com.yeogiya.repository.*;
import com.yeogiya.web.auth.PrincipalDetails;
import com.yeogiya.web.diary.dto.request.CalendarPageRequestDTO;
import com.yeogiya.web.diary.dto.request.DiaryModifyRequestDTO;
import com.yeogiya.web.diary.dto.request.DiarySaveRequestDTO;
import com.yeogiya.web.diary.dto.response.CalendarPageResponseDTO;
import com.yeogiya.web.diary.dto.response.DiariesResponseDTO;
import com.yeogiya.web.diary.dto.response.DiaryIdResponseDTO;
import com.yeogiya.web.diary.dto.request.PlaceRequestDTO;
import com.yeogiya.web.diary.dto.response.DiaryResponseDTO;
import com.yeogiya.web.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    private final DiaryImageService diaryImageService;
    private final DiaryHashtagService hashtagService;
    private final DiaryPlaceService placeService;

    @Transactional
    public DiaryIdResponseDTO postDiary(DiarySaveRequestDTO diarySaveRequestDTO,
                                        PlaceRequestDTO placeRequestDTO,
                                        PrincipalDetails principal,
                                        List<MultipartFile> multipartFiles) throws IOException {

        Member member = memberRepository.findById(principal.getUsername()).orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOT_FOUND_MEMBER));
        Diary diary = diarySaveRequestDTO.toEntity(member);

        hashtagService.setTags(diary, diarySaveRequestDTO.getHashtags());
        diaryImageService.setDiaryImage(diary, multipartFiles);
        placeService.setPlace(diary, placeRequestDTO);

        diaryRepository.save(diary);

        return DiaryIdResponseDTO.builder()
                .id(diary.getId())
                .build();
    }

    @Transactional
    public DiaryIdResponseDTO modifyDiary(Long diaryId,
                                          DiaryModifyRequestDTO diaryModifyRequestDTO,
                                          PlaceRequestDTO placeRequestDTO,
                                          PrincipalDetails principal,
                                          List<MultipartFile> multipartFiles) throws IOException {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(
                () -> new ClientException.NotFound(EnumErrorCode.NOT_FOUND_DIARY)
        );

        validateAccess(principal, diary);
        hashtagService.setTags(diary, diaryModifyRequestDTO.getHashtags());
        diaryImageService.setDiaryImage(diary, multipartFiles);
        placeService.setPlace(diary, placeRequestDTO);

        diary.update(diaryModifyRequestDTO.getContent(), diaryModifyRequestDTO.getOpenYn(), diaryModifyRequestDTO.getStar());


        return DiaryIdResponseDTO.builder()
                .id(diaryId)
                .build();

    }

    @Transactional
    public DiaryIdResponseDTO deleteDiary(Long diaryId, PrincipalDetails principal) {

        Diary diary = diaryRepository.findById(diaryId).orElseThrow(
                () -> new ClientException.NotFound(EnumErrorCode.NOT_FOUND_DIARY)
        );
        validateAccess(principal, diary);
        diaryRepository.delete(diary);

        return DiaryIdResponseDTO.builder()
                .id(diaryId)
                .build();
    }

    public DiaryResponseDTO getDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() ->  new ClientException.NotFound(EnumErrorCode.NOT_FOUND_DIARY));
        return new DiaryResponseDTO(diary);
    }

    @Transactional
    public CalendarPageResponseDTO getDiaries(CalendarPageRequestDTO calendarPageRequestDTO, PrincipalDetails principal) {

        List<Diary> diaries = new ArrayList<>();

        // if(calendarPageRequestDTO.getDay()==0){  월별, 주간별 기능 추가되면 복구
            Calendar cal = Calendar.getInstance();
            cal.set(calendarPageRequestDTO.getYear(), calendarPageRequestDTO.getMonth(),calendarPageRequestDTO.getDay());
            calendarPageRequestDTO.setDay(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            diaries = diaryRepository.findAllByCreatedAtBetweenAndMemberOrderByCreatedAtAsc(
                    DateUtils.startDateTime(DateUtils.startDate(calendarPageRequestDTO))
                    ,DateUtils.endDateTime(DateUtils.getDate(calendarPageRequestDTO)),principal.getMember());
        // }

        CalendarPageResponseDTO calendarPageResponseDTO = new CalendarPageResponseDTO();
        for(Diary diary : diaries) {
            calendarPageResponseDTO.getDiaries().add(new DiariesResponseDTO(diary));
        }
        calendarPageResponseDTO.setTotalCnt(diaries.size());

        return calendarPageResponseDTO;
    }

    private void validateAccess(PrincipalDetails principal, Diary diary) {
        if (principal.getMember().getMemberId() != diary.getMember().getMemberId()) {
            throw new ClientException.Forbidden(EnumErrorCode.INVALID_ACCESS);
        }
    }

}
