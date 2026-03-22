package com.takima.race.registration.services;

import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.repositories.RunnerRepository;
import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RunnerRepository runnerRepository;
    private final RaceRepository raceRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               RunnerRepository runnerRepository,
                               RaceRepository raceRepository) {
        this.registrationRepository = registrationRepository;
        this.runnerRepository = runnerRepository;
        this.raceRepository = raceRepository;
    }

    public Registration register(Long raceId, Long runnerId) {
        // 检查runner是否存在
        if(!runnerRepository.existsById(runnerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Runner not found");
        }

        // 检查race是否存在
        if(!raceRepository.existsById(raceId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Race not found");
        }

        // 检查是否重复注册
        if(registrationRepository.existsByRunnerIdAndRaceId(runnerId, raceId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Runner already registered");
        }

        // 检查比赛人数限制
        long count = registrationRepository.countByRaceId(raceId);
        Race race = raceRepository.findById(raceId).get(); // 已经检查存在，所以可以用get()
        if(count >= race.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Race is full");
        }

        // 创建注册记录
        Registration registration = new Registration();
        registration.setRaceId(raceId);
        registration.setRunnerId(runnerId);
        registration.setRegistrationDate(LocalDate.now());

        return registrationRepository.save(registration);
    }

    public List<Registration> getByRace(Long raceId) {
        // 检查race是否存在
        if (!raceRepository.existsById(raceId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Race not found");
        }
        return registrationRepository.findByRaceId(raceId);
    }

    public List<Registration> getByRunner(Long runnerId) {
        // 检查runner是否存在
        if (!runnerRepository.existsById(runnerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Runner not found");
        }
        return registrationRepository.findByRunnerId(runnerId);
    }
}