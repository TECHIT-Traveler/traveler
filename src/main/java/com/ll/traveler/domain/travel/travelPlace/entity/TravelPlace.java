package com.ll.traveler.domain.travel.travelPlace.entity;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.global.jpa.IdEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
public class TravelPlace extends IdEntity {
    private String name;
    private String address;
    private int travelOrder;
    private int travelDay;

    @ManyToOne
    private TravelRoute travelRoute;
}
