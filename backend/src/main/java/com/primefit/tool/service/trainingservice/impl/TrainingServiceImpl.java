package com.primefit.tool.service.trainingservice.impl;

import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.Training;
import com.primefit.tool.repository.TrainingRepository;
import com.primefit.tool.service.trainingservice.TrainingService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    /***
     * Get a list with all the trainings.
     * @return a list with all the trainings.
     */
    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingRepository.findAll());
    }

    /***
     * Find a specific training based on id.
     * @param id
     * @return found training.
     */
    @Override
    public Optional<Training> findById(@NotNull Integer id) {
        return trainingRepository.findById(id);
    }

    /***
     * Find a specific training based on name.
     * @param name
     * @return found training.
     */
    @Override
    public Optional<Training> findByName(String name) {
        return trainingRepository.findByName(name);
    }

    /***
     * Save a training.
     * @param training
     * @return saved training.
     */
    @Override
    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    /***
     * Update all information for a specific training.
     * @param newTraining
     * @param id
     * @return updated training.
     */
    @Override
    public Training update(@NotNull Training newTraining, Integer id) {
        Training training = trainingRepository.findById(id).orElseThrow();

        training.setName(newTraining.getName());
        training.setDuration(newTraining.getDuration());
        training.setTrainingIntensity(newTraining.getTrainingIntensity());

        return trainingRepository.save(training);
    }

    /***
     * Delete a training by a specific id.
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        trainingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Training", "id", id));
        trainingRepository.deleteById(id);
    }
}
