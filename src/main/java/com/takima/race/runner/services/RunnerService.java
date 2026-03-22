package com.takima.race.runner.services;

import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public List<Runner> getAll() {
        return runnerRepository.findAll();
    }

    public Runner getById(Long id) {
        return runnerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Runner %s not found", id)
                )
        );
    }
    
    public Runner create(Runner runner) {
        validateRunner(runner);
        return runnerRepository.save(runner);
    }

    public Runner update(Long id, Runner runner) {
        Runner existing = runnerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Runner %s not found", id)
            ));
        
        validateRunner(runner);
        
        existing.setFirstName(runner.getFirstName());
        existing.setLastName(runner.getLastName());
        existing.setEmail(runner.getEmail());
        existing.setAge(runner.getAge());

        return runnerRepository.save(existing);
    }

    public void delete(Long id) {
        if(!runnerRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Runner %s not found", id)
            );
        }
        runnerRepository.deleteById(id);
    }

    private void validateRunner(Runner runner) {
        // 邮箱验证 (TP要求)
        String email = runner.getEmail();
        if (email == null || !email.contains("@")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email must contain @"
            );
        }
        
        // 年龄验证 (可选)
        if (runner.getAge() != null && runner.getAge() < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Age cannot be negative"
            );
        }

        // 姓名验证 (可选)
        if (runner.getFirstName() == null || runner.getFirstName().trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "First name is required"
            );
        }

        if (runner.getLastName() == null || runner.getLastName().trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Last name is required"
            );
        }
    }
}