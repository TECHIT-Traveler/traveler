package com.ll.traveler.domain.travel.travelRoute.service;

import com.ll.traveler.domain.travel.travelRoute.repository.TravelRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelRouteService {
    private final TravelRouteRepository travelRouteRepository;
}
