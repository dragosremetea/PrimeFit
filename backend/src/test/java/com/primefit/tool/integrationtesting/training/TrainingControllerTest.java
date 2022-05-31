package com.primefit.tool.integrationtesting.training;

import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static com.primefit.tool.model.TrainingIntensity.EXTREME;
import static com.primefit.tool.model.TrainingIntensity.MEDIUM;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

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
    @DisplayName("Should get an empty list of trainings from the DB")
    void getTrainingListEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/trainings").with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should get all trainings from the DB")
    void getTrainingListTest() throws Exception {
        Training training = createTraining();

        when(trainingService.findAll()).thenReturn(Collections.singletonList(training));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/trainings").with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HIIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pdfUrl").value("url"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].duration").value(60));
    }

    @Test
    @DisplayName("Should update a persisted training from DB")
    void updateTrainingTest() throws Exception {
        Training training = createTraining();
        Training updatedTraining = new Training(2, "HIIT2", "url2", 456, MEDIUM);

        when(trainingService.update(training, training.getId())).thenReturn(updatedTraining);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/trainings/{id}", training.getId()).with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete a persisted training from DB")
    void deleteTrainingTest() throws Exception {
        Training training = createTraining();

        doNothing().when(trainingService).deleteById(training.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/trainings/{id}", training.getId()).with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
