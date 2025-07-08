package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Request.ClienteRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Api_Client.Response.ClienteResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ClienteJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.ComunaJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ClienteJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Repository.Jpa.ComunaJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.moduleuser_client.Service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public class ClienteServiceTest {

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    @Mock
    private ComunaJpaRepository comunaJpaRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void CreateCliente_Success() {
        ClienteRequest request = new ClienteRequest();
        request.setNombreCliente("Juan");
        request.setApellidoCliente("Perez");
        request.setEmail("juan@mail.com");
        request.setTelefono(12345678);
        request.setContraseña("pass123");
        request.setDireccion("Calle Falsa 123");
        request.setFechaNacimiento(LocalDate.of(1990,1,1));
        request.setIdComuna(1);

        ComunaJpa comuna = new ComunaJpa();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("ComunaTest");

        when(clienteJpaRepository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
        when(clienteJpaRepository.findByTelefono(12345678)).thenReturn(Optional.empty());
        when(comunaJpaRepository.findById(1)).thenReturn(Optional.of(comuna));
        when(clienteJpaRepository.save(any(ClienteJpa.class))).thenAnswer(i -> {
            ClienteJpa c = i.getArgument(0);
            c.setClienteId(100);
            return c;
        });

        ClienteResponse response = clienteService.createCliente(request);

        assertEquals("Juan", response.getNombreCliente());
        assertEquals("Perez", response.getApellidoCliente());
        assertEquals("juan@mail.com", response.getEmail());
        assertEquals(12345678, response.getTelefono());
        assertEquals("pass123", response.getContraseña());
        assertEquals("Calle Falsa 123", response.getDireccion());
        assertEquals(LocalDate.of(1990,1,1), response.getFechaNacimiento());
        assertEquals(1, response.getComuna().getIdComuna());
        assertEquals("ComunaTest", response.getComuna().getNombreComuna());
    }

    @Test
    public void CreateCliente_EmailExists_Throws() {
        ClienteRequest request = new ClienteRequest();
        request.setEmail("juan@mail.com");

        when(clienteJpaRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(new ClienteJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            clienteService.createCliente(request);
        });
        assertTrue(ex.getMessage().contains("ya existe"));
    }

    @Test
    public void CreateCliente_TelefonoExists_Throws() {
        ClienteRequest request = new ClienteRequest();
        request.setEmail("nuevo@mail.com");
        request.setTelefono(12345678);

        when(clienteJpaRepository.findByEmail("nuevo@mail.com")).thenReturn(Optional.empty());
        when(clienteJpaRepository.findByTelefono(12345678)).thenReturn(Optional.of(new ClienteJpa()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            clienteService.createCliente(request);
        });
        assertTrue(ex.getMessage().contains("ya está registrado"));
    }

    @Test
    public void GetAllClientes_ReturnsList() {
        ClienteJpa c1 = new ClienteJpa();
        c1.setClienteId(1);
        ClienteJpa c2 = new ClienteJpa();
        c2.setClienteId(2);

        when(clienteJpaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        var result = clienteService.getAllClientes();

        assertEquals(2, result.size());
    }

    @Test
    public void DeleteClienteById_Exists_ReturnsTrue() {
        when(clienteJpaRepository.existsById(1)).thenReturn(true);
        doNothing().when(clienteJpaRepository).deleteById(1);

        boolean result = clienteService.deleteClienteById(1);

        assertTrue(result);
        verify(clienteJpaRepository).deleteById(1);
    }

    @Test
    public void DeleteClienteById_NotExists_ReturnsFalse() {
        when(clienteJpaRepository.existsById(99)).thenReturn(false);

        boolean result = clienteService.deleteClienteById(99);

        assertFalse(result);
        verify(clienteJpaRepository, never()).deleteById(any());
    }

    @Test
    public void GetClienteById_Exists() {
        ClienteJpa cliente = new ClienteJpa();
        cliente.setClienteId(1);

        when(clienteJpaRepository.findById(1)).thenReturn(Optional.of(cliente));

        Optional<ClienteJpa> result = clienteService.getClienteById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getClienteId());
    }

    @Test
    public void GetClienteById_NotExists() {
        when(clienteJpaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<ClienteJpa> result = clienteService.getClienteById(99);

        assertFalse(result.isPresent());
    }

    @Test
    public void UpdateCliente_Success() {
        ClienteJpa clienteExistente = new ClienteJpa();
        clienteExistente.setClienteId(1);
        clienteExistente.setEmail("old@mail.com");
        clienteExistente.setTelefono(111);

        ClienteRequest request = new ClienteRequest();
        request.setNombreCliente("NuevoNombre");
        request.setApellidoCliente("NuevoApellido");
        request.setEmail("new@mail.com");
        request.setTelefono(222);
        request.setContraseña("newpass");
        request.setDireccion("Nueva direccion");
        request.setFechaNacimiento(LocalDate.of(1990,1,1));
        request.setIdComuna(1);

        ComunaJpa comuna = new ComunaJpa();
        comuna.setIdComuna(1);

        when(clienteJpaRepository.findById(1)).thenReturn(Optional.of(clienteExistente));
        when(clienteJpaRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(clienteJpaRepository.findByTelefono(222)).thenReturn(Optional.empty());
        when(comunaJpaRepository.findById(1)).thenReturn(Optional.of(comuna));
        when(clienteJpaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ClienteResponse response = clienteService.updateCliente(1, request);

        assertEquals("NuevoNombre", response.getNombreCliente());
        assertEquals("NuevoApellido", response.getApellidoCliente());
        assertEquals("new@mail.com", response.getEmail());
        assertEquals(222, response.getTelefono());
        assertEquals("newpass", response.getContraseña());
        assertEquals("Nueva direccion", response.getDireccion());
        assertEquals(LocalDate.of(1990,1,1), response.getFechaNacimiento());
        assertEquals(1, response.getComuna().getIdComuna());
    }

    @Test
    public void UpdateCliente_EmailDuplicate_Throws() {
        ClienteJpa clienteExistente = new ClienteJpa();
        clienteExistente.setClienteId(1);
        clienteExistente.setEmail("old@mail.com");

        ClienteJpa otroCliente = new ClienteJpa();
        otroCliente.setClienteId(2);

        ClienteRequest request = new ClienteRequest();
        request.setEmail("emailDuplicado@mail.com");
        request.setIdComuna(1);

        when(clienteJpaRepository.findById(1)).thenReturn(Optional.of(clienteExistente));
        when(clienteJpaRepository.findByEmail("emailDuplicado@mail.com")).thenReturn(Optional.of(otroCliente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            clienteService.updateCliente(1, request);
        });
        assertTrue(ex.getMessage().contains("ya está registrado"));
    }
}
