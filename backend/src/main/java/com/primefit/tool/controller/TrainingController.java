package com.primefit.tool.controller;

import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Training> saveTraining(@RequestBody Training training) {
        trainingService.save(training);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Training> deleteTraining(@PathVariable Integer id) {
        trainingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(@RequestBody Training newTraining, @PathVariable Integer id) {
        trainingService.update(newTraining, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
