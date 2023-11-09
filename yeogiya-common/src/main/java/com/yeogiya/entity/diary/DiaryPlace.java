//package com.yeogiya.entity.diary;
//
//import com.yeogiya.entity.BaseTimeEntity;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Entity
//public class DiaryPlace extends BaseTimeEntity {
//    @Id
//    @Column(name = "id", columnDefinition = "INT(11)")
//    @GeneratedValue(strategy = IDENTITY)
//    private Long id;
//
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "diary_id", nullable = false)
//    private Diary diary;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "place_id", nullable = false)
//    private Place place;
//
//    @Builder
//    public DiaryPlace(Diary diary, Place place) {
//        this.diary = diary;
//        this.place = place;
//    }
//
//    public void update(Place place) {
//        this.place = place;
//    }
//
//}
