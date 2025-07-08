package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RoleJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RoleJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class RoleServiceTest {
    @Mock
    private RoleJpaRepository roleJpaRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test

    void gettAllRoles_siExisten() {
        RoleJpa adminRole = new RoleJpa();
        adminRole.setIdRole(1);
        adminRole.setNombreRole("ADMIN");

        RoleJpa userRole = new RoleJpa();
        userRole.setIdRole(2);
        userRole.setNombreRole("USER");

        List<RoleJpa> rolesJpa = Arrays.asList(adminRole, userRole);

        when(roleJpaRepository.findAll()).thenReturn(rolesJpa);

        List<RoleJpa> result = roleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombreRole());
        assertEquals("USER", result.get(1).getNombreRole());
        verify(roleJpaRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_returnElRoleSiExiste() {
        RoleJpa adminRole = new RoleJpa();
        adminRole.setIdRole(1);
        adminRole.setNombreRole("ADMIN");

        when(roleJpaRepository.findById(1)).thenReturn(Optional.of(adminRole));

        Optional<RoleJpa> result = roleService.findRoleById(1);

        assertNotNull(result);
        assertEquals(1, result.get().getIdRole());
        assertEquals("ADMIN", result.get().getNombreRole());
        verify(roleJpaRepository, times(1)).findById(1);
    }

    @Test
    void getRoleById_returnNullSiNoExisteElRole() {
        when(roleJpaRepository.findByNombreRole("EJEMPLO_TEST")).thenReturn(Optional.empty());

        Optional<RoleJpa> result = roleService.findRoleByNombreRole("EJEMPLO_TEST");

        assertTrue(result.isEmpty());
        verify(roleJpaRepository, times(1)).findByNombreRole("EJEMPLO_TEST");
    }
}
