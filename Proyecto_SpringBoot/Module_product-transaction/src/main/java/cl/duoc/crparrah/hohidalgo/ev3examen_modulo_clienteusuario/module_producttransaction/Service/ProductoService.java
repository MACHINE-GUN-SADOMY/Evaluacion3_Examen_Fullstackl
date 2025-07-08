package cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Service;

import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Request.ProductoRequest;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Api_Client.Response.ProductoResponse;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.Jpa.ProductoJpa;
import cl.duoc.crparrah.hohidalgo.ev3examen_modulo_clienteusuario.module_producttransaction.Repository.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    private RestClient restClient;

    private ProductoResponse mapToProductoResponse(ProductoJpa producto) {
        return new ProductoResponse(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getDescripcionProducto(),
                producto.getPrecio(),
                producto.getSku(),
                producto.getStockDisponible(),
                producto.getCategoria()
        );
    }

    public ProductoResponse crearProducto(ProductoRequest request) {
        if (productoJpaRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("El SKU " + request.getSku() + " ya existe");
        }

        ProductoJpa producto = new ProductoJpa();
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcionProducto(request.getDescripcionProducto());
        producto.setSku(request.getSku());
        producto.setStockDisponible(request.getStockDisponible());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        productoJpaRepository.save(producto);

        ProductoJpa savedProducto = productoJpaRepository.save(producto);
        return mapToProductoResponse(savedProducto);
    }

    public ProductoResponse actualizarProducto(Integer id, ProductoRequest request) {
        ProductoJpa producto = productoJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!producto.getSku().equals(request.getSku()) && productoJpaRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("El SKU " + request.getSku() + " ya está en uso");
        }

        producto.setNombreProducto(request.getNombreProducto());
        producto.setSku(request.getSku());
        producto.setStockDisponible(request.getStockDisponible());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());

        return mapToProductoResponse(productoJpaRepository.save(producto));
    }

    public List<ProductoJpa> findAllProductos() {
        return productoJpaRepository.findAll();
    }

    public Optional<ProductoJpa> findById(Integer id) {
        return productoJpaRepository.findById(id);
    }

    public void deleteProducto(Integer id) {
        if (!productoJpaRepository.existsById(id)) {
            throw new RuntimeException("Producto con ID " + id + " no existe");
        }productoJpaRepository.deleteById(id);
    }
}
