package com.primefit.tool.controller;

import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @GetMapping
    public List<Training> getTrainingList() {
        return trainingService.findAll();
    }

    @Secured("USER")
    @PostMapping
    public ResponseEntity<Training> saveTraining(@RequestBody Training training) {
        return new ResponseEntity<>(trainingService.saveOrUpdate(training), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public void deleteTraining(@PathVariable Integer id) {
        trainingService.deleteById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Training> saveOrUpdateTraining(@RequestBody Training newTraining, @PathVariable Integer id) {
        return trainingService.findById(id)
                .map(training -> {
                    training.setName(newTraining.getName());
                    training.setDuration(newTraining.getDuration());
                    training.setTrainingIntensity(newTraining.getTrainingIntensity());
                    return new ResponseEntity<>(trainingService.saveOrUpdate(training), HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    newTraining.setId(id);
                    return new ResponseEntity<>(trainingService.saveOrUpdate(newTraining), HttpStatus.CREATED);
                });
    }
}
