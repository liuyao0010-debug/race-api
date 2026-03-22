package com.takima.race.runner.repositories;

import com.takima.race.runner.entities.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Long> {
    List<Runner> findByFirstName(String firstName);
}