package com.primefit.tool.userTest;

import com.primefit.tool.model.Training;
import com.primefit.tool.model.TrainingIntensity;
import com.primefit.tool.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.primefit.tool.model.TrainingIntensity.EXTREME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TrainingServiceTest {

    @Autowired
    private TrainingRepository trainingService;

    private final Integer TRAINING_ID = 1;
    private final String TRAINING_NAME = "HIIT";
    private final Integer TRAINING_DURATION = 45;
    private final TrainingIntensity TRAINING_INTENSITY = EXTREME;

    @Test
    public void findAll() {


        Training training = Training.builder()
                .id(TRAINING_ID)
                .name(TRAINING_NAME)
                .duration(TRAINING_DURATION)
                .trainingIntensity(TRAINING_INTENSITY)
                .build();

        trainingService.save(training);

        assertEquals(training.getId(), 1);
    }
}
