package com.ll.traveler.domain.travel.travelRoute.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.repository.TravelRouteRepository;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelRouteService {
    private final TravelRouteRepository travelRouteRepository;

    @Transactional
    public RsData<TravelRoute> write(Member author, String title, String body, String area, String startDate, String endDate) {
        TravelRoute travelRoute = TravelRoute.builder()
                .author(author)
                .title(title)
                .body(body)
                .area(area)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();

        travelRouteRepository.save(travelRoute);

        return RsData.of("200", "d번 여행 계획이 작성되었습니다.".formatted(travelRoute.getId()), travelRoute);
    }
    @Transactional
    public TravelPlace addPlace(TravelRoute travelRoute, String name, String address, int day, int order) {
        return travelRoute.addPlace(name, address, day, order);
    }

    public Optional<TravelRoute> findById(long id) {
        return travelRouteRepository.findById(id);
    }

    public boolean canModify(Member actor, TravelRoute travelRoute) {
        if(actor == null) {
            return false;
        }
        return actor.equals(travelRoute.getAuthor());
    }



    @Transactional
    public void deleteAllPlace(TravelRoute travelRoute) {
        travelRoute.deleteAllPlace();
    }

    @Transactional
    public void modify(TravelRoute travelRoute, String title, String body, String area, String startDate, String endDate) {
        travelRoute.setTitle(title);
        travelRoute.setBody(body);
        travelRoute.setArea(area);
        travelRoute.setStartDate(LocalDate.parse(startDate));
        travelRoute.setEndDate(LocalDate.parse(endDate));
    }

    public boolean canDelete(Member actor, TravelRoute travelRoute) {
        if(actor == null) return false;
        if(actor.isAdmin()) return true;

        return actor.equals(travelRoute.getAuthor());
    }

    @Transactional
    public void delete(TravelRoute travelRoute) {
        travelRouteRepository.delete(travelRoute);
    }
}
