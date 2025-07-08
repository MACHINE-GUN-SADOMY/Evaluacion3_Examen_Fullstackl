package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.ClienteRestClient;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.CarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ItemCarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.CarritoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    @Mock
    private CarritoJpaRepository carritoJpaRepository;

    @Mock
    private ClienteRestClient clienteRestClient;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    void MapToResponse_CorrectMapping() {
        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(1);
        carrito.setClienteId(10);
        carrito.setFechaCreacion(LocalDate.now());

        ProductoJpa producto = new ProductoJpa();
        producto.setIdProducto(100);
        producto.setNombreProducto("Producto Test");

        ItemCarritoJpa item = new ItemCarritoJpa();
        item.setIdItemCarrito(50);
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(5);
        item.setPrecioUnitario(15000);

        carrito.setItems(List.of(item));

        CarritoResponse response = carritoService.mapToResponse(carrito);

        assertEquals(1, response.getId());
        assertEquals(10, response.getIdCliente());
        assertEquals(5, response.getCantidadTotal());

        assertEquals(1, response.getItems().size());
        assertEquals(5, response.getItems().get(0).getCantidad());
        assertEquals("Producto Test", response.getItems().get(0).getNombreProducto());
    }

    @Test
    void CrearCarritoParaCliente_Success() {
        Integer clienteId = 100;

        when(clienteRestClient.verificarCliente(clienteId))
                .thenReturn(ResponseEntity.ok().build());

        when(carritoJpaRepository.findByClienteId(clienteId))
                .thenReturn(Optional.empty());

        when(carritoJpaRepository.save(any(CarritoJpa.class))).thenAnswer(invocation -> {
            CarritoJpa carritoGuardado = invocation.getArgument(0);
            carritoGuardado.setId(1);
            return carritoGuardado;
        });

        CarritoResponse response = carritoService.crearCarritoParaCliente(clienteId);

        assertEquals(clienteId, response.getIdCliente());
        assertEquals(1, response.getId());
        assertEquals(0, response.getCantidadTotal()); // Sin items aún
        assertTrue(response.getItems().isEmpty());
    }


    @Test
    void FindByCarritoId_Found() {
        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(1);
        when(carritoJpaRepository.findById(1)).thenReturn(Optional.of(carrito));

        CarritoResponse response = carritoService.findByCarritoId(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void FindByCarritoId_NotFound() {
        when(carritoJpaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.findByCarritoId(1));
        assertEquals("Carrito no encontrado", exception.getMessage());
    }

    @Test
    void GetAllCarritos_ReturnsList() {
        CarritoJpa carrito1 = new CarritoJpa();
        carrito1.setId(1);
        CarritoJpa carrito2 = new CarritoJpa();
        carrito2.setId(2);

        when(carritoJpaRepository.findAll()).thenReturn(Arrays.asList(carrito1, carrito2));

        List<CarritoResponse> responses = carritoService.getAllCarritos();

        assertEquals(2, responses.size());
    }


    @Test
    void CrearCarritoParaCliente_CarritoYaExiste() {
        when(clienteRestClient.verificarCliente(100)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        CarritoJpa carritoExistente = new CarritoJpa();
        when(carritoJpaRepository.findByClienteId(100)).thenReturn(Optional.of(carritoExistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.crearCarritoParaCliente(100));
        assertEquals("El cliente ya tiene un carrito", exception.getMessage());
    }

    @Test
    void DeleteCarritoBy_Success() {
        when(carritoJpaRepository.existsById(1)).thenReturn(true);

        carritoService.deleteCarritoBy(1);

        verify(carritoJpaRepository).deleteById(1);
    }

    @Test
    void DeleteCarritoBy_NoExiste() {
        when(carritoJpaRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> carritoService.deleteCarritoBy(1));
        assertEquals("Carrito no existe", exception.getMessage());
    }

    @Test
    void VerificarCliente_Existe() {
        when(clienteRestClient.verificarCliente(100)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        assertTrue(carritoService.verificarCliente(100));
    }

    @Test
    void VerificarCliente_NotExiste() {
        when(clienteRestClient.verificarCliente(100)).thenThrow(new RuntimeException());
        assertFalse(carritoService.verificarCliente(100));
    }

    @Test
    void CrearCarritoParaCliente_ClienteNoExiste() {
        when(clienteRestClient.verificarCliente(100)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            carritoService.crearCarritoParaCliente(100);
        });

        assertEquals("Cliente no existe", exception.getMessage());
    }
}
