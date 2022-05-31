//package com.primefit.tool.integrationtesting.user;
//
//
//import com.primefit.tool.controller.TrainingController;
//import com.primefit.tool.model.Role;
//import com.primefit.tool.model.User;
//import com.primefit.tool.service.roleservice.RoleService;
//import com.primefit.tool.service.userservice.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ContextConfiguration(classes = TrainingController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private RoleService roleService;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @BeforeEach
//    public void setup() {
//        //Init MockMvc Object and build
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    void getAllEvents_emptyList() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/users")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
//    }
//
//    private User createUser() {
//        LocalDateTime date = LocalDateTime.of(2022, 12, 9, 20, 45);
//        Role role = new Role("ADMIN");
//        roleService.save(role);
//        Set<Role> set = new HashSet<>();
//        set.add(role);
//
//        return User.builder()
//                .username("DOODS2012")
//                .password("User12345")
//                .firstName("Popescu")
//                .lastName("Ion")
//                .height(180)
//                .weight(65)
//                .email("ion@gmail.com")
//                .phoneNumber("1234567890")
//                .dateOfBirth(LocalDate.from(date))
//                .gymSubscriptionStartDate(LocalDate.from(date))
//                .roles(set)
//                .locked(false)
//                .enabled(true)
//                .build();
//    }
//
//    @Test
//    void getAllEvents_notEmpty() throws Exception {
//        User user = createUser();
//
//        when(userService.findAll()).thenReturn(Collections.singletonList(user));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/events")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("DOODS2012"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value("User12345"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value("2022-12-09T20:45:00"));
//    }
//}
