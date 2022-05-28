package com.primefit.tool.unittesting.role;

import com.primefit.tool.model.Role;
import com.primefit.tool.repository.RoleRepository;
import com.primefit.tool.service.roleservice.impl.RoleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleServiceMock;

    private Role createRole() {

        return Role.builder()
                .id(1)
                .name("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should save a new role in the DB")
    public void saveTest() {
        Role role = createRole();

        // Arrange <=> Given
        when(roleRepository.save(role)).thenReturn(role);

        // Act <=> When
        Role persistedRole = roleServiceMock.save(role);

        // Assert <=> Then
        assertEquals(role, persistedRole);
        verify(roleRepository, times(1)).save(role);
    }
}
