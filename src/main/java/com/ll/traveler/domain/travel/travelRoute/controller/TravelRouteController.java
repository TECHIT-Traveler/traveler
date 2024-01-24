package com.ll.traveler.domain.travel.travelRoute.controller;

import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.service.TravelRouteService;
import com.ll.traveler.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelRouteController {
    private final TravelRouteService travelRouteService;

    @GetMapping("/new")
    public String showPlanSetting() {
        return "domain/travel/travelRoute/new";
    }

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
        private String[] places;
    }
    @PostMapping("/write")
    public String write(@Valid WriteForm form) {
        TravelRoute travelRoute = travelRouteService.write(form.getTitle(), form.getBody(), form.getArea(), form.getStartDate(), form.getEndDate()).getData();

        String[] places = form.getPlaces();
        for(int i = 0; i < places.length; i++) {
            String[] placeInfo = places[i].split(",");
            int day = Integer.parseInt(placeInfo[0]);
            int order = Integer.parseInt(placeInfo[1]);
            String name = placeInfo[2];
            String address = placeInfo[3];

            travelRouteService.addPlace(travelRoute, name, address, day, order);
        }

        return "redirect:/";
    }
}
