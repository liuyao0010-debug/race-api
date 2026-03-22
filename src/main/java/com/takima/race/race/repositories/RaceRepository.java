package com.takima.race.race.repositories;

import com.takima.race.race.entities.Race;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    List<Race> findByLocation(String location);
}
