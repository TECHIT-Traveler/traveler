package com.ll.traveler.domain.travel.travelRoute.entity;

import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
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

    @OneToMany(mappedBy = "route" ,cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<TravelPlace> places = new ArrayList<>();
}
