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

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingRepository.findAll());
    }

    @Override
    public Optional<Training> findById(@NotNull Integer id) {
        return trainingRepository.findById(id);
    }

    @Override
    public Optional<Training> findByName(String name) {
        return trainingRepository.findByName(name);
    }

    @Override
    public Training saveOrUpdate(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public void deleteById(Integer id) {
        trainingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Training", "id", id));
        trainingRepository.deleteById(id);
    }
}
