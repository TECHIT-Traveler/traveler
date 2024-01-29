package com.ll.traveler.domain.travel.travelRoute.controller;

import com.ll.traveler.domain.travel.travelPlace.entity.TravelPlace;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.service.TravelRouteService;
import com.ll.traveler.global.exceptions.GlobalException;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelRouteController {
    private final TravelRouteService travelRouteService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String showPlanSetting() {
        return "domain/travel/travelRoute/new";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public String PlanSetting(
            Model model,
            @RequestParam String area,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate _startDate = LocalDate.parse(startDate);
        LocalDate _endDate = LocalDate.parse(endDate);
        Period period = Period.between(_startDate, _endDate);

        int days = period.getDays();

        model.addAttribute("area", area);
        model.addAttribute("startDate", _startDate);
        model.addAttribute("endDate", _endDate);
        model.addAttribute("days", days);

        return "domain/travel/travelRoute/write";
    }

    @Getter
    @Setter
    public static class WriteForm {
        @NotBlank
        private String title;
        @NotBlank
        private String area;
        @NotBlank
        private String startDate;
        @NotBlank
        private String endDate;
        @NotBlank
        private String body;
        @NotEmpty
        private List<String> places;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid WriteForm form) {
        TravelRoute travelRoute = travelRouteService.write(rq.getMember(), form.getTitle(), form.getBody(), form.getArea(), form.getStartDate(), form.getEndDate()).getData();

        List<String> places = form.getPlaces();
        for(String place : places) {
            String[] placeInfo = place.split("/");
            int day = Integer.parseInt(placeInfo[0]);
            int order = Integer.parseInt(placeInfo[1]);
            String name = placeInfo[2];
            String address = placeInfo[3];

            travelRouteService.addPlace(travelRoute, name, address, day, order);
        }

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        TravelRoute travelRoute = travelRouteService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        if(!travelRouteService.canModify(rq.getMember(), travelRoute)) {
            throw new GlobalException("403-1", "권한이 없습니다.");
        }
        List<TravelPlace> places = travelRoute.getPlaces();

        Period period = Period.between(travelRoute.getStartDate(), travelRoute.getEndDate());
        int days = period.getDays();

        model.addAttribute("travelRoute", travelRoute);
        model.addAttribute("places", places);
        model.addAttribute("days", days);

        return "domain/travel/travelRoute/modify";
    }

    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String area;
        @NotBlank
        private String startDate;
        @NotBlank
        private String endDate;
        @NotBlank
        private String body;
        @NotEmpty
        private List<String> places;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}/modify")
    public String modify(@PathVariable long id, @Valid ModifyForm form) {
        TravelRoute travelRoute = travelRouteService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        if(!travelRouteService.canModify(rq.getMember(), travelRoute)) {
            throw new GlobalException("403-1", "권한이 없습니다.");
        }

        travelRouteService.modify(travelRoute, form.getTitle(), form.getBody(), form.getArea(), form.getStartDate(), form.getEndDate());
        travelRouteService.deleteAllPlace(travelRoute);
        for(String place : form.getPlaces()) {
            String[] placeInfo = place.split("/");
            int day = Integer.parseInt(placeInfo[0]);
            int order = Integer.parseInt(placeInfo[1]);
            String name = placeInfo[2];
            String address = placeInfo[3];
            travelRouteService.addPlace(travelRoute, name, address, day, order);
        }

        return "redirect:/";
    }
}
