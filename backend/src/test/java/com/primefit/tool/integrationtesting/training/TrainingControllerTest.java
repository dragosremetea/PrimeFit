package com.primefit.tool.integrationtesting.training;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.primefit.tool.controller.TrainingController;
import com.primefit.tool.model.Training;
import com.primefit.tool.service.trainingservice.TrainingService;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.primefit.tool.model.TrainingIntensity.EXTREME;
import static com.primefit.tool.model.TrainingIntensity.MEDIUM;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@WebMvcTest
@ContextConfiguration(classes = TrainingController.class)
public class TrainingControllerTest {


    // MockMvc mockMvc;
    //
    //    @BeforeEach
    //    void setup(WebApplicationContext wac) {
    //        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    //    }
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        List<Training> listOfEmployees = new ArrayList<>();

        Training training1 = createTraining();
        Training training2 = new Training(2, "HIIT2", "url2", 456, MEDIUM);
        listOfEmployees.add(training1);
        listOfEmployees.add(training2);

        when(trainingService.findAll()).thenReturn(listOfEmployees);

        mockMvc
                .perform(get("/trainings").with(user("admin").password("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("HIIT")));
    }


//    @Test
//    public void testPostExample() throws Exception {
//        Training training = createTraining();
//
//        when(trainingService.save(ArgumentMatchers.any())).thenReturn(training);
//        String json = mapper.writeValueAsString(training);
//        mockMvc.perform(post("/trainings")
//                        .with(user("admin").password("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
//                        .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("HIIT")));
//    }


    @Test
    public void createNewItemTest() throws Exception {
        Training training = createTraining();
        String productJson = json(training);

        mockMvc
                .perform(post("/trainings").with(user("admin").password("admin").roles("ADMIN")).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateItemTest() throws Exception {
        Training training1 = createTraining();
        // Training training2 = new Training(2, "HIIT2", 456, MEDIUM);
        training1.setName("ceva");
        String productJson = json(training1);

        mockMvc.perform(put("/trainings/" + training1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
//               .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HIIT"));
    }


    /**
     * Converts object to JSON string format
     *
     * @param object to be converted
     * @return JSON String
     * @throws JsonProcessingException
     */
    protected String json(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Training training1 = createTraining();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training1)));

        // then - verify the result or output using assert statements
        response.andDo(print())

                .andExpect(jsonPath("$.name", is("HIIT")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }
}
