package com.ll.traveler.domain.travel.travelRoute.repository;

import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRouteRepository extends JpaRepository<TravelRoute, Long> {
    Page<TravelRoute> findByTitleContaining(String kw, Pageable pageable);

    Page<TravelRoute> findByAreaContaining(String kw, Pageable pageable);
}
