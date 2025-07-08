package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.UsuarioRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.UsuarioResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.RoleJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.UsuarioJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.RoleJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.UsuarioJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class UsuarioServiceTest {

    LocalDate localDate = LocalDate.now();
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    private UsuarioJpaRepository usuarioJpaRepository;
    private RoleJpaRepository roleJpaRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioJpaRepository = Mockito.mock(UsuarioJpaRepository.class);
        roleJpaRepository = Mockito.mock(RoleJpaRepository.class);
        usuarioService = new UsuarioService(usuarioJpaRepository, roleJpaRepository);
    }

    @Test
    void createUsuario() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombreUsuario("nuevoUsuario");
        request.setIdRole(1);
        request.setFecaRegistro(date);

        RoleJpa role = new RoleJpa();
        role.setIdRole(1);
        role.setNombreRole("ADMIN");

        when(usuarioJpaRepository.findByNombreUsuario("nuevoUsuario")).thenReturn(Optional.empty());
        when(roleJpaRepository.findById(1)).thenReturn(Optional.of(role));
        when(usuarioJpaRepository.save(any(UsuarioJpa.class))).thenAnswer(i -> {
            UsuarioJpa u = i.getArgument(0);
            u.setIdUsuario(10);
            return u;
        });

        UsuarioResponse response = usuarioService.createUsuario(request);

        assertNotNull(response);
        assertEquals(10, response.getIdUsuario());
        assertEquals("nuevoUsuario", response.getNombreUsuario());
        assertEquals("ADMIN", response.getRole().getNombreRole());
    }

    @Test
    void createUsuario_VerificarUsuarioExists() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombreUsuario("usuarioExistente");

        when(usuarioJpaRepository.findByNombreUsuario("usuarioExistente")).thenReturn(Optional.of(new UsuarioJpa()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.createUsuario(request);
        });

        assertTrue(exception.getMessage().contains("ya existe"));
    }

    @Test
    void updateUsuario() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombreUsuario("usuarioActualizado");
        request.setIdRole(2);

        RoleJpa oldRole = new RoleJpa();
        oldRole.setIdRole(1);
        oldRole.setNombreRole("USER");

        RoleJpa newRole = new RoleJpa();
        newRole.setIdRole(2);
        newRole.setNombreRole("ADMIN");

        UsuarioJpa existingUsuario = new UsuarioJpa();
        existingUsuario.setIdUsuario(5);
        existingUsuario.setNombreUsuario("usuarioViejo");
        existingUsuario.setRole(oldRole);
        existingUsuario.setContraseñaUsuario("pass");
        existingUsuario.setFechaRegistro(date);

        when(usuarioJpaRepository.findById(5)).thenReturn(Optional.of(existingUsuario));
        when(usuarioJpaRepository.findByNombreUsuario("usuarioActualizado")).thenReturn(Optional.empty());
        when(roleJpaRepository.findById(2)).thenReturn(Optional.of(newRole));
        when(usuarioJpaRepository.save(any(UsuarioJpa.class))).thenAnswer(i -> i.getArgument(0));

        UsuarioResponse response = usuarioService.updateUsuario(5, request);

        assertNotNull(response);
        assertEquals(5, response.getIdUsuario());
        assertEquals("usuarioActualizado", response.getNombreUsuario());
        assertEquals("ADMIN", response.getRole().getNombreRole());
    }

    @Test
    void updateUsuario_VerificarSiExisteOtroUsuario() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombreUsuario("usuarioExistente");
        request.setIdRole(1);

        RoleJpa role = new RoleJpa();
        role.setIdRole(1);
        role.setNombreRole("USER");

        UsuarioJpa existingUsuario = new UsuarioJpa();
        existingUsuario.setIdUsuario(5);
        existingUsuario.setNombreUsuario("usuarioViejo");
        existingUsuario.setRole(role);

        UsuarioJpa usuarioConMismoNombre = new UsuarioJpa();
        usuarioConMismoNombre.setIdUsuario(6);
        usuarioConMismoNombre.setNombreUsuario("usuarioExistente");

        when(usuarioJpaRepository.findById(5)).thenReturn(Optional.of(existingUsuario));
        when(usuarioJpaRepository.findByNombreUsuario("usuarioExistente")).thenReturn(Optional.of(usuarioConMismoNombre));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.updateUsuario(5, request);
        });

        assertTrue(exception.getMessage().contains("ya está registrada"));
    }

    @Test
    void getUsuarioById_returnUsuario() {
        UsuarioJpa usuario = new UsuarioJpa();
        usuario.setIdUsuario(7);
        usuario.setNombreUsuario("usuario7");

        when(usuarioJpaRepository.findById(7)).thenReturn(Optional.of(usuario));

        Optional<UsuarioJpa> result = usuarioService.getUsuarioById(7);

        assertTrue(result.isPresent());
        assertEquals("usuario7", result.get().getNombreUsuario());
    }

    @Test
    void getAllUsuarios() {
        List<UsuarioJpa> usuarios = Arrays.asList(
                new UsuarioJpa(),
                new UsuarioJpa()
        );

        when(usuarioJpaRepository.findAll()).thenReturn(usuarios);

        List<UsuarioJpa> result = usuarioService.getAllusuarios();

        assertEquals(2, result.size());
    }

    @Test
    void deleteUsuario_returnTrueCuandoExiste() {
        when(usuarioJpaRepository.existsById(1)).thenReturn(true);
        doNothing().when(usuarioJpaRepository).deleteById(1);

        boolean result = usuarioService.deleteUsuario(1);

        assertTrue(result);
        verify(usuarioJpaRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUsuario_returnFalseCuandoNoExiste() {
        when(usuarioJpaRepository.existsById(1)).thenReturn(false);

        boolean result = usuarioService.deleteUsuario(1);

        assertFalse(result);
        verify(usuarioJpaRepository, never()).deleteById(anyInt());
    }
}
