package com.primefit.tool.unittesting.user;

import com.primefit.tool.exceptions.InvalidEmailException;
import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.Role;
import com.primefit.tool.model.User;
import com.primefit.tool.repository.RoleRepository;
import com.primefit.tool.repository.UserRepository;
import com.primefit.tool.service.userservice.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userServiceMock;

    private User createUser() {

        LocalDate now = LocalDate.now();
        Role role = new Role("ADMIN");
        roleRepository.save(role);
        Set<Role> set = new HashSet<>();
        set.add(role);

        return User.builder()
                .username("DOODS2012")
                .password("User12345")
                .firstName("Popescu")
                .lastName("Ion")
                .height(180)
                .weight(65)
                .email("ion@gmail.com")
                .phoneNumber("1234567890")
                .dateOfBirth(now)
                .gymSubscriptionStartDate(now)
                .roles(set)
                .locked(false)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should get all users from the DB")
    public void getAllTest() {
        List<User> userList = new ArrayList<>();
        LocalDate now = LocalDate.now();

        Role role = new Role("ADMIN");
        roleRepository.save(role);
        Set<Role> set = new HashSet<>();
        set.add(role);

        User user1 = createUser();
        User user2 = User.builder()
                .username("DOODS2022")
                .password("User123456")
                .firstName("Popescu1")
                .lastName("Ion1")
                .height(190)
                .weight(70)
                .email("ion2@gmail.com")
                .phoneNumber("123456789110")
                .dateOfBirth(now)
                .gymSubscriptionStartDate(now)
                .roles(set)
                .locked(false)
                .enabled(true)
                .build();

        userList.add(user1);
        userList.add(user2);

        // Arrange <=> Given
        when(userRepository.findAll()).thenReturn(userList);

        // Act <=> When
        List<User> userServiceList = userServiceMock.findAll();

        // Assert <=> Then
        assertEquals(userList.size(), userServiceList.size());
        assertThat(userList.get(0)).isSameAs(userServiceList.get(0));
        assertThat(userList.get(1)).isSameAs(userServiceList.get(1));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save a new user in the DB")
    public void saveTest() {
        User user = createUser();

        // Arrange <=> Given
        when(userRepository.save(user)).thenReturn(user);

        // Act <=> When
        User persistedUser = userServiceMock.save(user);

        // Assert <=> Then
        assertEquals(user, persistedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should find a persisted user based on id")
    void findByIdTest() {
        User user = createUser();

        // Arrange <=> Given
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        // Act <=> When
        Optional<User> expectedUser = Optional.ofNullable(userServiceMock.findById(user.getId()));

        // Assert <=> Then
        assert expectedUser.isPresent();
        assertEquals(user, expectedUser.get());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Should find a persisted user based on name")
    void findByUsernameTest() {
        User user = createUser();

        // Arrange <=> Given
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        // Act <=> When
        Optional<User> expectedUser = userServiceMock.findByUsername(user.getUsername());

        // Assert <=> Then
        assert expectedUser.isPresent();
        assertEquals(user, expectedUser.get());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Should check if a username already exists")
    void IsUsernameAlreadyExistsTest() {
        User user = createUser();

        // Arrange <=> Given
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        // Act <=> When
        boolean flag = userServiceMock.IsUsernameAlreadyExists(user.getUsername());

        // Assert <=> Then
        assertTrue(flag);

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Should delete a persisted user based on id")
    void deleteByIdTest() {
        User user = createUser();

        // Given:
        doNothing().when(userRepository).deleteById(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: (do it!)
        userServiceMock.deleteById(user.getId());

        // Then: Verify (interactions, with as exact as possible parameters ..Mockito.any***)
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to delete a non existing user")
    void deleteByIdFailsTest() {
        User user = createUser();

        assertThatThrownBy(() -> userServiceMock.deleteById(user.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id : %s was not found!", user.getId());
    }

    @Test
    @DisplayName("Should update an existing user from the DB")
    public void updateTest() {
        LocalDate now = LocalDate.now();

        Role role = new Role("ADMIN");
        roleRepository.save(role);
        Set<Role> set = new HashSet<>();
        set.add(role);

        User user = createUser();
        User newUser = User.builder()
                .id(2)
                .username("DOODS2022")
                .password("User123456")
                .firstName("Popescu1")
                .lastName("Ion1")
                .height(190)
                .weight(70)
                .email("ion2@gmail.com")
                .phoneNumber("123456789110")
                .dateOfBirth(now)
                .gymSubscriptionStartDate(now)
                .roles(set)
                .locked(false)
                .enabled(true)
                .build();

        //Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //When
        userServiceMock.update(newUser, user.getId());

        assertNotEquals(user.getId(), newUser.getId());
        assertEquals(user.getUsername(), newUser.getUsername());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals(user.getHeight(), newUser.getHeight());
        assertEquals(user.getWeight(), newUser.getWeight());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getPhoneNumber(), newUser.getPhoneNumber());
        assertEquals(user.getDateOfBirth(), newUser.getDateOfBirth());
        assertEquals(user.getGymSubscriptionStartDate(), newUser.getGymSubscriptionStartDate());
        assertEquals(user.getRoles(), newUser.getRoles());
        assertEquals(user.getLocked(), newUser.getLocked());
        assertEquals(user.getEnabled(), newUser.getEnabled());

        //Then
        verify(userRepository).findById(user.getId());
    }

    @Test()
    @DisplayName("Should throw ResourceNotFoundException when trying to update a non existing user")
    void updateFailsTest() {
        User user = createUser();

        assertThatThrownBy(() -> userServiceMock.update(user, 2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test()
    @DisplayName("Should enable a user after he enabled his email")
    void enableUserTest() {
        User user = createUser();

        when(userRepository.enableUser(user.getEmail())).thenReturn(1);

        int flag = userServiceMock.enableUser(user.getEmail());

        assertEquals(flag, 1);

        verify(userRepository).enableUser(user.getEmail());
    }

    @Test()
    @DisplayName("Should check if a user email is valid")
    void checkIfEmailIsValidTest() throws InvalidEmailException {
        List<User> userRepositoryList = new ArrayList<>();
        User user = createUser();
        userRepositoryList.add(user);

        // Arrange <=> Given
        when(userRepository.findAll()).thenReturn(userRepositoryList);

        // Act <=> When
        userServiceMock.checkIfEmailIsValid(user.getEmail());
        List<User> userServiceMockAllList = userServiceMock.findAll();

        // Assert <=> Then
        assertEquals(userServiceMockAllList.size(), 1);
        assertEquals(user.getUsername(), userServiceMockAllList.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }
}
