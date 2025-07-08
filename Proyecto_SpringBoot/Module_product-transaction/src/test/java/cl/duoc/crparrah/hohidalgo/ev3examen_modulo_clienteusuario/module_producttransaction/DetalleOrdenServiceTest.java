package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.DetalleOrdenRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.DetalleOrdenResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.DetalleOrdenJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.*;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.OrdenJpaRepository;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.CarritoService;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.DetalleOrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class DetalleOrdenServiceTest {

    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @Mock
    private CarritoJpaRepository carritoJpaRepository;

    @Mock
    private OrdenJpaRepository ordenJpaRepository;

    @Mock
    private DetalleOrdenJpaRepository detalleOrdenJpaRepository;

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private DetalleOrdenService detalleOrdenService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GetDetalleOrdenById_Found() {
        DetalleOrdenJpa detalle = new DetalleOrdenJpa();
        detalle.setIdDetalleOrden(1);
        ProductoJpa producto = new ProductoJpa();
        producto.setIdProducto(100);
        producto.setNombreProducto("Producto 1");
        detalle.setProducto(producto);

        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(10);
        orden.setCarrito(null);
        detalle.setOrden(orden);

        when(detalleOrdenJpaRepository.findById(1)).thenReturn(Optional.of(detalle));

        DetalleOrdenResponse response = detalleOrdenService.getDetalleOrdenById(1);

        assertEquals(1, response.getId());
        assertEquals("Producto 1", response.getProducto().getNombreProducto());
        assertEquals(10, response.getOrden().getIdOrden());
    }

    @Test
    public void GetDetalleOrdenById_NotFound() {
        when(detalleOrdenJpaRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            detalleOrdenService.getDetalleOrdenById(999);
        });

        assertEquals("Detalle de orden no encontrado", ex.getMessage());
    }

    // METODO AUXILIAR
    private ItemCarritoJpa crearItemCarrito(int id, String nombreProducto, int cantidad, int precioUnitario) {
        ProductoJpa producto = new ProductoJpa();
        producto.setIdProducto(id);
        producto.setNombreProducto(nombreProducto);
        producto.setPrecio(precioUnitario);
        producto.setStockDisponible(precioUnitario);

        ItemCarritoJpa item = new ItemCarritoJpa();
        item.setIdItemCarrito(id);
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precioUnitario);

        return item;
    }

    @Test
    public void AgregarDetalleOrden_Success() {
        // Preparar mocks
        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(1);
        orden.setEstadoOrden("Pendiente");

        CarritoJpa carrito = new CarritoJpa();
        carrito.setItems(Arrays.asList(
                crearItemCarrito(1, "Producto A", 5, 10),
                crearItemCarrito(2, "Producto B", 2, 20)
        ));
        orden.setCarrito(carrito);

        DetalleOrdenRequest request = new DetalleOrdenRequest();
        request.setIdOrden(1);

        when(ordenJpaRepository.findById(1)).thenReturn(Optional.of(orden));
        when(detalleOrdenJpaRepository.save(any(DetalleOrdenJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = detalleOrdenService.agregarDetalleOrden(request);

        assertEquals("Detalles de la orden agregados correctamente", result);
        verify(detalleOrdenJpaRepository, times(2)).save(any(DetalleOrdenJpa.class));
    }
    @Test
    public void AgregarDetalleOrden_NoStock() {
        // Preparar mocks
        OrdenJpa orden = new OrdenJpa();
        orden.setIdOrden(1);
        orden.setEstadoOrden("Pendiente");

        ProductoJpa productoConPocoStock = new ProductoJpa();
        productoConPocoStock.setIdProducto(1);
        productoConPocoStock.setNombreProducto("Producto Limitado");
        productoConPocoStock.setStockDisponible(1);

        ItemCarritoJpa item = crearItemCarrito(1, "Producto Limitado", 5, 100); // Cantidad > stock
        item.setProducto(productoConPocoStock);

        CarritoJpa carrito = new CarritoJpa();
        carrito.setItems(Collections.singletonList(item));
        orden.setCarrito(carrito);

        DetalleOrdenRequest request = new DetalleOrdenRequest();
        request.setIdOrden(1);

        when(ordenJpaRepository.findById(1)).thenReturn(Optional.of(orden));

        String result = detalleOrdenService.agregarDetalleOrden(request);

        assertTrue(result.contains("Stock insuficiente"));
        verify(detalleOrdenJpaRepository, never()).save(any());
    }

    @Test
    public void DeleteDetalleOrden_Success() {
        DetalleOrdenJpa detalle = new DetalleOrdenJpa();
        detalle.setIdDetalleOrden(1);

        OrdenJpa orden = new OrdenJpa();
        orden.setEstadoOrden("Pendiente");
        detalle.setOrden(orden);

        when(detalleOrdenJpaRepository.findById(1)).thenReturn(Optional.of(detalle));

        detalleOrdenService.deleteDetalleOrden(1);

        verify(detalleOrdenJpaRepository).delete(detalle);
    }

    @Test
    public void DeleteDetalleOrden_EstadoPagado() {
        DetalleOrdenJpa detalle = new DetalleOrdenJpa();
        detalle.setIdDetalleOrden(1);

        OrdenJpa orden = new OrdenJpa();
        orden.setEstadoOrden("Pagado");
        detalle.setOrden(orden);

        when(detalleOrdenJpaRepository.findById(1)).thenReturn(Optional.of(detalle));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            detalleOrdenService.deleteDetalleOrden(1);
        });

        assertEquals("No se puede eliminar el detalle de la orden, ya que está pagado o cancelado", ex.getMessage());
        verify(detalleOrdenJpaRepository, never()).delete(any());
    }

}
