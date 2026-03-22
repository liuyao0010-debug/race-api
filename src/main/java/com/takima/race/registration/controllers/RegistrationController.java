package com.takima.race.registration.controllers;

import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.services.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/races/{raceId}/registrations")
    public Registration register(
            @PathVariable Long raceId,
            @RequestBody Registration regRequest
    ) {
        return registrationService.register(raceId, regRequest.getRunnerId());
    }

    @GetMapping("/races/{raceId}/registrations")
    public List<Long> getByRace(@PathVariable Long raceId) {
        return registrationService.getByRace(raceId)
                .stream()
                .map(Registration::getRunnerId)
                .collect(Collectors.toList());
    }

    @GetMapping("/runners/{runnerId}/races")
    public List<Long> getByRunner(@PathVariable Long runnerId) {
        return registrationService.getByRunner(runnerId)
                .stream()
                .map(Registration::getRaceId)
                .collect(Collectors.toList());
    }
}