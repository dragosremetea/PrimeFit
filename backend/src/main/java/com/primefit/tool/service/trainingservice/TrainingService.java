package com.primefit.tool.service.trainingservice;

import com.primefit.tool.model.Training;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TrainingService {

    List<Training> findAll();

    Optional<Training> findById(@NotNull Integer id);

    Optional<Training> findByName(String name);

    Training saveOrUpdate(Training training);

    void deleteById(Integer id);
}
