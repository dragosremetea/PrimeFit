package com.primefit.tool.unittesting.confirmationtoken;

import com.primefit.tool.exceptions.ResourceNotFoundException;
import com.primefit.tool.model.ConfirmationToken;
import com.primefit.tool.model.Role;
import com.primefit.tool.model.User;
import com.primefit.tool.repository.ConfirmationTokenRepository;
import com.primefit.tool.repository.RoleRepository;
import com.primefit.tool.service.confirmationtokenservive.impl.ConfirmationTokenImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ConfirmationTokenImpl confirmationTokenServiceMock;

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

    private ConfirmationToken createToken() {
        LocalDateTime now = LocalDateTime.now();
        User user = createUser();

        return ConfirmationToken.builder()
                .id(1)
                .token("token")
                .tokenCreatedAt(now)
                .tokenConfirmedAt(now.plusMinutes(5))
                .tokenExpiresAt(now.plusMinutes(15))
                .User(user)
                .build();
    }

    @Test
    @DisplayName("Should save a new token in the DB")
    public void saveConfirmationTokenTest() {
        ConfirmationToken token = createToken();

        // Arrange <=> Given
        when(confirmationTokenRepository.save(token)).thenReturn(token);

        // Act <=> When
        ConfirmationToken persistedRole = confirmationTokenServiceMock.saveConfirmationToken(token);

        // Assert <=> Then
        assertEquals(token, persistedRole);
        verify(confirmationTokenRepository, times(1)).save(token);
    }

    @Test
    @DisplayName("Should find a persisted confirmation token based on token")
    void getTokenTest() {
        ConfirmationToken token = createToken();

        // Arrange <=> Given
        when(confirmationTokenRepository.findByToken(token.getToken()))
                .thenReturn(Optional.of(token));

        // Act <=> When
        Optional<ConfirmationToken> expectedUser = confirmationTokenServiceMock.getToken(token.getToken());

        // Assert <=> Then
        assert expectedUser.isPresent();
        assertEquals(token, expectedUser.get());

        verify(confirmationTokenRepository, times(1)).findByToken(token.getToken());
    }

    @Test
    @DisplayName("Should delete a persisted token based on id")
    void deleteTest() {
        ConfirmationToken token = createToken();

        // Given:
        doNothing().when(confirmationTokenRepository).deleteById(token.getId());
        when(confirmationTokenRepository.findById(token.getId())).thenReturn(Optional.of(token));

        // When: (do it!)
        confirmationTokenServiceMock.delete(token.getId());

        // Then: Verify
        verify(confirmationTokenRepository).deleteById(token.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when training can not be deleted")
    void deleteFailsTest() {
        ConfirmationToken token = createToken();

        assertThatThrownBy(() -> confirmationTokenServiceMock.delete(token.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Token with id : %s was not found!", token.getId());
    }

    @Test
    @DisplayName("Should set a confirmation date if email is validated")
    void setConfirmedAtTest() {
        ConfirmationToken token = createToken();
        LocalDateTime now = LocalDateTime.now();

        // Given:
        lenient().when(confirmationTokenRepository.updateConfirmedAt(token.getToken(), now)).thenReturn(1);

        // When: (do it!)
        int flag = confirmationTokenServiceMock.setConfirmedAt(token.getToken());

        assertEquals(0, flag);
    }
}
