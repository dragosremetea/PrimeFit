package com.primefit.tool.unittesting.training;

import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.Training;
import com.primefit.tool.repository.TrainingRepository;
import com.primefit.tool.service.trainingservice.impl.TrainingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.primefit.tool.model.TrainingIntensity.EXTREME;
import static com.primefit.tool.model.TrainingIntensity.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingServiceImpl trainingServiceMock;

    private Training createTraining() {

        return Training.builder()
                .id(1)
                .name("HIIT")
                .pdfUrl("url")
                .duration(60)
                .trainingIntensity(EXTREME)
                .build();
    }

    @Test
    @DisplayName("Should get all trainings from the DB")
    public void getAllTest() {
        List<Training> trainingList = new ArrayList<>();
        Training training1 = createTraining();
        Training training2 = new Training(2, "HIIT2", "url2", 456, MEDIUM);

        trainingList.add(training1);
        trainingList.add(training2);

        // Arrange <=> Given
        when(trainingRepository.findAll()).thenReturn(trainingList);

        // Act <=> When
        List<Training> trainingServiceList = trainingServiceMock.findAll();

        // Assert <=> Then
        assertEquals(trainingList.size(), trainingServiceList.size());
        assertThat(trainingList.get(0)).isSameAs(trainingServiceList.get(0));
        assertThat(trainingList.get(1)).isSameAs(trainingServiceList.get(1));
        verify(trainingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save a new training in the DB")
    public void saveTest() {
        Training training = createTraining();

        // Arrange <=> Given
        when(trainingRepository.save(training)).thenReturn(training);

        // Act <=> When
        Training persistedTraining = trainingServiceMock.save(training);

        // Assert <=> Then
        assertEquals(training, persistedTraining);
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    @DisplayName("Should find a persisted training based on id")
    void findByIdTest() {
        Training training = createTraining();

        // Arrange <=> Given
        when(trainingRepository.findById(training.getId()))
                .thenReturn(Optional.of(training));

        // Act <=> When
        Optional<Training> expectedTraining = trainingServiceMock.findById(training.getId());

        // Assert <=> Then
        assert expectedTraining.isPresent();
        assertEquals(training, expectedTraining.get());

        verify(trainingRepository, times(1)).findById(training.getId());
    }

    @Test
    @DisplayName("Should find a persisted training based on name")
    void findByNameTest() {
        Training training = createTraining();

        // Arrange <=> Given
        when(trainingRepository.findByName(training.getName()))
                .thenReturn(Optional.of(training));

        // Act <=> When
        Optional<Training> expectedTraining = trainingServiceMock.findByName(training.getName());

        // Assert <=> Then
        assert expectedTraining.isPresent();
        assertEquals(training, expectedTraining.get());

        verify(trainingRepository, times(1)).findByName(training.getName());
    }

    @Test
    @DisplayName("Should delete a persisted training based on id")
    void deleteByIdTest() {
        Training training = createTraining();

        // Given:
        doNothing().when(trainingRepository).deleteById(training.getId());
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        // When: (do it!)
        trainingServiceMock.deleteById(training.getId());

        // Then: Verify (interactions, with as exact as possible parameters ..Mockito.any***)
        verify(trainingRepository).deleteById(training.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when training can not be deleted")
    void deleteByIdFailsTest() {
        Training training = createTraining();

        assertThatThrownBy(() ->  trainingServiceMock.deleteById(training.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Training with id : %s was not found!",  training.getId());
    }

    @Test
    @DisplayName("Should update an existing training from the DB")
    public void updateTest() {
        Training training = createTraining();

        Training newTraining = new Training(2, "Squads", "url2",10, EXTREME);

        //Given
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        //When
        trainingServiceMock.update(newTraining, training.getId());

        assertNotEquals(training.getId(), newTraining.getId());
        assertEquals(training.getTrainingIntensity(), newTraining.getTrainingIntensity());
        assertEquals(training.getPdfUrl(), newTraining.getPdfUrl());
        assertEquals(training.getDuration(), newTraining.getDuration());
        assertEquals(training.getName(), newTraining.getName());

        //Then
        verify(trainingRepository).findById(training.getId());
    }

    @Test()
    @DisplayName("Should throw ResourceNotFoundException when training can not be updated")
    void updateFailsTest() {
        Training training = createTraining();

        assertThatThrownBy(() ->  trainingServiceMock.update(training, 2))
                .isInstanceOf(RuntimeException.class);
    }
}
