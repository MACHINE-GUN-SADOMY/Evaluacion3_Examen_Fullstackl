package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.CarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ItemCarritoJpaRepository;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.CarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ItemCarritoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCarritoService {
    @Autowired
    private ItemCarritoJpaRepository itemCarritoJpaRepository;

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private CarritoJpaRepository carritoJpaRepository;

    public ItemCarritoJpa agregarItemCarrito(Integer id, Integer idProducto, Integer cantidad) {
        // Verificar si el producto existe
        ProductoJpa producto = productoJpaRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + idProducto));

        // Verificar si hay suficiente stock
        if (producto.getStockDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
        }

        // Verificar si el carrito existe
        CarritoJpa carrito = carritoJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Crear un nuevo item de carrito
        ItemCarritoJpa item = new ItemCarritoJpa();
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setCarrito(carrito);
        item.setPrecioUnitario(producto.getPrecio());

        // Actualizar el stock del producto
        producto.setStockDisponible(producto.getStockDisponible() - cantidad);
        productoJpaRepository.save(producto); // Guardar el producto actualizado

        // Guardar el nuevo item de carrito
        return itemCarritoJpaRepository.save(item);
    }

    public void eliminarItemCarrito(Integer carritoId, Integer itemId) {
        ItemCarritoJpa item = itemCarritoJpaRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id " + itemId));

        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new RuntimeException("El item no pertenece al carrito especificado");
        }

        ProductoJpa producto = item.getProducto();
        producto.setStockDisponible(producto.getStockDisponible() + item.getCantidad());
        productoJpaRepository.save(producto);

        // Eliminar el item del carrito
        itemCarritoJpaRepository.delete(item);
    }
}
