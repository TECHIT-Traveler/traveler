package com.ll.traveler.domain.travel.travelRoute.service;

import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.repository.TravelRouteRepository;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelRouteService {
    private final TravelRouteRepository travelRouteRepository;

    @Transactional
    public RsData<TravelRoute> write(String title, String body, String area, String _startDate, String _endDate) {
        LocalDate startDate = LocalDate.parse(_startDate);
        LocalDate endDate = LocalDate.parse(_endDate);

        TravelRoute travelRoute = TravelRoute.builder()
                .title(title)
                .body(body)
                .area(area)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        travelRouteRepository.save(travelRoute);

        return RsData.of("200", "d번 여행 계획이 작성되었습니다.".formatted(travelRoute.getId()), travelRoute);
    }
    @Transactional
    public TravelPlace addPlace(TravelRoute travelRoute, String name, String address, int day, int order) {
        return travelRoute.addPlace(name, address, day, order);
    }
}