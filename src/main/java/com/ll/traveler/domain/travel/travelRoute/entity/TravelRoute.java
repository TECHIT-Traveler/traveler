package com.ll.traveler.domain.travel.travelRoute.entity;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.travel.routeLike.entity.RouteLike;
import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class TravelRoute extends BaseEntity {
    private String title;
    private String body;
    private LocalDate startDate;
    private LocalDate endDate;
    private String area;

    @OneToMany(mappedBy = "travelRoute" ,cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<TravelPlace> places = new ArrayList<>();

    @OneToMany(mappedBy = "travelRoute", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<RouteLike> likes = new ArrayList<>();

    @ManyToOne
    private Member author;

    public TravelPlace addPlace(String name, String address, int day, int order) {
        TravelPlace travelPlace = TravelPlace.builder()
                .travelRoute(this)
                .name(name)
                .address(address)
                .travelDay(day)
                .travelOrder(order)
                .build();

        places.add(travelPlace);

        return travelPlace;
    }

    public void deleteAllPlace() {
        places.clear();
    }

    public void addLike(Member member) {
        if(hasLike(member)) {
            return;
        }
        likes.add(RouteLike.builder()
                .travelRoute(this)
                .member(member)
                .build());
    }

    public boolean hasLike(Member member) {
        return likes.stream()
                .anyMatch(like -> like.getMember().equals(member));
    }

    public void deleteLike(Member member) {
        likes.removeIf(like -> like.getMember().equals(member));
    }
}
