package com.primefit.tool.unittesting.diet;


import com.primefit.tool.model.Diet;
import com.primefit.tool.repository.DietRepository;
import com.primefit.tool.service.dietservice.impl.DietServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.primefit.tool.model.DietCategory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {

    @Mock
    private DietRepository dietRepository;

    @InjectMocks
    private DietServiceImpl dietServiceMock;

    private Diet createDiet() {

        return Diet.builder()
                .id(1)
                .name("Vegan diet")
                .pdfUrl("url")
                .dietCategory(LOW_CARB)
                .build();
    }

    @Test
    @DisplayName("Should get all diets from the DB")
    public void getAllTest() {
        List<Diet> dietList = new ArrayList<>();
        Diet diet1 = createDiet();
        Diet diet2 = new Diet(2, "Paleo", "url2", PALEO);

        dietList.add(diet1);
        dietList.add(diet2);

        // Arrange <=> Given
        when(dietRepository.findAll()).thenReturn(dietList);

        // Act <=> When
        List<Diet> dietServiceList = dietServiceMock.findAll();

        // Assert <=> Then
        assertEquals(dietList.size(), dietServiceList.size());
        assertThat(dietList.get(0)).isSameAs(dietServiceList.get(0));
        assertThat(dietList.get(1)).isSameAs(dietServiceList.get(1));
        verify(dietRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find a persisted diet based on id")
    void findByIdTest() {
        Diet diet = createDiet();

        // Arrange <=> Given
        when(dietRepository.findById(diet.getId()))
                .thenReturn(Optional.of(diet));

        // Act <=> When
        Optional<Diet> expectedDiet = dietServiceMock.findById(diet.getId());

        // Assert <=> Then
        assert expectedDiet.isPresent();
        assertEquals(diet, expectedDiet.get());

        verify(dietRepository, times(1)).findById(diet.getId());
    }

    @Test
    @DisplayName("Should save a new diet in the DB")
    public void saveTest() {
        Diet diet = createDiet();

        // Arrange <=> Given
        when(dietRepository.save(diet)).thenReturn(diet);

        // Act <=> When
        Diet persistedDiet = dietServiceMock.save(diet);

        // Assert <=> Then
        assertEquals(diet, persistedDiet);
        verify(dietRepository, times(1)).save(diet);
    }

    @Test
    @DisplayName("Should update an existing diet from the DB")
    public void updateTest() {
        Diet diet = createDiet();

        Diet newDiet = new Diet(2, "Gluten_free", "url2", GLUTEN_FREE);

        //Given
        when(dietRepository.findById(diet.getId())).thenReturn(Optional.of(diet));

        //When
        dietServiceMock.update(newDiet, diet.getId());

        assertNotEquals(diet.getId(), newDiet.getId());
        assertEquals(diet.getName(), newDiet.getName());
        assertEquals(diet.getPdfUrl(), newDiet.getPdfUrl());
        assertEquals(diet.getDietCategory(), newDiet.getDietCategory());

        //Then
        verify(dietRepository).findById(diet.getId());
    }

}
