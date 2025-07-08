package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.OrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.OrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ItemCarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.*;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class OrdenServiceTest {

    @Mock
    private CarritoJpaRepository carritoJpaRepository;

    @Mock
    private OrdenJpaRepository ordenJpaRepository;

    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @Mock
    private ItemCarritoJpaRepository itemCarritoJpaRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void CrearOrden_Success() {
        ItemCarritoJpa item1 = new ItemCarritoJpa();
        ProductoJpa producto1 = new ProductoJpa();
        producto1.setPrecio(10);
        item1.setProducto(producto1);
        item1.setCantidad(2);
        item1.setPrecioUnitario(10);

        ItemCarritoJpa item2 = new ItemCarritoJpa();
        ProductoJpa producto2 = new ProductoJpa();
        producto2.setPrecio(20);
        item2.setProducto(producto2);
        item2.setCantidad(3);
        item2.setPrecioUnitario(20);

        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(1);
        carrito.setClienteId(100);
        carrito.setFechaCreacion(LocalDate.now());
        carrito.setItems(Arrays.asList(item1, item2));

        when(carritoJpaRepository.findById(1)).thenReturn(Optional.of(carrito));
        when(ordenJpaRepository.save(any(OrdenJpa.class))).thenAnswer(invocation -> {
            OrdenJpa o = invocation.getArgument(0);
            o.setIdOrden(1); // simular id generado
            return o;
        });

        OrdenRequest request = new OrdenRequest();
        request.setIdCarrito(1);
        request.setIdCliente(100);
        request.setDireccionCliente("Dirección prueba");

        OrdenResponse response = ordenService.crearOrden(request);

        // Total esperado: (10*2)+(20*3)= 10*2=20 + 60=80
        assertEquals(1, response.getIdOrden());
        assertEquals(100, response.getIdCliente());
        assertEquals("Dirección prueba", response.getDireccionCliente());
        assertEquals(80, response.getMontoTotal());
        assertEquals("Pendiente", response.getEstadoOrden());
        assertEquals(1, response.getCarrito().getId());
    }

    @Test
    public void GetOrdenById_Found() {
        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(1);
        orden.setIdCliente(100);
        orden.setDireccionCliente("Dir 1");
        orden.setFechaOrden(LocalDate.now());
        orden.setMontoTotal(150);
        orden.setEstadoOrden("Pendiente");

        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(2);
        carrito.setClienteId(100);
        carrito.setFechaCreacion(LocalDate.now());
        carrito.setItems(Collections.emptyList());
        orden.setCarrito(carrito);

        when(ordenJpaRepository.findById(1)).thenReturn(Optional.of(orden));

        OrdenResponse response = ordenService.getOrdenById(1);

        assertEquals(1, response.getIdOrden());
        assertEquals("Dir 1", response.getDireccionCliente());
        assertEquals(150, response.getMontoTotal());
        assertEquals(2, response.getCarrito().getId());
    }

    @Test
    public void GetOrdenById_NotFound() {
        when(ordenJpaRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenService.getOrdenById(999);
        });

        assertEquals("Orden no encontrada", ex.getMessage());
    }

    @Test
    public void ModificarEstadoOrden_Valido() {
        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(1);
        orden.setEstadoOrden("Pendiente");

        when(ordenJpaRepository.findById(1)).thenReturn(Optional.of(orden));
        when(ordenJpaRepository.save(any(OrdenJpa.class))).thenReturn(orden);

        ordenService.modificarEstadoOrden(1, "Pagado");

        assertEquals("Pagado", orden.getEstadoOrden());
        verify(ordenJpaRepository).save(orden);
    }

    @Test
    public void ModificarEstadoOrden_Invalido() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenService.modificarEstadoOrden(1, "NoValido");
        });
        assertEquals("Estado de la orden no válido", ex.getMessage());
    }

    @Test
    public void DeleteOrdenById_Success() {
        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(1);
        CarritoJpa carrito = new CarritoJpa();
        orden.setCarrito(carrito);

        when(ordenJpaRepository.findById(1)).thenReturn(Optional.of(orden));

        ordenService.deleteOrdenById(1);

        verify(ordenJpaRepository).delete(orden);
    }

    @Test
    public void DeleteOrdenById_NotFound() {
        when(ordenJpaRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenService.deleteOrdenById(999);
        });
        assertTrue(ex.getMessage().contains("Orden no encontrada"));
    }
}
