package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.ItemCarritoRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ItemCarritoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ItemCarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemCarritoService {
    @Autowired
    private ItemCarritoJpaRepository itemCarritoJpaRepository;

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private CarritoJpaRepository carritoJpaRepository;

    public boolean verificarClienteConCarrito(Integer idCliente) {
        Optional<CarritoJpa> carrito = carritoJpaRepository.findByClienteId(idCliente);
        return carrito.isPresent();
    }

    public ItemCarritoResponse agregarItemCarrito(Integer idCliente, Integer idCarrito, ItemCarritoRequest itemRequest) {
        boolean clienteConCarrito = verificarClienteConCarrito(idCliente);
        if (!clienteConCarrito) {
            throw new RuntimeException("El cliente no tiene un carrito en el módulo 2");
        }

        CarritoJpa carrito = carritoJpaRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (!carrito.getClienteId().equals(idCliente)) {
            throw new RuntimeException("Este carrito no pertenece al cliente");
        }

        ProductoJpa producto = productoJpaRepository.findById(itemRequest.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStockDisponible() < itemRequest.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
        }

        ItemCarritoJpa item = new ItemCarritoJpa();
        item.setProducto(producto);
        item.setCantidad(itemRequest.getCantidad());
        item.setCarrito(carrito);
        item.setPrecioUnitario(producto.getPrecio());

        producto.setStockDisponible(producto.getStockDisponible() - itemRequest.getCantidad());
        productoJpaRepository.save(producto);

        ItemCarritoJpa itemGuardado = itemCarritoJpaRepository.save(item);

        return new ItemCarritoResponse(
                itemGuardado.getIdItemCarrito(),
                itemGuardado.getCarrito().getId(),
                itemGuardado.getProducto().getIdProducto(),
                itemGuardado.getCantidad(),
                itemGuardado.getProducto().getNombreProducto(),
                itemGuardado.getPrecioUnitario()
        );
    }

    public void eliminarItemCarrito(Integer idCliente, Integer idCarrito, Integer idItemCarrito) {
        // VERIFICAR SI EXISTE EL CLIENTE
        boolean clienteConCarrito = verificarClienteConCarrito(idCliente);
        if (!clienteConCarrito) {
            throw new RuntimeException("El cliente no tiene un carrito en el módulo 2");
        }

        // VERIFICAR SI EL CARRITO EXISTE
        CarritoJpa carrito = carritoJpaRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // VERIFICAR SI EL CARRITO PERTENECE AL CLIENTE
        if (!carrito.getClienteId().equals(idCliente)) {
            throw new RuntimeException("Este carrito no pertenece al cliente");
        }

        // VERIFICAR SI EL ITEM EXISTE
        ItemCarritoJpa item = itemCarritoJpaRepository.findById(idItemCarrito)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        // VERIFICAR QUE EL ITEM ES DEL CARRO
        if (!item.getCarrito().getId().equals(idCarrito)) {
            throw new RuntimeException("El item no pertenece al carrito");
        }

        // RESTAURAR EL STOCK
        ProductoJpa producto = item.getProducto();
        producto.setStockDisponible(producto.getStockDisponible() + item.getCantidad());
        productoJpaRepository.save(producto);

        itemCarritoJpaRepository.delete(item);
    }
}
