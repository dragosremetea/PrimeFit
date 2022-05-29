package com.primefit.tool.service.trainingservice;

import com.primefit.tool.model.Training;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface used for declaring the methods signatures that can be performed with a training.
 */
public interface TrainingService {

    /***
     * Get a list with all the trainings
     * @return a list with all the trainings
     */
    List<Training> findAll();

    /***
     * Find a specific training based on id
     * @param id - the id of training
     * @return found training
     */
    Optional<Training> findById(@NotNull Integer id);

    /***
     * Find a specific training based on name
     * @param name - the name of training
     * @return found training
     */
    Optional<Training> findByName(String name);

    /***
     * Save a training.
     * @param training - the training to be persisted
     * @return saved training.
     */
    Training save(Training training);

    /***
     * Update all information for a specific training.
     * @param newTraining - the new training that will be saved
     * @param id - the id of the persisted training
     * @return updated training
     */
    Training update(@NotNull Training newTraining, Integer id);

    /***
     * Delete a training by a specific id.
     * @param id - the id of persisted training
     */
    void deleteById(Integer id);

    /**
     * @param file - the file we want to convert
     * @return converted file into a MultipartFile
     * @throws IOException - exception in case of failing conversion
     */
    File convertMultiPartToFile(@NotNull MultipartFile file) throws IOException;
}
