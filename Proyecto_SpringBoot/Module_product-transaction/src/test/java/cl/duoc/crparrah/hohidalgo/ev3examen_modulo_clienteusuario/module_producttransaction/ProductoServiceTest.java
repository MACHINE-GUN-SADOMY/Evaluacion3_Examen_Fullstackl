package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.ProductoRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ProductoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.*;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service.ProductoService;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @InjectMocks
    private ProductoService productoService;

    private ProductoJpa productoJpa;
    private ProductoRequest productoRequest;

    @BeforeEach
    void setup() {
        productoJpa = new ProductoJpa();
        productoJpa.setIdProducto(1);
        productoJpa.setNombreProducto("Test Producto");
        productoJpa.setDescripcionProducto("Descripción");
        productoJpa.setSku("SKU123");
        productoJpa.setPrecio(100);
        productoJpa.setStockDisponible(50);
        productoJpa.setCategoria("Categoría");

        productoRequest = new ProductoRequest();
        productoRequest.setNombreProducto("Test Producto");
        productoRequest.setDescripcionProducto("Descripción");
        productoRequest.setSku("SKU123");
        productoRequest.setPrecio(100);
        productoRequest.setStockDisponible(50);
        productoRequest.setCategoria("Categoría");
    }

    @Test
    void CrearProducto_SkuNoExiste_CreaProducto() {
        when(productoJpaRepository.findBySku(productoRequest.getSku())).thenReturn(Optional.empty());
        when(productoJpaRepository.save(any(ProductoJpa.class))).thenReturn(productoJpa);

        ProductoResponse response = productoService.crearProducto(productoRequest);

        assertNotNull(response);
        assertEquals(productoJpa.getNombreProducto(), response.getNombreProducto());
        verify(productoJpaRepository).findBySku(productoRequest.getSku());
        verify(productoJpaRepository, times(2)).save(any(ProductoJpa.class));
    }

    @Test
    void CrearProducto_SkuExiste_LanzaExcepcion() {
        when(productoJpaRepository.findBySku(productoRequest.getSku())).thenReturn(Optional.of(productoJpa));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.crearProducto(productoRequest);
        });

        assertEquals("El SKU " + productoRequest.getSku() + " ya existe", exception.getMessage());
        verify(productoJpaRepository).findBySku(productoRequest.getSku());
        verify(productoJpaRepository, never()).save(any());
    }

    @Test
    void ActualizarProducto_SkuDisponible_Actualiza() {
        ProductoJpa productoGuardado = new ProductoJpa();
        productoGuardado.setIdProducto(1);
        productoGuardado.setSku("SKU_ANTERIOR");
        productoRequest.setSku("SKU123");

        when(productoJpaRepository.findById(1)).thenReturn(Optional.of(productoGuardado));
        when(productoJpaRepository.findBySku(productoRequest.getSku())).thenReturn(Optional.empty());
        when(productoJpaRepository.save(any(ProductoJpa.class))).thenReturn(productoJpa);

        ProductoResponse response = productoService.actualizarProducto(1, productoRequest);

        assertNotNull(response);
        assertEquals(productoRequest.getSku(), response.getSku());
        verify(productoJpaRepository).findById(1);
        verify(productoJpaRepository).findBySku(productoRequest.getSku());
        verify(productoJpaRepository).save(any(ProductoJpa.class));
    }

    @Test
    void ActualizarProducto_SkuEnUso_LanzaExcepcion() {
        ProductoJpa productoGuardado = new ProductoJpa();
        productoGuardado.setIdProducto(1);
        productoGuardado.setSku("SKU_ANTERIOR");

        ProductoJpa otroProducto = new ProductoJpa();
        otroProducto.setIdProducto(2);
        otroProducto.setSku(productoRequest.getSku());

        when(productoJpaRepository.findById(1)).thenReturn(Optional.of(productoGuardado));
        when(productoJpaRepository.findBySku(productoRequest.getSku())).thenReturn(Optional.of(otroProducto));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarProducto(1, productoRequest);
        });

        assertEquals("El SKU " + productoRequest.getSku() + " ya está en uso", exception.getMessage());
        verify(productoJpaRepository).findById(1);
        verify(productoJpaRepository).findBySku(productoRequest.getSku());
        verify(productoJpaRepository, never()).save(any());
    }

    @Test
    void FindAllProductos_RetornaLista() {
        List<ProductoJpa> lista = new ArrayList<>();
        lista.add(productoJpa);

        when(productoJpaRepository.findAll()).thenReturn(lista);

        List<ProductoJpa> result = productoService.findAllProductos();

        assertFalse(result.isEmpty());
        verify(productoJpaRepository).findAll();
    }

    @Test
    void FindById_Existente() {
        when(productoJpaRepository.findById(1)).thenReturn(Optional.of(productoJpa));

        Optional<ProductoJpa> resultado = productoService.findById(1);

        assertTrue(resultado.isPresent());
        assertEquals(productoJpa.getIdProducto(), resultado.get().getIdProducto());
        verify(productoJpaRepository).findById(1);
    }

    @Test
    void DeleteProducto_Existente() {
        when(productoJpaRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> productoService.deleteProducto(1));

        verify(productoJpaRepository).existsById(1);
        verify(productoJpaRepository).deleteById(1);
    }

    @Test
    void DeleteProducto_NoExistente() {
        when(productoJpaRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.deleteProducto(1);
        });

        assertEquals("Producto con ID 1 no existe", exception.getMessage());
        verify(productoJpaRepository).existsById(1);
        verify(productoJpaRepository, never()).deleteById(1);
    }
}

