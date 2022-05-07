package com.primefit.tool.training;

import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import com.primefit.tool.service.trainingservice.impl.TrainingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.primefit.tool.model.TrainingIntensity.EXTREME;
import static com.primefit.tool.model.TrainingIntensity.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingService  trainingService;

    private Training createTraining() {

        return Training.builder()
                .id(1)
                .name("HIIT")
                .duration(60)
                .trainingIntensity(EXTREME)
                .build();
    }

    @Test
    @DisplayName("Should get all trainings from the DB")
    public void getAllTest() {
        List<Training> trainingList = new ArrayList<>();
        Training training1 = createTraining();
        Training training2 = new Training(2, "HIIT2", 456, MEDIUM);

        trainingList.add(training1);
        trainingList.add(training2);

        when(trainingService.findAll()).thenReturn(trainingList);
        assertEquals(trainingList.size(), 2);
        assertEquals(2, trainingService.findAll().size());

        verify(trainingService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save a new training in the DB")
    public void saveTest() {
        Training training = createTraining();

        when(trainingService.save(training)).thenReturn(training);

        assertThat(training.getId()).isGreaterThan(0);
        assertThat(training.getName()).isNotNull();
        assertThat(training.getDuration()).isGreaterThan(0);
        assertThat(training.getTrainingIntensity()).isNotNull();
        assertEquals(training, trainingService.save(training));

        verify(trainingService, times(1)).save(training);
    }

    @Test
    @DisplayName("Should find a persisted training based on id")
    void findByIdTest() {
        Training expectedTraining = createTraining();

        when(trainingService.findById(expectedTraining.getId()))
                .thenReturn(Optional.of(expectedTraining));

        Optional<Training> actualTraining = trainingService.findById(expectedTraining.getId());

        assert actualTraining.isPresent();
        assertEquals(expectedTraining, actualTraining.get());

        verify(trainingService, times(1)).findById(expectedTraining.getId());
    }

    @Test
    @DisplayName("Should find a persisted training based on name")
    void findByNameTest() {
        Training expectedTraining = createTraining();

        when(trainingService.findByName(expectedTraining.getName()))
                .thenReturn(Optional.of(expectedTraining));

        Optional<Training> actualTraining = trainingService.findByName(expectedTraining.getName());

        assert actualTraining.isPresent();
        assertEquals(expectedTraining, actualTraining.get());

        verify(trainingService, times(1)).findByName(expectedTraining.getName());
    }

    @Test
    @DisplayName("Should delete a persisted training based on id")
    void deleteByIdTest() {
        Training expectedTraining = createTraining();

        trainingService.deleteById(expectedTraining.getId());

        verify(trainingService, times(1)).deleteById(expectedTraining.getId());
    }


    //@Test
    //	public void testDoThat() {
    //
    //		given(otherServiceMock.bar()).willThrow(new MyException());
    //
    //		when(() -> myService.foo());
    //
    //		then(caughtException()).isInstanceOf(MyException.class);  NoSuchElementException
    //	}


    @Test
    @DisplayName("Should update an existing training from the DB")
    public void updateTest() {
        Training training = createTraining();

        Training newTraining = new Training(2, "Squads", 10, EXTREME);

        when(trainingService.update(newTraining, training.getId())).thenReturn(training).thenThrow(new NoSuchElementException());

        assertThat(training.getId()).isGreaterThan(0);
        assertThat(training.getName()).isNotNull();
        assertThat(training.getDuration()).isGreaterThan(0);
        assertThat(training.getTrainingIntensity()).isNotNull();
        assertEquals(training, trainingService.update(newTraining, training.getId()));

        verify(trainingService, times(1)).update(newTraining, training.getId());
    }
}
