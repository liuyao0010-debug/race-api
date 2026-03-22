package com.takima.race.race.services;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import com.takima.race.registration.repositories.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RegistrationRepository registrationRepository;

    public RaceService(RaceRepository raceRepository,
                       RegistrationRepository registrationRepository) {
        this.raceRepository = raceRepository;
        this.registrationRepository = registrationRepository;
    }


    public List<Race> getAll(String location) {
    if (location != null) {
        return raceRepository.findByLocation(location);
    }
    return raceRepository.findAll();
}

    public Race getById(Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Race not found"));
    }

    public Race create(Race race) {
        validateRace(race);
        return raceRepository.save(race);
    }

    public Race update(Long id, Race raceDetails) {
        Race race = getById(id); // 复用getById的404检查
        
        validateRace(raceDetails);
        
        race.setName(raceDetails.getName());
        race.setDate(raceDetails.getDate());
        race.setLocation(raceDetails.getLocation());
        race.setMaxParticipants(raceDetails.getMaxParticipants());
        
        return raceRepository.save(race);
    }

    public void delete(Long id) {
        if (!raceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Race not found");
        }
        
        // 检查是否有注册
        long registrationCount = registrationRepository.countByRaceId(id);
        if (registrationCount > 0) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, 
                "Cannot delete race with existing registrations"
            );
        }
        
        raceRepository.deleteById(id);
    }

    public long countParticipants(Long raceId) {
        if (!raceRepository.existsById(raceId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Race not found");
        }
        return registrationRepository.countByRaceId(raceId);
    }

    private void validateRace(Race race) {
        if (race.getName() == null || race.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Race name is required");
        }
        
        if (race.getLocation() == null || race.getLocation().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is required");
        }
        
        if (race.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is required");
        }
        
        if (race.getMaxParticipants() == null || race.getMaxParticipants() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Max participants must be positive"
            );
        }
    }
}