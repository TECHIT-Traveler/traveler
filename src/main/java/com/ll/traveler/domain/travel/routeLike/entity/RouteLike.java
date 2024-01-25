package com.ll.traveler.domain.travel.routeLike.entity;
import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.global.jpa.IdEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
public class RouteLike extends IdEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private TravelRoute travelRoute;
}
