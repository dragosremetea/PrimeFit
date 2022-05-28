package com.primefit.tool.service.trainingservice.impl;

import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.Training;
import com.primefit.tool.repository.TrainingRepository;
import com.primefit.tool.service.trainingservice.TrainingService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing trainings.
 */
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
    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public Training update(@NotNull Training newTraining, Integer id) {
        Training training = trainingRepository.findById(id).orElseThrow();

        training.setName(newTraining.getName());
        training.setPdfUrl(newTraining.getPdfUrl());
        training.setDuration(newTraining.getDuration());
        training.setTrainingIntensity(newTraining.getTrainingIntensity());

        return trainingRepository.save(training);
    }

    @Override
    public void deleteById(Integer id) {
        trainingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Training", "id", id));
        trainingRepository.deleteById(id);
    }

    @Override
    public File convertMultiPartToFile(@NotNull MultipartFile file) throws IOException {
        File convFile = new File("FileName");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
