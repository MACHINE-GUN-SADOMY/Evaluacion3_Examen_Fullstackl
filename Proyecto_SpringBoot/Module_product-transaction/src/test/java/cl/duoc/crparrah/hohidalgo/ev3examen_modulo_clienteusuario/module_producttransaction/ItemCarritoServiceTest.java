package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.ItemCarritoRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ItemCarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ItemCarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.ItemCarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ItemCarritoServiceTest {

    @Mock
    private ItemCarritoJpaRepository itemCarritoJpaRepository;

    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @Mock
    private CarritoJpaRepository carritoJpaRepository;

    @InjectMocks
    private ItemCarritoService itemCarritoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void AgregarItemCarrito_Success() {
        Integer idCliente = 1;
        Integer idCarrito = 10;
        Integer idProducto = 100;
        int cantidad = 2;

        ItemCarritoRequest request = new ItemCarritoRequest();
        request.setIdProducto(idProducto);
        request.setCantidad(cantidad);

        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(idCarrito);
        carrito.setClienteId(idCliente);

        when(carritoJpaRepository.findByClienteId(idCliente)).thenReturn(Optional.of(carrito));
        when(carritoJpaRepository.findById(idCarrito)).thenReturn(Optional.of(carrito));

        ProductoJpa producto = new ProductoJpa();
        producto.setIdProducto(idProducto);
        producto.setNombreProducto("Producto Test");
        producto.setPrecio(10000);
        producto.setStockDisponible(5);

        when(productoJpaRepository.findById(idProducto)).thenReturn(Optional.of(producto));
        when(productoJpaRepository.save(any(ProductoJpa.class))).thenReturn(producto);

        ItemCarritoJpa itemGuardado = new ItemCarritoJpa();
        itemGuardado.setIdItemCarrito(50);
        itemGuardado.setCantidad(cantidad);
        itemGuardado.setCarrito(carrito);
        itemGuardado.setProducto(producto);
        itemGuardado.setPrecioUnitario(producto.getPrecio());

        when(itemCarritoJpaRepository.save(any(ItemCarritoJpa.class))).thenReturn(itemGuardado);

        ItemCarritoResponse response = itemCarritoService.agregarItemCarrito(idCliente, idCarrito, request);

        assertEquals(idCarrito, response.getIdCarrito());
        assertEquals(idProducto, response.getIdProducto());
        assertEquals(cantidad, response.getCantidad());
        assertEquals("Producto Test", response.getNombreProducto());
        assertEquals(10000, response.getPrecioUnitario());
        assertEquals(50, response.getIdItemCarrito());

        assertEquals(3, producto.getStockDisponible());
        verify(productoJpaRepository).save(producto);
        verify(itemCarritoJpaRepository).save(any(ItemCarritoJpa.class));
    }

    @Test
    void EliminarItemCarrito_Success() {
        Integer idCliente = 1;
        Integer idCarrito = 10;
        Integer idItemCarrito = 50;

        CarritoJpa carrito = new CarritoJpa();
        carrito.setId(idCarrito);
        carrito.setClienteId(idCliente);

        ProductoJpa producto = new ProductoJpa();
        producto.setIdProducto(100);
        producto.setNombreProducto("Producto Test");
        producto.setPrecio(10000);
        producto.setStockDisponible(5);

        ItemCarritoJpa item = new ItemCarritoJpa();
        item.setIdItemCarrito(idItemCarrito);
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(2);
        item.setPrecioUnitario(10000);

        when(carritoJpaRepository.findByClienteId(idCliente)).thenReturn(Optional.of(carrito));
        when(carritoJpaRepository.findById(idCarrito)).thenReturn(Optional.of(carrito));
        when(itemCarritoJpaRepository.findById(idItemCarrito)).thenReturn(Optional.of(item));
        when(productoJpaRepository.save(any(ProductoJpa.class))).thenReturn(producto);

        itemCarritoService.eliminarItemCarrito(idCliente, idCarrito, idItemCarrito);

        assertEquals(7, producto.getStockDisponible());
        verify(productoJpaRepository).save(producto);
        verify(itemCarritoJpaRepository).delete(item);
    }
}
