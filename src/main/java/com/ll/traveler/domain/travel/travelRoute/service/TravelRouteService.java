package com.ll.traveler.domain.travel.travelRoute.service;

import com.ll.traveler.domain.base.genFile.entity.GenFile;
import com.ll.traveler.domain.base.genFile.service.GenFileService;
import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.repository.TravelRouteRepository;
import com.ll.traveler.global.app.AppConfig;
import com.ll.traveler.global.rsData.RsData;
import com.ll.traveler.standard.utill.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelRouteService {
    private final TravelRouteRepository travelRouteRepository;
    private final GenFileService genFileService;

    public Page<TravelRoute> search(String kw, String criteria, Pageable pageable) {
        switch(criteria) {
            case "area" :
                return travelRouteRepository.findByAreaContaining(kw, pageable);
            default:
                return travelRouteRepository.findByTitleContaining(kw, pageable);
        }
    }

    @Transactional
    public RsData<TravelRoute> write(Member author, String title, String body, String area, String startDate, String endDate, MultipartFile coverImg) {
        String coverImgFilePath = Ut.file.toFile(coverImg, AppConfig.getTempDirPath());
        TravelRoute travelRoute = TravelRoute.builder()
                .author(author)
                .title(title)
                .body(body)
                .area(area)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();

        travelRouteRepository.save(travelRoute);

        if (Ut.str.hasLength(coverImgFilePath)) {
            saveCoverImg(travelRoute, coverImgFilePath);
        }

        return RsData.of("200", "d번 여행 계획이 작성되었습니다.".formatted(travelRoute.getId()), travelRoute);
    }

    public void saveCoverImg(TravelRoute travelRoute, String coverImgFilePath) {
        genFileService.save(travelRoute.getModelName(), travelRoute.getId(), "common", "coverImg", 1, coverImgFilePath);
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
    public void modify(TravelRoute travelRoute, String title, String body, String area, String startDate, String endDate, MultipartFile coverImg) {
        String coverImgFilePath = Ut.file.toFile(coverImg, AppConfig.getTempDirPath());

        if (Ut.str.hasLength(coverImgFilePath)) {
            saveCoverImg(travelRoute, coverImgFilePath);
        }

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

    public boolean canLike(Member actor, TravelRoute travelRoute) {
        if(actor == null) return false;

        return !travelRoute.hasLike(actor);
    }

    public boolean canCancelLike(Member actor, TravelRoute travelRoute) {
        if(actor == null) return false;

        return travelRoute.hasLike(actor);
    }

    @Transactional
    public void like(Member actor, TravelRoute travelRoute) {
        travelRoute.addLike(actor);
    }

    @Transactional
    public void cancelLike(Member actor, TravelRoute travelRoute) {
        travelRoute.deleteLike(actor);
    }

    public String getCoverImgUrl(TravelRoute travelRoute) {
        return Optional.ofNullable(travelRoute)
                .flatMap(this::findCoverImgUrl)
                .orElse("");
    }

    private Optional<String> findCoverImgUrl(TravelRoute travelRoute) {
        return genFileService.findBy(
                        travelRoute.getModelName(), travelRoute.getId(), "common", "coverImg", 1
                )
                .map(GenFile::getUrl);
    }

    public List<TravelRoute> findByAuthorId(Long id) {
        return travelRouteRepository.findByAuthorId(id);
    }
}
