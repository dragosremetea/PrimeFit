package com.primefit.tool.service.dietservice;

import com.primefit.tool.model.Diet;
import com.primefit.tool.model.Training;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface used for declaring the methods signatures that can be performed with a diet.
 */
public interface DietService {

    /***
     * Get a list with all the diets
     * @return a list with all the diets
     */
    List<Diet> findAll();

    /***
     * Find a specific diet based on id
     * @param id - the id of diet
     * @return found diet
     */
    Optional<Diet> findById(@NotNull Integer id);

    /***
     * Save a diet
     * @param diet - the diet to be persisted
     * @return saved diet
     */
    Diet save(Diet diet);

    /***
     * Update all information for a specific diet
     * @param newDiet - the new diet that will be saved
     * @param id - the id of the persisted diet
     * @return updated diet
     */
    Diet update(@NotNull Diet newDiet, Integer id);

    /***
     * Delete a diet by a specific id.
     * @param id - the id of persisted diet
     */
    void deleteById(Integer id);

    /**
     * @param file - the file we want to convert
     * @return converted file into a MultipartFile
     * @throws IOException - exception in case of failing conversion
     */
    File convertMultiPartToFile(@NotNull MultipartFile file) throws IOException;
}
