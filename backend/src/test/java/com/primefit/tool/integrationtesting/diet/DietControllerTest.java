package com.primefit.tool.integrationtesting.diet;


import com.primefit.tool.model.Diet;
import com.primefit.tool.service.dietservice.DietService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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

import static com.primefit.tool.model.DietCategory.LOW_CARB;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DietControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DietService dietService;

    @Contract(" -> new")
    private @NotNull Diet createDiet() {
        return new Diet(1, "Vegan diet", "url", LOW_CARB);
    }

    @Test
    @DisplayName("Should get an empty list of diets from the DB")
    void getDietListEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/trainings").with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should get all diets from the DB")
    void getDietListTest() throws Exception {
        Diet diet = createDiet();

        when(dietService.findAll()).thenReturn(Collections.singletonList(diet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/diets").with(user("admin").password("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Vegan diet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pdfUrl").value("url"));
    }
}
